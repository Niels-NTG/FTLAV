package nl.nielspoldervaart.ftlav.ui;

import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.DataUtil;
import nl.nielspoldervaart.ftlav.data.FileWatcher;
import nl.nielspoldervaart.ftlav.data.TableReader;
import nl.nielspoldervaart.ftlav.visualiser.Visualiser;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Slf4j
public class FTLFrame extends JFrame {

	private final JLabel loadedSaveGameLabel;
	public JToggleButton toggleGraphButton;
	private JButton exportImageButton;

	private JFrame graphFrame;

	private final Preferences prefs;

	private static final ImageIcon loadGameIcon	    = new ImageIcon(ClassLoader.getSystemResource("UI/loadgame.gif"));
	private static final ImageIcon newRecordingIcon = new ImageIcon(ClassLoader.getSystemResource("UI/new.gif"));
	private static final ImageIcon openRecordingIcon = new ImageIcon(ClassLoader.getSystemResource("UI/open.gif"));
	private static final ImageIcon graphIcon		= new ImageIcon(ClassLoader.getSystemResource("UI/graph.gif"));
	private static final ImageIcon exportImageIcon	= new ImageIcon(ClassLoader.getSystemResource("UI/savegraph.gif"));
	private static final ImageIcon resetIcon        = new ImageIcon(ClassLoader.getSystemResource("UI/reset.gif"));
	private static final ImageIcon helpIcon		    = new ImageIcon(ClassLoader.getSystemResource("UI/help.gif"));

	private Visualiser graphRenderer;
	private JScrollPane graphInspectorScrollPane;

	private final String appName;
	private final int appVersion;


	public FTLFrame(String appName, int appVersion, Preferences prefs) {

		this.appName = appName;
		this.appVersion = appVersion;
		this.prefs = prefs;

		// graph window
		setupGraphFrame();

		// inspector window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle(String.format("%s %d - Inspector", appName, appVersion));
		setLayout(new BorderLayout());

		// inspector toolbar
		add(setupToolbar(), BorderLayout.NORTH);

		loadedSaveGameLabel = new JLabel(" ");
		loadedSaveGameLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		loadedSaveGameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(loadedSaveGameLabel, BorderLayout.SOUTH);

		// inspector options
		setupInspector();

		pack();

		toggleGraphButton.setSelected(FTLAdventureVisualiser.hasRecords());

		startGameStateWatcher();

		// Initial update UI to reflect current game state, if any.
		onGameStateUpdate();

	}

