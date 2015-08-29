package net.ntg.ftl.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.filechooser.FileFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.ui.graph.GraphInspector;
import net.ntg.ftl.ui.graph.GraphRenderer;
import net.ntg.ftl.util.FileWatcher;

import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.MysteryBytes;
import net.blerf.ftl.parser.random.NativeRandom;
import net.blerf.ftl.parser.SavedGameParser; // TODO remove write methods from SavedGameParser
import net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator;
import net.vhati.modmanager.core.FTLUtilities;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class FTLFrame extends JFrame {

	private static final Logger log = LogManager.getLogger(FTLFrame.class);

	JFrame graphFrame;
	// TODO JFrame helpFrame;

	private File chosenFile;
	private SavedGameParser.SavedGameState lastGameState = null;

	// TODO new FTL style icons for the open and export button
	private ImageIcon openIcon  = new ImageIcon(ClassLoader.getSystemResource("open.gif"));
	private ImageIcon watchIcon = new ImageIcon(ClassLoader.getSystemResource("watch.gif"));
	private ImageIcon graphIcon = new ImageIcon(ClassLoader.getSystemResource("graph.gif"));
	private ImageIcon exportIcon= new ImageIcon(ClassLoader.getSystemResource("save.gif"));

	private JButton gameStateSaveBtn;
	private GraphInspector inspector;

	private String appName;
	private int appVersion;

	public FTLFrame (String appName, int appVersion) {
		this.appName = appName;
		this.appVersion = appVersion;

		// graph window
		setupGraphFrame();

		// inspector window
		setDefaultCloseOperation(EXIT_ON_CLOSE); // TODO show warning dialog before exit
		setResizable(false);
		setTitle(String.format("%s %d.0 - Inspector", appName, appVersion));
		setLayout(new BorderLayout());

		// inspector toolbar
		JToolBar toolbar = new JToolBar();
		setupToolbar(toolbar);
		add(toolbar, BorderLayout.NORTH);

		// inspector options
		inspector = new GraphInspector(this);
		add(inspector, BorderLayout.CENTER);

		pack();

	}


	private void setupToolbar (JToolBar toolbar) {
		log.trace( "Initialising toolbar." );

		toolbar.setMargin( new Insets(5, 5, 5, 5) );
		toolbar.setFloatable(false);


		JButton gameStateOpenBtn = new JButton( "Open", openIcon );
		final JToggleButton gameStateWatcherBtn = new JToggleButton("Monitor save game", watchIcon, false);
		final JToggleButton toggleGraphBtn = new JToggleButton("Graph", graphIcon, false);
		gameStateWatcherBtn.setEnabled(false);
		toggleGraphBtn.setEnabled(false);
		// TODO JButton "Refresh" to redraw graph
		// TODO spacing
		JButton exportImageBtn = new JButton( "Export", exportIcon );
		// TODO JButton "Export As…" that toggles an options dialog for the preferrend file-format
		//      and file destination. After confirm the "Export" button uses the options set by
		//      the "Export As…" button
		// TODO spacing
		// TODO JButton "Help" that sets the helpFrame visible


		// TODO get continue.sav automaticly if it exist on the expected location
		final JFileChooser fc = new JFileChooser();
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

				fc.setDialogTitle( "Open Saved Game" );
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

					// TODO write into configFile from here to remember chosenFile for later sessions
					// config.setProperty( "ftlContinuePath", chosenFile.getAbsolutePath() );

				} else if (sillyMistake || lastGameState == null) {
					gameStateWatcherBtn.setEnabled(false);
					toggleGraphBtn.setEnabled(false);
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
						timer.schedule( task , new Date(), 1000 ); // repeat the check every second

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
						// spawn window
						graphFrame.setVisible(true);

					} else {
						toggleGraphBtn.doClick();
					}
				} else {
					// destroy window if it exists
					graphFrame.setVisible(false);
				}
			}
		});


		exportImageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				GraphRenderer.captureImage = true;
			}
		});


		graphFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				toggleGraphBtn.setSelected(false);
			}
		});


		toolbar.add( gameStateOpenBtn );
		toolbar.add( gameStateWatcherBtn );
		toolbar.add( toggleGraphBtn );
		toolbar.add( exportImageBtn );

		toolbar.add( Box.createHorizontalGlue() );

	}


	private void setupGraphFrame() {

		graphFrame = new JFrame();

		graphFrame.setSize(1200, 700);
		graphFrame.setResizable(true);
		graphFrame.setLocationRelativeTo(null);
		graphFrame.setTitle(String.format("%s %d.0 - Graph Renderer", appName, appVersion));
		graphFrame.setLayout(new BorderLayout());

		processing.core.PApplet graphRenderer = new GraphRenderer();
		graphFrame.add(graphRenderer);
		GraphRenderer.panelWidth = graphFrame.getWidth();
		GraphRenderer.panelHeight= graphFrame.getHeight();
		graphRenderer.init();

		graphFrame.setVisible(false);

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
				musteryBuf.append( "This saved game file contains mystery bytes the developers hadn't anticipated!\n" );
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
				"Error reading saved game (\"%s\"):\n%s: %s",
				chosenFile.getName(), f.getClass().getSimpleName(), f.getMessage())
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

			if (currentGameState.getEncounter().getText().toLowerCase().contains("giant alien spiders".toLowerCase())) {
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