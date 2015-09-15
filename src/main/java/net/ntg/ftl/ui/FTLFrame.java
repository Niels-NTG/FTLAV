package net.ntg.ftl.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.ui.graph.GraphInspector;
import net.ntg.ftl.ui.graph.GraphRenderer;
import net.ntg.ftl.util.FileWatcher;
import net.ntg.ftl.parser.CreateCSV;

import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.MysteryBytes;
import net.blerf.ftl.parser.random.NativeRandom;
import net.blerf.ftl.parser.SavedGameParser; // TODO remove write methods from SavedGameParser
import net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator;
import net.vhati.modmanager.core.FTLUtilities;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class FTLFrame extends JFrame {

	private static Logger log = LogManager.getLogger(FTLFrame.class);

	JFrame graphFrame;
	JFrame helpFrame;

	private File chosenFile;
	private SavedGameParser.SavedGameState lastGameState = null;

	private ImageIcon openIcon  = new ImageIcon(ClassLoader.getSystemResource("open.gif"));
	private ImageIcon watchIcon = new ImageIcon(ClassLoader.getSystemResource("watch.gif"));
	private ImageIcon graphIcon = new ImageIcon(ClassLoader.getSystemResource("graph.gif"));
	private ImageIcon exportImageIcon= new ImageIcon(ClassLoader.getSystemResource("save.gif"));
	private ImageIcon helpIcon  = new ImageIcon(ClassLoader.getSystemResource("help.gif"));

	private URL helpPage = ClassLoader.getSystemResource("help.html");
	private HyperlinkListener linkListener;

	private JButton gameStateSaveBtn;
	private GraphInspector inspector;

	private String appName;
	private int appVersion;

	public FTLFrame (String appName, int appVersion) {

		this.appName = appName;
		this.appVersion = appVersion;

		linkListener = new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate( HyperlinkEvent e ) {
				if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
					log.trace( "Dialog link clicked: "+ e.getURL() );

					if ( Desktop.isDesktopSupported() ) {
						try {
							Desktop.getDesktop().browse( e.getURL().toURI() );
							log.trace( "Link opened in external browser." );
						}
						catch ( Exception f ) {
							log.error( "Unable to open link.", f );
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


	private void setupToolbar (JToolBar toolbar) {

		log.trace("Initialising toolbar.");

		toolbar.setMargin(new Insets(5, 5, 5, 5));
		toolbar.setFloatable(false);


		JButton gameStateOpenBtn = new JButton("Open", openIcon);
		JToggleButton gameStateWatcherBtn = new JToggleButton("Monitor save game", watchIcon, false);
		JToggleButton toggleGraphBtn = new JToggleButton("Graph", graphIcon, false);
		// TODO JButton "Refresh" to redraw graph
		JButton exportImageBtn = new JButton("Export image", exportImageIcon);
		JButton exportDataBtn = new JButton("Export data"); // TODO export data icon
		JButton helpBtn = new JButton("Help", helpIcon);


		gameStateWatcherBtn.setEnabled(false);
		toggleGraphBtn.setEnabled(false);
		exportImageBtn.setEnabled(false);
		exportDataBtn.setEnabled(false);


		// TODO get continue.sav automaticly if it exist on the expected location
		JFileChooser fc = new JFileChooser();
		fc.setFileHidingEnabled( false );
		fc.addChoosableFileFilter( new FileFilter() {
			@Override
			public String getDescription() {
				return "FTL Saved Game (continue.sav)";
			}
			@Override
			public boolean accept(File f) {
			return f.isDirectory() || f.getName().equalsIgnoreCase("continue.sav");
			}
		});

		File candidateSaveFile = new File( FTLUtilities.findUserDataDir(), "continue.sav" );
		if ( candidateSaveFile.exists() ) {
			fc.setSelectedFile( candidateSaveFile );
		} else {
			fc.setCurrentDirectory( FTLUtilities.findUserDataDir() );
		}
		fc.setMultiSelectionEnabled(false);


		gameStateOpenBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				log.trace( "Open saved game button clicked." );

				gameStateWatcherBtn.doClick();

				fc.setDialogTitle( "Select continue.sav (saved game)" );
				int chooserResponse = fc.showOpenDialog(null);
				chosenFile = fc.getSelectedFile();
				boolean sillyMistake = false;

				if ( chooserResponse == JFileChooser.APPROVE_OPTION ) {
					if ("ae_prof.sav".equals(chosenFile.getName()) ||
						"prof.sav".equals(chosenFile.getName())
					) {
						int sillyResponse = JOptionPane.showConfirmDialog(
							FTLFrame.this,
							"Warning: What you are attempting makes no sense.\n"+
							"This is the SavedGame tab, and you're opening \""+
							chosenFile.getName()+
							"\".\n\n Are you sure you know what you're doing?",
							"Really!?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE
						);
						if ( sillyResponse != JOptionPane.YES_OPTION ) sillyMistake = true;
					}
				}

				if ( chooserResponse == JFileChooser.APPROVE_OPTION && !sillyMistake ) {
					loadGameStateFile( chosenFile );
					gameStateWatcherBtn.setEnabled(true);
					toggleGraphBtn.setEnabled(true);
					gameStateWatcherBtn.doClick();
					toggleGraphBtn.setSelected(true);
					graphFrame.setVisible(true);
					exportImageBtn.setEnabled(true);
					exportDataBtn.setEnabled(true);

					// TODO write into configFile from here to remember chosenFile for later sessions
					// config.setProperty( "ftlContinuePath", chosenFile.getAbsolutePath() );

				} else if (sillyMistake || lastGameState == null) {
					gameStateWatcherBtn.setEnabled(false);
					toggleGraphBtn.setEnabled(false);
					exportImageBtn.setEnabled(false);
					exportDataBtn.setEnabled(false);
				} else {
					log.trace( "Open dialog cancelled." );
				}
			}
		});


		gameStateWatcherBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				gameStateWatcherBtn.setSelected(abstractButton.getModel().isSelected());

				if ( gameStateWatcherBtn.isSelected() ) {
					if ( lastGameState != null ) {
						// start monitor function
						log.info("Start monitoring save file...");

						TimerTask task = new FileWatcher( chosenFile ) {
							protected void onChange( File file ) {
								// here we code the action on a change
								log.info( "\nFILE "+ file.getName() +" HAS CHANGED !" );
								if (gameStateWatcherBtn.isSelected()) {
									loadGameStateFile ( chosenFile );
								}
							}
						};
						Timer timer = new Timer();
						timer.schedule( task , new Date(), 2000 );

					} else {
						gameStateWatcherBtn.doClick();
					}
				}
			}
		});


		toggleGraphBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				toggleGraphBtn.setSelected(abstractButton.getModel().isSelected());

				if ( toggleGraphBtn.isSelected() ) {
					if ( lastGameState != null ) {
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
			public void actionPerformed( ActionEvent e ) {

				JFileChooser exportChooser = new JFileChooser();
				exportChooser.setCurrentDirectory(null);
				exportChooser.setFileHidingEnabled(true);
				exportChooser.setDialogTitle("Export as PNG image");

				int chooserResponse = exportChooser.showSaveDialog(null);

				GraphRenderer.exportPath = exportChooser.getSelectedFile().getAbsolutePath();
				GraphRenderer.captureImage = true;

				log.info("Export path " + exportChooser.getSelectedFile().getAbsolutePath());

			}
		});


		exportDataBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {

				JFileChooser exportChooser = new JFileChooser();
				exportChooser.setCurrentDirectory(null);
				exportChooser.setFileHidingEnabled(true);
				exportChooser.setDialogTitle("Export raw data as CSV file");

				int chooserResponse = exportChooser.showSaveDialog(null);

				CreateCSV.writeCSV(exportChooser.getSelectedFile().getAbsolutePath());

				log.info("Export path " + exportChooser.getSelectedFile().getAbsolutePath());

			}
		});


		helpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				helpFrame.setVisible(true);
			}
		});


		graphFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				toggleGraphBtn.setSelected(false);
			}
		});


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
				if (exitWarning == JOptionPane.YES_OPTION) {
					exportImageBtn.doClick();
				}
				if (exitWarning == JOptionPane.NO_OPTION) {
					setDefaultCloseOperation(EXIT_ON_CLOSE);
				}
			}
		});


		toolbar.add( gameStateOpenBtn );
		toolbar.add( gameStateWatcherBtn );
		toolbar.add( toggleGraphBtn );
		toolbar.add( Box.createHorizontalGlue() );
		toolbar.add( exportImageBtn );
		toolbar.add( exportDataBtn );
		toolbar.add( Box.createHorizontalGlue() );
		toolbar.add( helpBtn );

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
		// graphFrame.setType(JFrame.Type.UTILITY);
		graphFrame.setResizable(true);
		graphFrame.setLocationRelativeTo(null);
		graphFrame.setTitle(String.format("%s %d - Graph Renderer", appName, appVersion));
		graphFrame.setLayout(new BorderLayout());

		processing.core.PApplet graphRenderer = new GraphRenderer();
		graphFrame.add(graphRenderer);
		GraphRenderer.panelWidth = graphFrame.getWidth();
		GraphRenderer.panelHeight= graphFrame.getHeight();
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


	public void loadGameStateFile(File file) {
		FileInputStream in = null;
		StringBuilder hexBuf = new StringBuilder();
		Exception exception = null;

		try {
			log.info( "Opening game state: "+ file.getAbsolutePath() );

			in = new FileInputStream( file );

			// Read the content in advance, in case an error ocurs.
			byte[] buf = new byte[4096];
			int len = 0;
			while ( (len = in.read(buf)) >= 0 ) {
				for (int j=0; j < len; j++) {
					hexBuf.append( String.format( "%02x", buf[j] ) );
					if ( (j+1) % 32 == 0 ) {
						hexBuf.append( "\n" );
					}
				}
			}
			in.getChannel().position( 0 );

			SavedGameParser parser = new SavedGameParser();
			SavedGameParser.SavedGameState gs = parser.readSavedGame( in );
			loadGameState( gs );

			log.trace( "Game state read successfully." );

			if ( lastGameState.getMysteryList().size() > 0 ) {
				StringBuilder musteryBuf = new StringBuilder();
				musteryBuf.append("This saved game file contains mystery bytes the developers hadn't anticipated!\n");
				boolean first = true;
				for (MysteryBytes m : lastGameState.getMysteryList()) {
					if (first) { first = false; }
					else { musteryBuf.append( ",\n" ); }
					musteryBuf.append(m.toString().replaceAll( "(^|\n)(.+)", "$1  $2") );
				}
				log.warn( musteryBuf.toString() );
			}
		} catch ( Exception f ) {
			log.error( String.format("Error reading saved game (\"%s\").", chosenFile.getName()), f );
			showErrorDialog( String.format(
				"Error reading saved game (\"%s\"):\n%s: %s\n" +
				"This error is probably caused by a game-over or the restarting of a game.\n" +
				"If not, please report this on the %s GitHub Issue page.\n" +
				"You can still save the graph by pressing the Export button.\n" +
				"Restart %s to reset everything.",
				chosenFile.getName(), f.getClass().getSimpleName(), f.getMessage(), appName, appName )
			);

			exception = f;
		} finally {
			try { if ( in != null ) in.close(); }
			catch ( IOException f ) {}
		}
	}


	public void loadGameState (SavedGameParser.SavedGameState currentGameState) {

		log.info( "------" );
		log.info( "Ship Name : " + currentGameState.getPlayerShipName() );
		log.info( "Currently at beacon number : " + currentGameState.getTotalBeaconsExplored() );
		log.info( "Currently in sector : " + currentGameState.getSectorNumber() + 1 );

		if (FTLAdventureVisualiser.gameStateArray.isEmpty() ||
			currentGameState.getTotalBeaconsExplored() > lastGameState.getTotalBeaconsExplored()
		) {
			FTLAdventureVisualiser.gameStateArray.add(currentGameState);
			FTLAdventureVisualiser.shipStateArray.add(currentGameState.getPlayerShipState());
			FTLAdventureVisualiser.nearbyShipStateArray.add(currentGameState.getNearbyShipState());
			FTLAdventureVisualiser.environmentArray.add(currentGameState.getEnvironment());

			ArrayList<SavedGameParser.CrewState> currentPlayerCrew = new ArrayList<SavedGameParser.CrewState>();
			for (int i = 0; i < currentGameState.getPlayerShipState().getCrewList().size(); i++) {
				if (currentGameState.getPlayerShipState().getCrewList().get(i).isPlayerControlled()) {
					currentPlayerCrew.add(currentGameState.getPlayerShipState().getCrewList().get(i));
				}
			}
			if (currentGameState.getNearbyShipState() != null) {
				for (int i = 0; i < currentGameState.getNearbyShipState().getCrewList().size(); i++) {
					if (currentGameState.getNearbyShipState().getCrewList().get(i).isPlayerControlled()) {
						currentPlayerCrew.add(currentGameState.getNearbyShipState().getCrewList().get(i));
					}
				}
			}
			FTLAdventureVisualiser.playerCrewArray.add(currentPlayerCrew);

			FTLAdventureVisualiser.sectorArray.clear();
			RandomSectorTreeGenerator myGen = new RandomSectorTreeGenerator( new NativeRandom() );
			List<List<SectorDot>> myColumns = myGen.generateSectorTree(
				currentGameState.getSectorTreeSeed(),
				currentGameState.isDLCEnabled()
			);
			int columnsOffset = 0;
			for (int i = 0; i < myColumns.size(); i++) {
				for (int k = 0; k < myColumns.get(i).size(); k++) {
					if (currentGameState.getSectorVisitation().subList(
							columnsOffset, columnsOffset + myColumns.get(i).size()
						).get(k)
					) {
						FTLAdventureVisualiser.sectorArray.add( myColumns.get(i).get(k) );
					}
				}
				columnsOffset += myColumns.get(i).size();
			}

			if (currentGameState.getEncounter().getText().toLowerCase().contains("giant alien spiders!")) {
				log.info("\n" +
					"     /^\\ ___ /^\\    \n"+
					"    //^\\(o o)/^\\\\  \n"+
					"   /'<^~``~''~^>`\\   \n"+
					" GIANT ALIEN SPIDERS  \n"+
					"     ARE NO JOKE!     \n"
				);
			}

			inspector.setGameState();
		}

		lastGameState = currentGameState;
	}


	private void showErrorDialog (String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}