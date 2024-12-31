package nl.nielspoldervaart.ftlav;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.model.sectortree.SectorTree;
import net.blerf.ftl.model.state.SavedGameState;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.DefaultDataManager;
import net.blerf.ftl.parser.SavedGameParser;
import net.blerf.ftl.parser.random.FTL_1_6_Random;
import net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator;
import nl.nielspoldervaart.ftlav.data.TableMaker;
import nl.nielspoldervaart.ftlav.data.TableRow;
import nl.nielspoldervaart.ftlav.data.VisualiserAnnotation;
import nl.nielspoldervaart.ftlav.ui.FTLFrame;
import net.vhati.modmanager.core.FTLUtilities;
import nl.nielspoldervaart.ftlav.visualiser.GraphLineColor;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;


@Slf4j
public class FTLAdventureVisualiser {

	public static final String APP_NAME = "FTL Adventure Visualiser";
	public static final int APP_VERSION = 3;

	private static final Preferences prefs = Preferences.userNodeForPackage(FTLAdventureVisualiser.class);
	private static final String FTL_DAT_PATH = "ftlDatsPath";
	private static final String FTL_CONTINUE_PATH = "ftlContinuePath";
	private static final String FTL_PROFILE_PATH = "ftlProfilePath";
	private static final String FTL_AE_PROFILE_PATH = "ftlAEProfilePath";

	private static File gameDatsDir;

	public static File gameStateFile;
	public static SavedGameState gameState = null;
	public static ArrayList<SectorDot> sectorList = new ArrayList<>();

	public static ArrayList<TableRow> recording = new ArrayList<>();

	public static File recordsExportFile;

	public static HashMap<String, Boolean> columnsInVisualiser = new HashMap<>();
	public static HashMap<String, GraphLineColor> colorsInVisualiser = new HashMap<>();

	public static void main(String[] args) {
		initLogger();
		SwingUtilities.invokeLater(FTLAdventureVisualiser :: guiInit);
	}

