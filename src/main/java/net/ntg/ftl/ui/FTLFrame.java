package net.ntg.ftl.ui;

import net.blerf.ftl.model.Profile;
import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.MysteryBytes;
import net.blerf.ftl.parser.ProfileParser;
import net.blerf.ftl.parser.SavedGameParser;
import net.blerf.ftl.parser.random.NativeRandom;
import net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator;
import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ParseCSV;
import net.ntg.ftl.util.FileWatcher;
import net.vhati.modmanager.core.FTLUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.prefs.Preferences;


public class FTLFrame extends JFrame {

	private static final Logger log = LogManager.getLogger(FTLFrame.class);

	private JFrame graphFrame;
	private JFrame helpFrame;

	private static final Preferences prefs = Preferences.userNodeForPackage(net.ntg.ftl.FTLAdventureVisualiser.class);

	private SavedGameParser.SavedGameState lastGameState = null;

	private final ImageIcon loadGameIcon	= new ImageIcon(ClassLoader.getSystemResource("loadgame.gif"));
	private final ImageIcon isRecordingIcon	= new ImageIcon(ClassLoader.getSystemResource("recording.gif"));
	private final ImageIcon newRecordIcon	= new ImageIcon(ClassLoader.getSystemResource("new.gif"));
	private final ImageIcon openRecordIcon	= new ImageIcon(ClassLoader.getSystemResource("open.gif"));
	private final ImageIcon graphIcon		= new ImageIcon(ClassLoader.getSystemResource("graph.gif"));
	private final ImageIcon exportImageIcon	= new ImageIcon(ClassLoader.getSystemResource("savegraph.gif"));
	private final ImageIcon helpIcon		= new ImageIcon(ClassLoader.getSystemResource("help.gif"));

	private final URL helpPage = ClassLoader.getSystemResource("help.html");
	private final HyperlinkListener linkListener;

	private GraphInspector inspector;
	private processing.core.PApplet graphRenderer;

	private final String appName;
	private final int appVersion;


