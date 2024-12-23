package nl.nielspoldervaart.ftlav.ui;

import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.TableMaker;
import nl.nielspoldervaart.ftlav.data.TableReader;
import nl.nielspoldervaart.ftlav.data.FileWatcher;
import nl.nielspoldervaart.ftlav.visualiser.Visualiser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Slf4j
public class FTLFrame extends JFrame {

	private final JLabel loadedSaveGameLabel;
	private JToggleButton toggleGameStateRecordingButton;
	private JButton exportRecordingButton;
	private JButton importRecordingButton;
	private JToggleButton toggleGraphButton;
//	private JButton exportImageButton;

	private JFrame graphFrame;
	private JFrame helpFrame;

	private final Preferences prefs;

	private static final ImageIcon loadGameIcon	    = new ImageIcon(ClassLoader.getSystemResource("UI/loadgame.gif"));
	private static final ImageIcon isRecordingIcon	= new ImageIcon(ClassLoader.getSystemResource("UI/recording.gif"));
	private static final ImageIcon exportRecordingIcon = new ImageIcon(ClassLoader.getSystemResource("UI/export.gif"));
	private static final ImageIcon importRecordingIcon = new ImageIcon(ClassLoader.getSystemResource("UI/import.gif"));
	private static final ImageIcon graphIcon		= new ImageIcon(ClassLoader.getSystemResource("UI/graph.gif"));
//	private static final ImageIcon exportImageIcon	= new ImageIcon(ClassLoader.getSystemResource("UI/savegraph.gif"));
	private static final ImageIcon resetIcon        = new ImageIcon(ClassLoader.getSystemResource("UI/reset.gif"));
	private static final ImageIcon helpIcon		    = new ImageIcon(ClassLoader.getSystemResource("UI/help.gif"));

	private final URL helpPage = ClassLoader.getSystemResource("UI/help.html");
	private final HyperlinkListener linkListener;

	private Visualiser graphRenderer;
	private JScrollPane graphInspectorScrollPane;

	private final String appName;
	private final int appVersion;