	private static void initLogger() {
		// Redirect any libraries' java.util.Logging messages.
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		// Doing this here instead of in "logback.xml", allows for conditional log files.
		// For example, the app could decide not to or in a different place.

		// Fork log into a file.
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(lc);
		encoder.setCharset(StandardCharsets.UTF_8);
		encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		encoder.start();

		FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
		fileAppender.setContext(lc);
		fileAppender.setName("LogFile");
		fileAppender.setFile(new File("./ftlav-log.txt").getAbsolutePath());
		fileAppender.setAppend(false);
		fileAppender.setEncoder(encoder);
		fileAppender.start();

		lc.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(fileAppender);
		for (ch.qos.logback.classic.Logger logger : lc.getLoggerList()) {
			logger.setLevel(Level.INFO);
		}
		lc.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.WARN);
	}

	private static void guiInit() {
		// Don't use the hard drive to buffer streams during ImageIO.read().
		ImageIO.setUseCache(false); // Small images don't need extra buffering.

		// Try to get an OS-native look and feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ignored) {}

		log.debug("{} v{}", APP_NAME, APP_VERSION);
		log.debug("{} {}", System.getProperty("os.name"), System.getProperty("os.version"));
		log.debug("{}, {}, {}", System.getProperty("java.vm.name"), System.getProperty("java.version"), System.getProperty("os.arch"));

		gameDatsDir = loadDatsDir();

		// Parse the dats.
		try {
			DefaultDataManager dataManager = new DefaultDataManager(gameDatsDir);
			DataManager.setInstance(dataManager);
			dataManager.setDLCEnabledByDefault(true);

			loadGameState(false);

		} catch (IOException | JAXBException | JDOMException e) {
			log.error("Error parsing FTL resources.", e);
			showErrorDialog("Error parsing FTL resources.");
			prefs.remove(FTL_DAT_PATH);
			System.exit(1);
			return;
		}

		try {
			FTLFrame frame = new FTLFrame(APP_NAME, APP_VERSION, prefs);
			frame.setVisible(true);
		} catch (Exception e) {
			log.error("Exception while creating FTLFrame.", e);
			System.exit(1);
		}

	}

	private static File loadDatsDir() {
		File datsDir = null;

		// FTL Resources Path.
		String datsPath = prefs.get(FTL_DAT_PATH, "");

		if (!datsPath.isEmpty()) {
			log.info("Using FTL dats path from config: {}", datsPath);
			datsDir = new File(datsPath);
			if (!FTLUtilities.isDatsDirValid(datsDir)) {
				log.error("The config's {}:{} does not exist, or it lacks data.dat.", FTL_DAT_PATH, datsPath);
				datsDir = null;
			}
		}

		if (datsDir == null) {
			datsDir = FTLUtilities.findDatsDir();
			if (datsDir != null) {
				int response = JOptionPane.showConfirmDialog(
					null,
					"FTL resources were found in:\n" + datsDir.getPath() + "\nIs this correct?",
					"Confirm",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE
				);
				if (response == JOptionPane.NO_OPTION) {
					datsDir = null;
				}
			}

			if (datsDir == null) {
				datsDir = FTLUtilities.promptForDatsDir(null);
			}

			if (datsDir != null) {
				prefs.put(FTL_DAT_PATH, datsDir.getAbsolutePath());
				log.info("FTL dats located at: {}", datsDir.getAbsolutePath());
			}
		}

		if (datsDir == null) {
			showErrorDialog("FTL resources were not found.\nFTLAV will now exit.");
			log.debug("No FTL dats path found, exiting.");
			System.exit(1);
		}
		return datsDir;
	}

	public static void loadGameState(boolean skipAutoDetectContinueFile) {
		File chosenFile = skipAutoDetectContinueFile ? loadContinueFileWithFileChooser(null) : loadContinueFile();
		if (chosenFile == null) {
			return;
		}
		log.info("Reading game state: {}", chosenFile.getAbsoluteFile());
		log.info("Table rows: {}", recording.size() + 1);

		try {
			loadGameState(chosenFile);
		} catch (Exception e) {
			log.error("Reading game state from file {} failed: {}", chosenFile.getAbsoluteFile(), e.getMessage());
			showErrorDialog(String.format("Reading game state from file %s failed: %s", chosenFile.getAbsoluteFile(), e.getMessage()));
		}
	}

	public static void loadGameState(File chosenFile) throws Exception {
		FileInputStream in = null;
		try {
			in = new FileInputStream(chosenFile);
			StringBuilder hexBuf = new StringBuilder();

			// Read the content in advance, in case an error occurs.
			byte[] buf = new byte[4096];
			int len;
			while ((len = in.read(buf)) >= 0) {
				for (int i = 0; i < len; i++) {
					hexBuf.append(String.format("%02x", buf[i]));
					if ((i + 1) % 32 == 0) {
						hexBuf.append("\n");
					}
				}
			}
			in.getChannel().position(0);

			SavedGameParser parser = new SavedGameParser();
			SavedGameState newGameState = parser.readSavedGame(in);
			if (
				gameState != null &&
				newGameState.getTotalBeaconsExplored() <= gameState.getTotalBeaconsExplored()
			) {
				return;
			}
			gameState = newGameState;

			RandomSectorTreeGenerator sectorTreeGenerator = new RandomSectorTreeGenerator(new FTL_1_6_Random());
			SectorTree sectorTree = new SectorTree();
			sectorTree.setSectorDots(sectorTreeGenerator.generateSectorTree(gameState.getSectorTreeSeed(), gameState.isDLCEnabled()));
			sectorTree.setSectorVisitation(gameState.getSectorVisitation());
			int lastVisitedColumn = sectorTree.getLastVisitedColumn();
			sectorList.clear();
			for (int c = 0; c <= lastVisitedColumn; c++) {
				sectorList.add(sectorTree.getVisitedDot(c));
			}

			log.info("Ship name: {}", gameState.getPlayerShipName());
			log.info("Number of beacons explored: {}", gameState.getTotalBeaconsExplored());
			log.info("Currently in sector: {}", gameState.getSectorNumber() + 1);

			gameStateFile = chosenFile;

			String timeStamp = getTimeStamp(chosenFile);
			recording.add(new TableRow(gameState, timeStamp));

			setDefaultVisualiserDataColumnVisibility();

			makeGameStateTable();

		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ignored) {}
		}
	}

	private static File loadContinueFile() {
		File candidateSaveFile;

		String continueFilePath = prefs.get(FTL_CONTINUE_PATH, "");
		if (!continueFilePath.isEmpty()) {
			log.info("Using FTL continue path from config: {}", continueFilePath);
			candidateSaveFile = new File(continueFilePath);
			if (candidateSaveFile.exists()) {
				if (validateContinueFile(candidateSaveFile)) {
					return candidateSaveFile;
				} else if (confirmLoadInvalidContinueFile(continueFilePath)) {
					return candidateSaveFile;
				}
			}
		}

		log.info("No valid FTL continue file path was found in config");

		candidateSaveFile = new File(FTLUtilities.findUserDataDir(), "continue.sav");
		if (validateContinueFile(candidateSaveFile)) {
			log.info("Found continue.sav at the expected location {}", candidateSaveFile.getPath());
			prefs.put(FTL_CONTINUE_PATH, candidateSaveFile.getAbsolutePath());
			return candidateSaveFile;
		}

		candidateSaveFile = loadContinueFileWithFileChooser(candidateSaveFile);
		if (candidateSaveFile == null || !candidateSaveFile.exists()) {
			prefs.remove(FTL_CONTINUE_PATH);
			return candidateSaveFile;
		}

		return candidateSaveFile;
	}

	private static File loadContinueFileWithFileChooser(File candidateSaveFile) {
		JFileChooser continueFileChooser = new JFileChooser();
		continueFileChooser.setFileHidingEnabled(false);
		continueFileChooser.setMultiSelectionEnabled(false);
		continueFileChooser.setDialogTitle("Choose FTL saved game (continue.sav)");
		continueFileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().equalsIgnoreCase("continue.sav");
			}

			@Override
			public String getDescription() {
				return "FTL saved game (continue.sav)";
			}
		});

		if (candidateSaveFile != null && candidateSaveFile.exists()) {
			continueFileChooser.setSelectedFile(candidateSaveFile);
		} else {
			continueFileChooser.setCurrentDirectory(FTLUtilities.findUserDataDir());
		}
		int fileChooserResult = continueFileChooser.showOpenDialog(null);
		candidateSaveFile = continueFileChooser.getSelectedFile();
		if (
			fileChooserResult == JFileChooser.APPROVE_OPTION &&
			candidateSaveFile != null &&
			candidateSaveFile.exists()
		) {
			if (validateContinueFile(candidateSaveFile)) {
				prefs.put(FTL_CONTINUE_PATH, candidateSaveFile.getAbsolutePath());
				return candidateSaveFile;
			} else if (confirmLoadInvalidContinueFile(candidateSaveFile.getAbsolutePath())) {
				prefs.put(FTL_CONTINUE_PATH, candidateSaveFile.getAbsolutePath());
				return candidateSaveFile;
			}
		}

		return candidateSaveFile;
	}

	private static boolean validateContinueFile(File continueFile) {
		if (continueFile == null || !continueFile.exists()) {
			return false;
		}
		return continueFile.getName().equalsIgnoreCase("continue.sav");
	}

	private static boolean confirmLoadInvalidContinueFile(String continueFilePath) {
		int response = JOptionPane.showConfirmDialog(
			null,
			String.format("Warning, selected file %s might not be a valid FTL save file!", continueFilePath),
			"Potentially invalid save file",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);
		return response == JOptionPane.YES_OPTION;
	}

	private static void setDefaultVisualiserDataColumnVisibility() {
		Field[] fields = TableRow.class.getDeclaredFields();
		for (Field field : fields) {
			VisualiserAnnotation annotation = field.getAnnotation(VisualiserAnnotation.class);
			if (annotation != null) {
				columnsInVisualiser.putIfAbsent(field.getName(), annotation.isEnabledByDefault());
				colorsInVisualiser.putIfAbsent(field.getName(), annotation.defaultGraphLineColor());
			}
		}
	}

	public static void unsetGameState() {
		gameState = null;
		gameStateFile = null;
		recording.clear();
		sectorList.clear();
	}

	private static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static String getTimeStamp() {
		return getTimeStamp(gameStateFile).replaceAll("T", " ");
	}
	public static String getTimeStamp(File file) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		return dateFormat.format(file.lastModified());
	}

	public static boolean hasGameState() {
		return gameState != null;
	}

	public static boolean hasRecords() {
		return !recording.isEmpty();
	}

	public static void makeGameStateTable() throws IOException {
		if (recordsExportFile != null && hasGameState()) {
			if (recordsExportFile.exists()) {
				log.info("Writing game state recording table file at {}", recordsExportFile.getAbsolutePath());
			} else {
				log.info("Creating game state recording table file at {} ", recordsExportFile.getAbsolutePath());
			}
			new TableMaker(recordsExportFile);
		}
	}
}
