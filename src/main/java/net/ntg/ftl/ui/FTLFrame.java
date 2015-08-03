package net.ntg.ftl.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
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
import net.ntg.ftl.ui.GraphPanel;
import net.ntg.ftl.ui.StatusbarMouseListener;
import net.ntg.ftl.util.FileWatcher;

import net.blerf.ftl.parser.MysteryBytes;
import net.blerf.ftl.parser.SavedGameParser;
// TODO remove write methods from SavedGameParser
import net.vhati.modmanager.core.FTLUtilities;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class FTLFrame extends JFrame {

	private static final Logger log = LogManager.getLogger(FTLFrame.class);

	private static final String SAVE_DUMP = "Dump";
	private static final String SAVE_GRAPH= "Graph";

	private File chosenFile;
	private SavedGameParser.SavedGameState lastGameState = null;

	private ImageIcon openIcon = new ImageIcon(ClassLoader.getSystemResource("open.gif"));

	private JButton gameStateSaveBtn;
	private JTabbedPane savedGameTabsPane;
	private DumpPanel savedGameDumpPanel;
	private GraphPanel savedGameGraphPanel;
	private JLabel statusLbl;

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

		// UI top
		JPanel contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);

		// UI savedGamePane
		JPanel savedGamePane = new JPanel(new BorderLayout());
		contentPane.add(savedGamePane, BorderLayout.CENTER);

		JToolBar savedGameToolbar = new JToolBar();
		setupSavedGameToolbar(savedGameToolbar);
		savedGamePane.add(savedGameToolbar, BorderLayout.NORTH);

		savedGameTabsPane = new JTabbedPane();
		savedGamePane.add( savedGameTabsPane, BorderLayout.CENTER );

		savedGameDumpPanel  = new DumpPanel();
		savedGameGraphPanel = new GraphPanel();

		savedGameTabsPane.addTab(SAVE_DUMP, savedGameDumpPanel);
		savedGameTabsPane.addTab(SAVE_GRAPH,savedGameGraphPanel);

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout( new BoxLayout(statusPanel, BoxLayout.Y_AXIS) );
		statusPanel.setBorder( BorderFactory.createLoweredBevelBorder() );
		statusLbl = new JLabel( " " );
		statusLbl.setBorder( BorderFactory.createEmptyBorder(2, 4, 2, 4));
		statusLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		statusPanel.add( statusLbl );
		contentPane.add( statusPanel, BorderLayout.SOUTH );
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
		gameStateOpenBtn.addMouseListener( new StatusbarMouseListener(this, "Open an existing saved game.") );
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
		gameStateWatcherBtn.addMouseListener(new StatusbarMouseListener(this, "Constantly watch save game file for changes"));
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
			showErrorDialog( String.format("Error reading saved game (\"%s\"):\n%s: %s", chosenFile.getName(), f.getClass().getSimpleName(), f.getMessage()) );
			exception = f;
		} finally {
			try { if ( in != null ) in.close(); }
			catch ( IOException f ) {}
		}
	}


	public void loadGameState( SavedGameParser.SavedGameState currentGameState ) {
		savedGameDumpPanel.setText((currentGameState != null ? currentGameState.toString() : ""));

		if (lastGameState != null) {
			if (currentGameState.getTotalBeaconsExplored() > lastGameState.getTotalBeaconsExplored()) {
				log.info( "currently at beacon number " + currentGameState.getTotalBeaconsExplored() );

				FTLAdventureVisualiser.gameStateArray.add(currentGameState);

				String dumplogString = "";
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					dumplogString += "\n Beacons explored: ";
					dumplogString += FTLAdventureVisualiser.gameStateArray.get(i).getTotalBeaconsExplored();
					dumplogString += "\n Scrap collected : ";
					dumplogString += FTLAdventureVisualiser.gameStateArray.get(i).getTotalScrapCollected();
					dumplogString += "\n";
				}
				log.info (dumplogString);
			}
		}

		lastGameState = currentGameState;
	}


	private void showErrorDialog (String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}


	public void setStatusText( String text ) {
		if (text.length() > 0) {
			statusLbl.setText( text );
		} else {
			statusLbl.setText( " " );
		}
	}
}