	private JToolBar setupToolbar() {

		JToolBar toolbar = new JToolBar();

		toolbar.setMargin(new Insets(5, 5, 5, 5));
		toolbar.setFloatable(false);

		JButton gameStateLoadBtn = new JButton("Load save game", loadGameIcon);
		JButton newRecordingButton = new JButton("New recording", newRecordingIcon);
		JButton openRecordingButton = new JButton("Open recording", openRecordingIcon);
		toggleGraphButton = new JToggleButton("Graph", graphIcon, false);
		exportImageButton = new JButton("Export graph", exportImageIcon);
		JButton resetButton = new JButton(resetIcon);
		JButton helpButton = new JButton(helpIcon);

		gameStateLoadBtn.setToolTipText("Choose FTL save game file (continue.sav)");
		newRecordingButton.setToolTipText("Start recording game state's history to a TSV file");
		openRecordingButton.setToolTipText("Open existing TSV file with recordings of the game state's history");
		toggleGraphButton.setToolTipText("Show/Hide graph window");
		exportImageButton.setToolTipText("Export full-resolution image of current graph view");
		resetButton.setToolTipText("Reset all preferences back to default");
		helpButton.setToolTipText("Open FTLAV documentation");

		toggleGraphButton.setEnabled(false);
		exportImageButton.setEnabled(false);

		gameStateLoadBtn.addActionListener(e -> {
			FTLAdventureVisualiser.loadGameState(true);
			startGameStateWatcher();
			onGameStateUpdate();
		});

		newRecordingButton.addActionListener(e -> {
			JFileChooser exportFileChooser = new JFileChooser();
			exportFileChooser.setCurrentDirectory(null);
			exportFileChooser.setFileHidingEnabled(true);
			exportFileChooser.setDialogTitle("Pick a location to store your recording");
			exportFileChooser.setSelectedFile(new File(
				String.format(
					"%s FTLAV v%d %s.tsv",
					DataUtil.getPlayerShipName(),
					appVersion,
					DataUtil.getFileNameSafeTimeStampLastRecord()
				)
			));

			int chooserResponse = exportFileChooser.showSaveDialog(this);
			if (chooserResponse == JFileChooser.APPROVE_OPTION) {
				File chosenExportFile = exportFileChooser.getSelectedFile();
				if (chosenExportFile != null) {
					FTLAdventureVisualiser.recordsExportFile = exportFileChooser.getSelectedFile();
					try {
						FTLAdventureVisualiser.makeGameStateTable();
					} catch (IOException ex) {
						log.error("Unable to make table.", ex);
						showErrorDialog(String.format("Unable to make table: %s", ex.getMessage()));
					}
				}
			}
		});

		openRecordingButton.addActionListener(e -> {
			JFileChooser importFileChooser = new JFileChooser();
			importFileChooser.setCurrentDirectory(null);
			importFileChooser.setFileHidingEnabled(true);
			importFileChooser.setDialogTitle("Pick TSV file to import");
			importFileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.isFile() && f.getName().toLowerCase().endsWith(".tsv");
				}

				@Override
				public String getDescription() {
					return "TSV files (*.tsv)";
				}
			});

