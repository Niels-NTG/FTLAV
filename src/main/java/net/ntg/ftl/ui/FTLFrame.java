package net.ntg.ftl.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.filechooser.FileFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.ui.DumpPanel;
import net.ntg.ftl.ui.graph.GraphPanelGeneral;
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

	private static final String SAVE_DUMP = "Dump";
	private static final String SAVE_GRAPH= "Graph Inspector";

	private File chosenFile;
	private SavedGameParser.SavedGameState lastGameState = null;

	private ImageIcon openIcon = new ImageIcon(ClassLoader.getSystemResource("open.gif"));

	private JButton gameStateSaveBtn;
	private JTabbedPane savedGameTabsPane;
	private DumpPanel dumpPanel;
	private GraphPanelGeneral graphPanelGeneral;

	private String appName;
	private int appVersion;

	public FTLFrame (String appName, int appVersion) {
		this.appName = appName;
		this.appVersion = appVersion;


		// UI setup
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800,700);
		setLocationRelativeTo(null);
		setTitle(String.format("%s (version %d)", appName, appVersion));

		JPanel contentPane = new JPanel(new GridLayout(2,1));
		setContentPane(contentPane);


		// UI savedGameInspector
		JPanel savedGameInspector = new JPanel(new BorderLayout());
		contentPane.add(savedGameInspector);

		JToolBar savedGameToolbar = new JToolBar();
		setupSavedGameToolbar(savedGameToolbar);
		savedGameInspector.add(savedGameToolbar, BorderLayout.NORTH);

		savedGameTabsPane = new JTabbedPane();
		savedGameInspector.add( savedGameTabsPane, BorderLayout.CENTER );

		dumpPanel = new DumpPanel();
		graphPanelGeneral = new GraphPanelGeneral(this);

		graphPanelGeneral.setFloatable(false);

		savedGameTabsPane.addTab(SAVE_DUMP, dumpPanel);
		savedGameTabsPane.addTab(SAVE_GRAPH,graphPanelGeneral);


		// UI Graph
		JPanel savedGameGraphPane = new JPanel(new BorderLayout());
		contentPane.add(savedGameGraphPane);

		processing.core.PApplet graphRenderer = new GraphRenderer();
		savedGameGraphPane.add(graphRenderer);
		// TODO get the actual height of the graphRender frame
		// some way that is not as bad as this one
		GraphRenderer.panelWidth = this.getWidth();
		GraphRenderer.panelHeight= this.getHeight() / 2;
		graphRenderer.init();
	}


	private void setupSavedGameToolbar (JToolBar toolbar) {
		log.trace( "Initialising SavedGame toolbar." );

		toolbar.setMargin( new Insets(5, 5, 5, 5) );
		toolbar.setFloatable(false);

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

		JButton gameStateOpenBtn = new JButton( "Open", openIcon );
		gameStateOpenBtn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				log.trace( "Open saved game button clicked." );

				fc.setDialogTitle( "Open Saved Game" );
				int chooserResponse = fc.showOpenDialog(FTLFrame.this);
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

					// TODO write into configFile from here to remember chosenFile for later sessions
					// config.setProperty( "ftlContinuePath", chosenFile.getAbsolutePath() );

				} else {
					log.trace( "Open dialog cancelled." );
				}
			}
		});
		toolbar.add( gameStateOpenBtn );


		// TODO button automaticly set to true when save file has been loaded
		final JToggleButton gameStateWatcherBtn = new JToggleButton("Monitor save game", false);
		gameStateWatcherBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				gameStateWatcherBtn.setSelected(abstractButton.getModel().isSelected());

				if ( gameStateWatcherBtn.isSelected() ) {
					if ( lastGameState != null ) {
						// start monitor function
						log.info("start monitoring save file");

						TimerTask task = new FileWatcher( chosenFile ) {
							protected void onChange( File file ) {
								// here we code the action on a change
								log.info( "FILE "+ file.getName() +" HAS CHANGED !" );
								loadGameStateFile ( chosenFile );
							}
						};

						Timer timer = new Timer();
						// repeat the check every second
						timer.schedule( task , new Date(), 1000 );

					} else {
						gameStateWatcherBtn.doClick();
					}
				} else {
					// TODO cancel TimerTask task somehow
					// task.cancel();
				}
			}
		});
		toolbar.add( gameStateWatcherBtn );

		toolbar.add( Box.createHorizontalGlue() );
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
			loadGameState( gs, gs.getPlayerShipState() );

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
			showErrorDialog( String.format("Error reading saved game (\"%s\"):\n%s: %s", chosenFile.getName(), f.getClass().getSimpleName(), f.getMessage()) );
			exception = f;
		} finally {
			try { if ( in != null ) in.close(); }
			catch ( IOException f ) {}
		}
	}


	public void loadGameState (SavedGameParser.SavedGameState currentGameState, SavedGameParser.ShipState currentShipState) {

		dumpPanel.setText(currentGameState != null ? currentGameState.toString() : "");

		log.info( "Currently at beacon number : " + currentGameState.getTotalBeaconsExplored() );
		log.info( "Currently in sector : " + currentGameState.getSectorNumber() );

		if (lastGameState != null) {
			if (currentGameState.getTotalBeaconsExplored() > lastGameState.getTotalBeaconsExplored() ||
				FTLAdventureVisualiser.gameStateArray.size() == 0
			) {
				FTLAdventureVisualiser.gameStateArray.add(currentGameState);
				FTLAdventureVisualiser.shipStateArray.add(currentShipState);

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

				graphPanelGeneral.setGameState();
			}
		}

		lastGameState = currentGameState;
	}


	private void showErrorDialog (String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}