	public FTLFrame(String appName, int appVersion, Preferences prefs) {

		this.appName = appName;
		this.appVersion = appVersion;
		this.prefs = prefs;

		linkListener = e -> {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				log.trace("Dialog link clicked: {}", e.getURL());
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
						log.trace("Link opened in external browser.");
					} catch (URISyntaxException | IOException f) {
						log.error("Unable to open link.", f);
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
		add(setupToolbar(), BorderLayout.NORTH);

		loadedSaveGameLabel = new JLabel(" ");
		loadedSaveGameLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		loadedSaveGameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(loadedSaveGameLabel, BorderLayout.SOUTH);

		// inspector options
		setupInspector();

		pack();

		toggleGameStateRecordingButton.setSelected(hasGameState());
		toggleGraphButton.setSelected(hasRecords());

		startGameStateWatcher();

		// Initial update UI to reflect current game state, if any.
		onGameStateUpdate();

	}

	private JToolBar setupToolbar() {

		JToolBar toolbar = new JToolBar();

		toolbar.setMargin(new Insets(5, 5, 5, 5));
		toolbar.setFloatable(false);

		JButton gameStateLoadBtn = new JButton("Load save game", loadGameIcon);
		toggleGameStateRecordingButton = new JToggleButton("Record", isRecordingIcon, false);
		exportRecordingButton = new JButton("Export recording", exportRecordingIcon);
		importRecordingButton = new JButton("Import recording", importRecordingIcon);
		toggleGraphButton = new JToggleButton("Graph", graphIcon, false);
//		exportImageButton = new JButton("Export image", exportImageIcon);
		JButton resetButton = new JButton(resetIcon);
		JButton helpButton = new JButton(helpIcon);

		gameStateLoadBtn.setToolTipText("Choose FTL save game file (continue.sav)");
		toggleGameStateRecordingButton.setToolTipText("Pause/Unpause continuous monitoring of the FTL save game file");
		exportRecordingButton.setToolTipText("Export recording of the game state's history to a TSV file");
		importRecordingButton.setToolTipText("Open existing TSV file with recordings of the game state's history");
		toggleGraphButton.setToolTipText("Show/Hide graph window");
		resetButton.setToolTipText("Reset all preferences back to default");
//		exportImageButton.setToolTipText("Export high-res image of the current graph view");

		toggleGameStateRecordingButton.setEnabled(false);
		exportRecordingButton.setEnabled(false);
		importRecordingButton.setEnabled(false);
		toggleGraphButton.setEnabled(false);
//		exportImageButton.setEnabled(false);

		gameStateLoadBtn.addActionListener(e -> {
			FTLAdventureVisualiser.loadGameState(true);
			onGameStateUpdate();
			if (hasGameState()) {
				toggleGraphButton.setSelected(true);
			}
		});

		exportRecordingButton.addActionListener(e -> {
			JFileChooser exportFileChooser = new JFileChooser();
			exportFileChooser.setCurrentDirectory(null);
			exportFileChooser.setFileHidingEnabled(true);
			exportFileChooser.setDialogTitle("Pick a location to store your recording");
			exportFileChooser.setSelectedFile(new File(
				FTLAdventureVisualiser.gameState.getPlayerShipName() + " FTLAV v" + appVersion + " " +
				FTLAdventureVisualiser.getTimeStamp().replaceAll("[/:]", "") + ".tsv"
			));

			int chooserResponse = exportFileChooser.showSaveDialog(this);
			if (chooserResponse == JFileChooser.APPROVE_OPTION) {
				File chosenExportFile = exportFileChooser.getSelectedFile();
				if (chosenExportFile != null) {
					try {
						log.info("Created TSV file at {}", chosenExportFile.getAbsolutePath());
						new TableMaker(chosenExportFile);
					} catch (IOException ex) {
						log.error("Unable to make table.", ex);
						showErrorDialog(String.format("Unable to make table: %s", ex.getMessage()));
					}
				}
			}
		});

		importRecordingButton.addActionListener(e -> {
			JFileChooser importFileChooser = new JFileChooser();
			importFileChooser.setCurrentDirectory(null);
			importFileChooser.setFileHidingEnabled(true);
			importFileChooser.setDialogTitle("Pick TSV file to import");
			importFileChooser.addChoosableFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isFile() && f.getName().toLowerCase().endsWith(".tsv");
				}

				@Override
				public String getDescription() {
					return "TSV files (*.tsv)";
				}
			});

			int chooserResponse = importFileChooser.showOpenDialog(this);
			if (chooserResponse == JFileChooser.APPROVE_OPTION) {
				File chosenImportFile = importFileChooser.getSelectedFile();
				if (chosenImportFile != null && chosenImportFile.exists()) {
					boolean canImport = true;
					if (hasRecords()) {
						int dialogChoice = JOptionPane.showConfirmDialog(
							this,
							"Importing a recording will overwrite the existing recording. " +
								"Are you sure you want to continue?",
							"Before you import",
							JOptionPane.YES_NO_OPTION
						);
						if (dialogChoice == JOptionPane.NO_OPTION) {
							canImport = false;
						}
					}
					if (canImport) {
						try {
							log.info("Reading TSV file at {}", chosenImportFile.getAbsolutePath());
							TableReader.read(chosenImportFile);
							onGameStateUpdate();
						} catch (IOException ex) {
							log.error("Unable to read TSV file at {}", chosenImportFile.getAbsolutePath(), ex);
							showErrorDialog(String.format("Unable to read TSV file: %s", ex.getMessage()));
						}
					}
				}
			}
		});

		toggleGraphButton.addItemListener(e -> {
			boolean hasGameStateAndGraphIsOpen = hasGameState() && e.getStateChange() == ItemEvent.SELECTED;
			graphFrame.setVisible(hasGameStateAndGraphIsOpen);
			graphInspectorScrollPane.setVisible(hasGameStateAndGraphIsOpen);
			pack();
		});
		graphFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				toggleGraphButton.setSelected(false);
			}
		});

		// TODO implement export graph image

		resetButton.addActionListener(e -> {
			int dialogChoice = JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to reset all preferences? This operation will quit " + appName + ".",
				"Reset all preferences",
				JOptionPane.YES_NO_OPTION
			);
			if (dialogChoice == JOptionPane.YES_OPTION) {
				log.info("All preferences will be ereased.");
				try {
					prefs.clear();
					log.info("All preferences have been cleared. Exiting...");
					System.exit(0);
				} catch (BackingStoreException bse) {
					log.trace("Something went wrong while attempting to reset all preferences", bse);
				}
			}
		});

		helpButton.addActionListener(e -> helpFrame.setVisible(true));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				int exitWarning = JOptionPane.showConfirmDialog(
					null,
					"Are you sure you want to quit FTLAV?\n" +
						"All collected data will be lost. Please use " +
						"the \"Export recording\" to save the recorded data",
					"Confirm before quiting FTLAV…",
					JOptionPane.YES_NO_OPTION
				);
				if (exitWarning == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		toolbar.add(gameStateLoadBtn);
		toolbar.add(toggleGameStateRecordingButton);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(exportRecordingButton);
		toolbar.add(importRecordingButton);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(toggleGraphButton);
//		toolbar.add(exportImageButton);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(resetButton);
		toolbar.add(helpButton);

		return toolbar;
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
			log.error(e.toString());
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

		graphRenderer = new Visualiser();
		graphRenderer.init();

		graphFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				graphRenderer.redraw();
			}
		});

		graphFrame.add(graphRenderer);

		graphFrame.setVisible(false);

	}

	private void setupInspector() {
		graphInspectorScrollPane = new JScrollPane(
			new GraphInspector(this),
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		graphInspectorScrollPane.getVerticalScrollBar().setUnitIncrement(14);
		graphInspectorScrollPane.setPreferredSize(new Dimension(getWidth(), 800));
		add(graphInspectorScrollPane, BorderLayout.CENTER);
	}

	private void startGameStateWatcher() {
		FileWatcher fileWatcher = new FileWatcher(
			FTLAdventureVisualiser.gameStateFile,
			(File file) -> {
				if (toggleGameStateRecordingButton.isSelected()) {
					log.info("File {} has updated", file.getName());
					FTLAdventureVisualiser.loadGameState(file);
				}
				onGameStateUpdate();
			}
		);
		fileWatcher.watch();
	}

	private void onGameStateUpdate() {
		updateToolbarButtonStates();
		graphRenderer.redraw();
	}

	public void redrawVisualiser() {
		graphRenderer.redraw();
	}

	private void updateToolbarButtonStates() {
		boolean hasGameState = hasGameState();

		loadedSaveGameLabel.setText(hasGameState ?
			String.format("Loaded %s (%s)", FTLAdventureVisualiser.gameStateFile.getName(), shortTimeStamp()) :
			"No save game loaded"
		);

		exportRecordingButton.setEnabled(hasGameState);
		importRecordingButton.setEnabled(hasGameState);
		toggleGameStateRecordingButton.setEnabled(hasGameState);
		if (!hasGameState) {
			toggleGameStateRecordingButton.setSelected(false);
		}

		boolean hasRecords = hasRecords();
		toggleGraphButton.setEnabled(hasRecords);
//		exportImageButton.setEnabled(hasRecords);
		if (!hasRecords) {
			toggleGraphButton.setSelected(false);
		}
	}

	private static String shortTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(FTLAdventureVisualiser.gameStateFile.lastModified());
	}

	private static boolean hasGameState() {
		return FTLAdventureVisualiser.gameState != null;
	}

	private static boolean hasRecords() {
		return !FTLAdventureVisualiser.recording.isEmpty();
	}

	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
