package net.ntg.ftl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;

import org.jdom2.JDOMException;

import net.vhati.modmanager.core.FTLUtilities;

import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.DefaultDataManager;
import net.blerf.ftl.parser.SavedGameParser;

import net.ntg.ftl.ui.FTLFrame;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class FTLAdventureVisualiser {

	private static final Logger log = LogManager.getLogger(FTLAdventureVisualiser.class);

	public static String APP_NAME = "FTL Adventure Visualiser";
	public static int APP_VERSION = 3;

	private static File configFile = new File("FTLAVconfig.cfg");
	public static File gameStateFile;

	public static SavedGameParser.SavedGameState gameState = null;
	public static SavedGameParser.ShipState shipState = null;
	// public static SavedGameParser.ShipState nearbyShipState = null;
	public static List<SavedGameParser.CrewState> playerCrewState;
	public static List<SavedGameParser.CrewState> enemyCrewState;
	public static SavedGameParser.EnvironmentState environmentState = null;
	public static ArrayList<SectorDot> sectorArray = new ArrayList<>();
	public static String fileChangedTimeStamp;

	public static String recordFilePath;
	public static ArrayList<Map<String, String>> recording = new ArrayList<>();
	public static String[] recordingHeaders;


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiInit();
			}
		});
	}


	private static void guiInit() {
		// Don't use the hard drive to buffer streams during ImageIO.read().
		ImageIO.setUseCache(false); // Small images don't need extra buffering.

		log.debug(String.format("%s v%s", APP_NAME, APP_VERSION));
		log.debug(String.format("%s %s", System.getProperty("os.name"), System.getProperty("os.version")));
		log.debug(String.format("%s, %s, %s", System.getProperty("java.vm.name"), System.getProperty("java.version"), System.getProperty("os.arch")));

		File datsDir = null;

		Properties config = readConfig();

		// FTL Resources Path.
		String datsPath = config.getProperty("ftlDatsPath");
		String continuePath = config.getProperty("ftlContinuePath");

		if (datsPath != null && datsPath.length() > 0) {
			log.info("Using FTL dats path from config: " + datsPath);
			datsDir = new File(datsPath);
			if (!FTLUtilities.isDatsDirValid(datsDir)) {
				log.error("The config's ftlDatsPath does not exist, or it lacks data.dat.");
				datsDir = null;
			} else {

				File candidateSaveFile;
				if (continuePath != null) {
					log.info("The config's ftlContinuePath exists");
					candidateSaveFile = new File(continuePath);
				} else {
					log.info("No path to continue.sav found in config. Guessing possible location...");
					candidateSaveFile = new File(FTLUtilities.findUserDataDir(), "continue.sav");
				}
				if (candidateSaveFile.exists()) {
					gameStateFile = candidateSaveFile;
					config.setProperty("ftlContinuePath", candidateSaveFile.getAbsolutePath());
					writeConfig(config);
				} else {
					log.error(candidateSaveFile.getAbsolutePath() + " doesn't seem to be a valid FTL save file because it doesn't exist or is invalid");
				}

			}
		} else {
			log.trace("No FTL dats path previously set.");
		}

		// Find/prompt for the path to set in the config.
		if (datsDir == null) {
			datsDir = FTLUtilities.findDatsDir();
			if (datsDir != null) {
				// TODO a more welcoming confirm dialog
				int response = JOptionPane.showConfirmDialog(
					null,
					"FTL resources were found in:\n"+ datsDir.getPath() +"\nIs this correct?",
					String.format("%s %s - Setup", APP_NAME, APP_VERSION),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE
				);
				if (response == JOptionPane.NO_OPTION) datsDir = null;
			}

			if (datsDir == null) {
				log.debug("FTL dats path was not located automatically. Prompting user for location.");
				datsDir = FTLUtilities.promptForDatsDir(null);
			}

			if (datsDir != null) {
				config.setProperty("ftlDatsPath", datsDir.getAbsolutePath());

				File candidateSaveFile = new File(FTLUtilities.findUserDataDir(), "continue.sav");
				if (candidateSaveFile.exists()) {
					gameStateFile = candidateSaveFile;
					config.setProperty("ftlContinuePath", candidateSaveFile.getAbsolutePath());
				}
				writeConfig(config);
				log.info("FTL dats located at: " + datsDir.getAbsolutePath());
			}
		}

		if (datsDir == null) {
			showErrorDialog("FTL data was not found.\nFTL Adventure Visualiser will now exit.");
			log.debug("No FTL dats path found, exiting.");
			System.exit(1);
		}

		// Parse the dats.
		try {
			DefaultDataManager dataManager = new DefaultDataManager(datsDir);
			DataManager.setInstance(dataManager);
			dataManager.setDLCEnabledByDefault(true);
		} catch (IOException | JAXBException | JDOMException e) {
			log.error("Error parsing FTL resources.", e);
			showErrorDialog("Error parsing FTL resources.");
			System.exit(1);
		}

		try {
			FTLFrame frame = new FTLFrame(APP_NAME, APP_VERSION);
			frame.setVisible(true);
		} catch (Exception e) {
			log.error("Exception while creating FTLFrame.", e);
			System.exit(1);
		}

	}


	public static Properties readConfig() {
		Properties config = new Properties();
		// Read the config file.
		InputStream in = null;
		try {
			if (configFile.exists()) {
				log.trace("Loading properties from config file.");
				in = new FileInputStream(configFile);
				config.load(new InputStreamReader(in, "UTF-8"));
			} else {
				writeConfig(config);
			}
		} catch (IOException e) {
			log.error("Error loading config.", e);
			showErrorDialog("Error loading config from " + configFile.getPath());
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {}
		}
		return config;
	}

	public static void writeConfig(String key, String value) {
		writeConfig((Properties) readConfig().setProperty(key, value));
	}

	public static void writeConfig(Properties config) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(configFile);
			String configComments = APP_NAME + " " + APP_VERSION + " - Config File";
			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			config.store(writer, configComments);
			writer.flush();
		} catch (IOException e) {
			log.error("Error saving config to "+ configFile.getPath(), e);
			showErrorDialog("Error saving config to " + configFile.getPath());
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {}
		}
	}


	private static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

}