	public FTLFrame(String appName, int appVersion) {

		this.appName = appName;
		this.appVersion = appVersion;

		linkListener = new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					log.trace("Dialog link clicked: " + e.getURL());
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
							log.trace("Link opened in external browser.");
						} catch (URISyntaxException | IOException f) {
							log.error("Unable to open link.", f);
						}
					}
				}
			}
		};

		// graph window
		setupGraphFrame();

		// inspector window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle(String.format("%s %d - Inspector", appName, appVersion));
		setLayout(new BorderLayout());

		// help frame
		createhelpFrame();

		// inspector toolbar
		JToolBar toolbar = new JToolBar();
		setupToolbar(toolbar);
		add(toolbar, BorderLayout.NORTH);

		// inspector options
		setupInspector();

		pack();

	}


	private void setupToolbar(JToolBar toolbar) {

		toolbar.setMargin(new Insets(5, 5, 5, 5));
		toolbar.setFloatable(false);


		final JButton gameStateLoadBtn = new JButton("Load save game", loadGameIcon);
		final JToggleButton gameStateRecordBtn = new JToggleButton("Record", isRecordingIcon, false);
		final JButton recordingNewBtn = new JButton("New recording", newRecordIcon);
		final JButton recordingOpenBtn = new JButton("Open recording", openRecordIcon);
		final JToggleButton toggleGraphBtn = new JToggleButton("Graph", graphIcon, false);
		final JButton exportImageBtn = new JButton("Export image", exportImageIcon);
		final JButton helpBtn = new JButton(helpIcon);

		gameStateLoadBtn.setToolTipText("Choose FTL save game file (continue.sav)");
		gameStateRecordBtn.setToolTipText("Pause/Unpause continuous monitoring of the FTL save game file");
		recordingNewBtn.setToolTipText("Create new spreadsheet file for recording the game state's history");
		recordingOpenBtn.setToolTipText("Open existing spreadsheet file with recordings of the game state's history");
		toggleGraphBtn.setToolTipText("Show/Hide graph window");
		exportImageBtn.setToolTipText("Export high-res image of the current graph view");

		gameStateRecordBtn.setEnabled(false);
		recordingNewBtn.setEnabled(false);
		recordingOpenBtn.setEnabled(false);
		toggleGraphBtn.setEnabled(false);
		exportImageBtn.setEnabled(false);


		final JFileChooser fc = new JFileChooser();
		fc.setFileHidingEnabled(false);
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "FTL Saved Game (continue.sav)";
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().equalsIgnoreCase("continue.sav");
			}
		});

		if (FTLAdventureVisualiser.gameStateFile != null && FTLAdventureVisualiser.gameStateFile.exists()) {
			loadGameStateFile(FTLAdventureVisualiser.gameStateFile);
			recordingNewBtn.setEnabled(true);
			recordingOpenBtn.setEnabled(true);
			gameStateRecordBtn.doClick();
			exportImageBtn.setEnabled(true);
			fc.setSelectedFile(FTLAdventureVisualiser.gameStateFile);
		} else {
			fc.setCurrentDirectory(FTLUtilities.findUserDataDir());
		}
		fc.setMultiSelectionEnabled(false);


		gameStateLoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				gameStateRecordBtn.doClick();

				fc.setDialogTitle("Select continue.sav (saved game)");
				int chooserResponse = fc.showOpenDialog(null);
				boolean sillyMistake = false;

				if (chooserResponse == JFileChooser.APPROVE_OPTION) {
					if (
						"ae_prof.sav".equals(FTLAdventureVisualiser.gameStateFile.getName()) ||
						"prof.sav".equals(FTLAdventureVisualiser.gameStateFile.getName())
					) {
						int sillyResponse = JOptionPane.showConfirmDialog(
							FTLFrame.this,
							"Warning: What you are attempting makes no sense.\n" +
							"We are looking for a savegame here, and you're opening \"" +
							FTLAdventureVisualiser.gameStateFile.getName() + "\".\n\n" +
							"Are you sure you know what you're doing?",
							"Really!?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
						);
						if (sillyResponse != JOptionPane.YES_OPTION) sillyMistake = true;
					}
				}

				if (chooserResponse == JFileChooser.APPROVE_OPTION && !sillyMistake) {
					resetGameState();
					FTLAdventureVisualiser.gameStateFile = fc.getSelectedFile();
					prefs.put("ftlContinuePath", FTLAdventureVisualiser.gameStateFile.getAbsolutePath());
					loadGameStateFile(FTLAdventureVisualiser.gameStateFile);
					recordingNewBtn.setEnabled(true);
					recordingOpenBtn.setEnabled(true);
					gameStateRecordBtn.doClick();
					exportImageBtn.setEnabled(true);
				} else if (sillyMistake || lastGameState == null) {
					recordingNewBtn.setEnabled(false);
					recordingOpenBtn.setEnabled(false);
					gameStateRecordBtn.setEnabled(false);
					recordingNewBtn.setEnabled(false);
					recordingOpenBtn.setEnabled(false);
					toggleGraphBtn.setEnabled(false);
					exportImageBtn.setEnabled(false);
				}
			}
		});


		gameStateRecordBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton)e.getSource();
				gameStateRecordBtn.setSelected(abstractButton.getModel().isSelected());

				if (gameStateRecordBtn.isSelected()) {
					if (lastGameState != null) {
						TimerTask task = new FileWatcher(FTLAdventureVisualiser.gameStateFile) {
							protected void onChange(File file) {
								// here we code the action on a change
								log.info("\nFILE "+ file.getName() +" HAS CHANGED !");
								if (gameStateRecordBtn.isSelected()) loadGameStateFile(FTLAdventureVisualiser.gameStateFile);
							}
						};
						Timer timer = new Timer();
						timer.schedule( task , new Date(), 1000 );
					} else {
						gameStateRecordBtn.doClick();
					}
				}

			}
		});


		recordingNewBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(null);
				fc.setFileHidingEnabled(true);
				fc.setDialogTitle("Pick a location to store your recording");

				int chooserResponse = fc.showSaveDialog(null);

				if (chooserResponse == JFileChooser.APPROVE_OPTION) {
					FTLAdventureVisualiser.recordFilePath = (
						fc.getSelectedFile().getAbsolutePath() +
							" (FTLAV " + appVersion + ") " +
							getTimeStamp().replaceAll("[/:]", "") + ".csv"
					);

					log.info("Created new record at " + FTLAdventureVisualiser.recordFilePath);

					resetGameState();
					loadGameStateFile(FTLAdventureVisualiser.gameStateFile);

					gameStateRecordBtn.setEnabled(true);
					gameStateRecordBtn.doClick();

					toggleGraphBtn.setEnabled(true);
					toggleGraphBtn.setSelected(true);
					graphFrame.setVisible(true);

				}

			}
		});


		recordingOpenBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO file filter

				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(null);
				fc.setFileHidingEnabled(true);
				fc.setDialogTitle("Pick a location to store your recording");

				fc.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "FTLAV CSV file (*.csv)";
					}
					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.getName().endsWith(".csv");
					}
				});

				int chooserResponse = fc.showOpenDialog(null);

				File chosenCSVFile = fc.getSelectedFile();
				boolean sillyMistake = false;

				// TODO check if CSV is valid
				if (chooserResponse == JFileChooser.APPROVE_OPTION) {
					if (!new ParseCSV().isValidCSV(chosenCSVFile.getAbsolutePath())) {
						int sillyResponse = JOptionPane.showConfirmDialog(
							FTLFrame.this,
							"Warning: This doesn't seem to be a CSV FTLAV can read.\n" +
							"Opening  \""+ chosenCSVFile.getName() + "\" might cause unexpected results.\n",
							"Are you sure you want to continue?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
						);
						if (sillyResponse != JOptionPane.YES_OPTION) sillyMistake = true;
					}
				}

				if (chooserResponse == JFileChooser.APPROVE_OPTION && !sillyMistake) {
					FTLAdventureVisualiser.recordFilePath = fc.getSelectedFile().getAbsolutePath();

					resetGameState();
					loadGameStateFile(FTLAdventureVisualiser.gameStateFile);

					gameStateRecordBtn.setEnabled(true);
					gameStateRecordBtn.doClick();

					toggleGraphBtn.setEnabled(true);
					toggleGraphBtn.setSelected(true);
					graphFrame.setVisible(true);

				}

				log.info("Imported record from " + FTLAdventureVisualiser.recordFilePath);

			}
		});


		toggleGraphBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton)e.getSource();
				toggleGraphBtn.setSelected(abstractButton.getModel().isSelected());

				if (toggleGraphBtn.isSelected()) {
					if (lastGameState != null) {
						graphFrame.setVisible(true);
					} else {
						toggleGraphBtn.doClick();
					}
				} else {
					graphFrame.setVisible(false);
				}

			}
		});


		exportImageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser exportChooser = new JFileChooser();
				exportChooser.setCurrentDirectory(null);
				exportChooser.setFileHidingEnabled(true);
				exportChooser.setDialogTitle("Export as PNG image");

				int chooserResponse = exportChooser.showSaveDialog(null);

				// GraphRenderer.exportPath = exportChooser.getSelectedFile().getAbsolutePath();
				// GraphRenderer.captureImage = true;

				log.info("Export path " + exportChooser.getSelectedFile().getAbsolutePath());

			}
		});


		helpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpFrame.setVisible(true);
			}
		});


		graphFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				toggleGraphBtn.setSelected(false);
			}
		});


		// TODO change text to inform user about importing CSV
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				int exitWarning = JOptionPane.showConfirmDialog(
					null,
					"Do you want to save the current graph before quitting?\n" +
					"Quiting " + appName + " will result in losing all data from before the current beacon!",
					"Please consider before quitting",
					JOptionPane.YES_NO_CANCEL_OPTION
				);
				if (exitWarning == JOptionPane.YES_OPTION) exportImageBtn.doClick();
				if (exitWarning == JOptionPane.NO_OPTION) setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
		});


		toolbar.add(gameStateLoadBtn);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(recordingNewBtn);
		toolbar.add(recordingOpenBtn);
		toolbar.add(gameStateRecordBtn);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(toggleGraphBtn);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(exportImageBtn);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(helpBtn);

	}


	private void createhelpFrame() {

		helpFrame = new JFrame();

		helpFrame.setSize(480, 670);
		helpFrame.setResizable(true);
		helpFrame.setLocationRelativeTo(this);
		helpFrame.setTitle("Help & Background Information");
		helpFrame.setLayout(new BorderLayout());

		try {
			JEditorPane editor = new JEditorPane(helpPage);
			editor.setEditable(false);
			editor.addHyperlinkListener(linkListener);

			JScrollPane helpScrollPane = new JScrollPane(
				editor,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
			);
			helpScrollPane.getVerticalScrollBar().setUnitIncrement(14);
			helpScrollPane.getHorizontalScrollBar().setUnitIncrement(14);
			helpFrame.add(helpScrollPane, BorderLayout.CENTER);
		} catch (IOException e) {
			log.error(e);
		}

		helpFrame.setVisible(false);

	}


	private void setupGraphFrame() {

		graphFrame = new JFrame();

		graphFrame.setSize(1200, 700);
		graphFrame.setType(JFrame.Type.UTILITY);
		graphFrame.setResizable(true);
		graphFrame.setLocationRelativeTo(null);
		graphFrame.setTitle(String.format("%s %d - Graph Renderer", appName, appVersion));
		graphFrame.setLayout(new BorderLayout());

		graphRenderer = new GraphRenderer();
		graphFrame.add(graphRenderer);
		// GraphRenderer.panelWidth  = graphFrame.getWidth();
		// GraphRenderer.panelHeight = graphFrame.getHeight();
		graphRenderer.init();

		graphFrame.setVisible(false);

	}


	private void setupInspector() {

		inspector = new GraphInspector(this);
		JScrollPane inspectorScrollPane = new JScrollPane(
			inspector,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		inspectorScrollPane.getVerticalScrollBar().setUnitIncrement(14);
		add(inspectorScrollPane, BorderLayout.CENTER);

	}


	private Profile loadProfileFile(File file) {

		FileInputStream in = null;
		StringBuilder hexBuf = new StringBuilder();
		Profile p = null;

		try {

			log.info("Opening profile: " + file.getAbsolutePath());

			in = new FileInputStream(file);

			// Read the content in advance, in case an error ocurs.
			byte[] buf = new byte[4096];
			int len;
			while ((len = in.read(buf)) >= 0) {
				for (int j = 0; j < len; j++) {
					hexBuf.append(String.format("%02x", buf[j]));
					if ((j + 1) % 32 == 0) {
						hexBuf.append("\n");
					}
				}
			}
			in.getChannel().position(0);

			// Parse file data.
			ProfileParser parser = new ProfileParser();
			p = parser.readProfile(in);

		} catch (Exception f) {
			log.error(
				String.format(
					"Error reading profile (\"%s\").",
					file.getName()
				),
				f
			);
			showErrorDialog(
				String.format(
					"Error reading profile (\"%s\"):\n%s: %s",
					file.getName(),
					f.getClass().getSimpleName(),
					f.getMessage()
				)
			);
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException f) {}
		}
		return p;

	}


	private void loadGameStateFile(File file) {

		FileInputStream in = null;
		StringBuilder hexBuf = new StringBuilder();

		try {
			log.info("Opening game state: " + file.getAbsolutePath());

			in = new FileInputStream(file);

			// Read the content in advance, in case an error ocurs.
			byte[] buf = new byte[4096];
			int len = 0;
			while ((len = in.read(buf)) >= 0) {
				for (int j = 0; j < len; j++) {
					hexBuf.append(String.format("%02x", buf[j]));
					if ((j+1) % 32 == 0) hexBuf.append("\n");
				}
			}
			in.getChannel().position(0);

			SavedGameParser parser = new SavedGameParser();
			SavedGameParser.SavedGameState gs = parser.readSavedGame(in);
			if (FTLAdventureVisualiser.recordFilePath != null)  {
				loadGameState(gs);

				log.trace("Game state read successfully.");

				if (lastGameState.getMysteryList().size() > 0) {
					StringBuilder mysteryBuf = new StringBuilder();
					mysteryBuf.append("This saved game file contains mystery bytes the developers hadn't anticipated!\n");
					boolean first = true;
					for (MysteryBytes m : lastGameState.getMysteryList()) {
						if (first) {
							first = false;
						} else {
							mysteryBuf.append(",\n");
						}
						mysteryBuf.append(m.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
					}
					log.warn( mysteryBuf.toString() );
				}
			}

		} catch (Exception f) {
			log.error(String.format("Error reading saved game (\"%s\").", FTLAdventureVisualiser.gameStateFile.getName()), f);
			showErrorDialog(String.format(
				"Error reading saved game (\"%s\"):\n%s: %s\n" +
				"This error is probably caused by a game-over or the restarting of a game.\n" +
				"If not, please report this on the %s GitHub Issue page.\n" +
				"You can still save the graph by pressing the Export button.\n" +
				"Restart %s to reset everything.",
				FTLAdventureVisualiser.gameStateFile.getName(), f.getClass().getSimpleName(), f.getMessage(), appName, appName
			));
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException f) {}
		}

	}


	private void loadGameState(SavedGameParser.SavedGameState currentGameState) {

		log.info("");
		log.info("Ship Name : " + currentGameState.getPlayerShipName());
		log.info("Currently at beacon number : " + currentGameState.getTotalBeaconsExplored());
		log.info("Currently in sector : " + (currentGameState.getSectorNumber() + 1));

		if (
			FTLAdventureVisualiser.gameState == null ||
			currentGameState.getTotalBeaconsExplored() > lastGameState.getTotalBeaconsExplored()
		) {
			FTLAdventureVisualiser.gameState = currentGameState;
			FTLAdventureVisualiser.shipState = currentGameState.getPlayerShipState();
			FTLAdventureVisualiser.nearbyShipState = currentGameState.getNearbyShipState();
			FTLAdventureVisualiser.environmentState = currentGameState.getEnvironment();
			FTLAdventureVisualiser.fileChangedTimeStamp = getTimeStamp();

			ArrayList<SavedGameParser.CrewState> currentPlayerCrew = new ArrayList<>();
			ArrayList<SavedGameParser.CrewState> currentEnemyCrew = new ArrayList<>();
			for (int i = 0; i < currentGameState.getPlayerShipState().getCrewList().size(); i++) {
				if (currentGameState.getPlayerShipState().getCrewList().get(i).isPlayerControlled()) {
					currentPlayerCrew.add(currentGameState.getPlayerShipState().getCrewList().get(i));
				} else {
					currentEnemyCrew.add(currentGameState.getPlayerShipState().getCrewList().get(i));
				}
			}
			if (currentGameState.getNearbyShipState() != null) {
				for (int i = 0; i < currentGameState.getNearbyShipState().getCrewList().size(); i++) {
					if (currentGameState.getNearbyShipState().getCrewList().get(i).isPlayerControlled()) {
						currentPlayerCrew.add(currentGameState.getNearbyShipState().getCrewList().get(i));
					} else {
						currentEnemyCrew.add(currentGameState.getNearbyShipState().getCrewList().get(i));
					}
				}
			}
			FTLAdventureVisualiser.playerCrewState = currentPlayerCrew;
			FTLAdventureVisualiser.enemyCrewState = currentEnemyCrew;

			FTLAdventureVisualiser.sectorArray.clear();
			RandomSectorTreeGenerator myGen = new RandomSectorTreeGenerator(new NativeRandom());
			List<List<SectorDot>> myColumns = myGen.generateSectorTree(
				currentGameState.getSectorTreeSeed(),
				currentGameState.isDLCEnabled()
			);
			int columnsOffset = 0;
			for (List<SectorDot> myColumn : myColumns) {
				for (int k = 0; k < myColumn.size(); k++) {
					if (currentGameState.getSectorVisitation().subList(
						columnsOffset, columnsOffset + myColumn.size()
					).get(k)
						) {
						FTLAdventureVisualiser.sectorArray.add(myColumn.get(k));
					}
				}
				columnsOffset += myColumn.size();
			}

			if (currentGameState.getEncounter().getText().toLowerCase().contains("giant alien spiders")) {
				log.info("\n" +
					"     /^\\ ___ /^\\    \n"+
					"    //^\\(o o)/^\\\\  \n"+
					"   /'<^~``~''~^>`\\   \n"+
					" GIANT ALIEN SPIDERS  \n"+
					"     ARE NO JOKE!     \n"
				);
			}

			if (new File(FTLAdventureVisualiser.recordFilePath).exists()) {
				log.info("Reading existing data...");
				new ParseCSV().readCSV(FTLAdventureVisualiser.recordFilePath);
				log.info("Start adding new row to CSV...");
				new ParseCSV().writeCSV(FTLAdventureVisualiser.recordFilePath);
			} else {
				log.info("CSV doesn't exist yet. Creating new CSV...");
				new ParseCSV().writeCSV(FTLAdventureVisualiser.recordFilePath);
			}

			// TODO figure out what prof.sav and ae_prof.sav are used for
			FTLAdventureVisualiser.profile = loadProfileFile(FTLAdventureVisualiser.aeProfileFile);

			// TODO read/write/read first, then inspector.setGameState()
			// inspector.setGraphSettings();

			// graphRenderer.destroy();
			// graphRenderer.init();

		}

		lastGameState = currentGameState;

	}


	private void resetGameState() {
		FTLAdventureVisualiser.gameState = null;
		lastGameState = null;
	}


	private String getTimeStamp() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(FTLAdventureVisualiser.gameStateFile.lastModified());
		return (
			cal.get(Calendar.YEAR) + "/" +
			String.format("%02d", cal.get(Calendar.MONTH) + 1) + "/" +
			String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + " " +
			String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + ":" +
			String.format("%02d", cal.get(Calendar.MINUTE)) + ":" +
			String.format("%02d", cal.get(Calendar.SECOND))
		);
	}


	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}