			int chooserResponse = importFileChooser.showOpenDialog(this);
			if (chooserResponse == JFileChooser.APPROVE_OPTION) {
				File chosenImportFile = importFileChooser.getSelectedFile();
				if (chosenImportFile != null && chosenImportFile.exists() && chosenImportFile.isFile()) {
					TableReader.read(chosenImportFile, this);
				}
			}
		});

		toggleGraphButton.addItemListener(e -> {
			boolean hasRecordsAndIsGraphInputActive =
				FTLAdventureVisualiser.hasRecords() && e.getStateChange() == ItemEvent.SELECTED;
			graphFrame.setVisible(hasRecordsAndIsGraphInputActive);
			graphInspectorScrollPane.setVisible(hasRecordsAndIsGraphInputActive);
			exportImageButton.setEnabled(hasRecordsAndIsGraphInputActive);
			pack();
		});
		graphFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				toggleGraphButton.setSelected(false);
			}
		});

		exportImageButton.addActionListener(e -> {
			JFileChooser exportGraphFileChooser = new JFileChooser();
			exportGraphFileChooser.setCurrentDirectory(null);
			exportGraphFileChooser.setFileHidingEnabled(true);
			exportGraphFileChooser.setDialogTitle("Pick a location to store your visualiser image");
			exportGraphFileChooser.setSelectedFile(new File(
				String.format(
					"%s FTLAV v%d %s.png",
					DataUtil.getPlayerShipName(),
					appVersion,
					DataUtil.getFileNameSafeTimeStampLastRecord()
				)
			));

			int chooserResponse = exportGraphFileChooser.showSaveDialog(this);
			if (chooserResponse == JFileChooser.APPROVE_OPTION) {
				File chosenExportFile = exportGraphFileChooser.getSelectedFile();
				if (chosenExportFile != null) {
					log.info("Created PNG file at {}", chosenExportFile.getAbsolutePath());
					graphRenderer.exportGraph(chosenExportFile.getAbsolutePath());
				}
			}
		});

		resetButton.addActionListener(e -> {
			int dialogChoice = JOptionPane.showConfirmDialog(
				this,
				String.format("Are you sure you want to reset all preferences? This operation will quit %s.", appName),
				"Reset all preferences",
				JOptionPane.YES_NO_OPTION
			);
			if (dialogChoice == JOptionPane.YES_OPTION) {
				log.info("All preferences will be ereased.");
				try {
					prefs.clear();
					log.info("All preferences have been cleared. Exiting…");
					System.exit(0);
				} catch (BackingStoreException bse) {
					log.trace("Something went wrong while attempting to reset all preferences", bse);
				}
			}
		});

		helpButton.addActionListener(e -> {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/Niels-NTG/FTLAV"));
				} catch (IOException | URISyntaxException ex) {
					log.error("Unable to open URL", ex);
				}
			} else {
				log.error("Unable to open URL: Desktop API not supported");
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				boolean hasUnsavedRecording =
					FTLAdventureVisualiser.hasRecords() && (
						FTLAdventureVisualiser.recordsExportFile == null || !FTLAdventureVisualiser.recordsExportFile.exists()
				);
				int exitWarningResponse = JOptionPane.showConfirmDialog(
					null,
					String.format("Are you sure you want to quit FTLAV?%s", hasUnsavedRecording ? " Your recording hasn't been saved to a file yet!" : ""),
					"Confirm before quiting FTLAV…",
					JOptionPane.YES_NO_OPTION
				);
				if (exitWarningResponse == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		toolbar.add(gameStateLoadBtn);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(newRecordingButton);
		toolbar.add(openRecordingButton);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(toggleGraphButton);
		toolbar.add(exportImageButton);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(resetButton);
		toolbar.add(helpButton);

		return toolbar;
	}

	private void setupGraphFrame() {

		graphFrame = new JFrame();

		graphFrame.setSize(1200, 700);
		graphFrame.setType(JFrame.Type.UTILITY);
		graphFrame.setResizable(true);
		graphFrame.setMinimumSize(new Dimension(1000, 660));
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
		GraphInspector graphInspector = new GraphInspector(this);
		graphInspectorScrollPane = new JScrollPane(
			graphInspector,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		graphInspectorScrollPane.getVerticalScrollBar().setUnitIncrement(14);
		graphInspectorScrollPane.setVisible(false);
		add(graphInspectorScrollPane, BorderLayout.CENTER);
	}

	private void startGameStateWatcher() {
        try {
			if (FTLAdventureVisualiser.gameStateFilewatcher != null) {
				if (FTLAdventureVisualiser.gameStateFilewatcher.targetFile == FTLAdventureVisualiser.gameStateFile) {
					return;
				} else {
					FTLAdventureVisualiser.gameStateFilewatcher.unwatch();
				}
			}
			FTLAdventureVisualiser.gameStateFilewatcher = new FileWatcher(
				FTLAdventureVisualiser.gameStateFile,
				(File file) -> {
					if (FTLAdventureVisualiser.hasGameState()) {
						log.info(
							"{} File {} has updated",
							DataUtil.getTimeStamp(new Date(file.lastModified())),
							file.getName()
						);
						FTLAdventureVisualiser.loadGameState(file);
					}
					onGameStateUpdate();
				}
			);
			FTLAdventureVisualiser.gameStateFilewatcher.watch();
        } catch (FileNotFoundException e) {
            log.error("No save game file was selected");
        }
	}

	public void onGameStateUpdate() {
		updateToolbarButtonStates();
		redrawVisualiser();
	}

	public void redrawVisualiser() {
		graphRenderer.redraw();
	}

	private void updateToolbarButtonStates() {
		boolean hasGameState = FTLAdventureVisualiser.hasGameState();

		updateStatusBar(hasGameState ?
			String.format(
				"%s save game file was updated",
				FTLAdventureVisualiser.getTimeStamp()
			) :
			"No save game file loaded"
		);

		boolean hasRecords = FTLAdventureVisualiser.hasRecords();
		toggleGraphButton.setEnabled(hasRecords);
		exportImageButton.setEnabled(hasRecords);
		if (!hasRecords) {
			toggleGraphButton.setSelected(false);
		}
	}

	public void updateStatusBar(String text) {
		loadedSaveGameLabel.setText(text);
	}

	public void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
