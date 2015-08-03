package net.blerf.ftl;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import net.vhati.modmanager.core.FTLUtilities;

import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.DefaultDataManager;
import net.blerf.ftl.ui.FTLFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FTLProfileEditor {

	private static final Logger log = LogManager.getLogger(FTLProfileEditor.class);

	public static final String APP_NAME = "FTL Profile Editor";
	public static final int APP_VERSION = 25;


	public static void main( String[] args ) {

		// Ensure all popups are triggered from the event dispatch thread.

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiInit();
			}
		});
	}

	private static void guiInit() {
		// Don't use the hard drive to buffer streams during ImageIO.read().
		ImageIO.setUseCache(false);  // Small images don't need extra buffering.

		log.debug( String.format( "%s v%s", APP_NAME, APP_VERSION ) );
		log.debug( String.format( "%s %s", System.getProperty("os.name"), System.getProperty("os.version") ) );
		log.debug( String.format( "%s, %s, %s", System.getProperty("java.vm.name"), System.getProperty("java.version"), System.getProperty("os.arch") ) );

		File configFile = new File( "ftl-editor.cfg" );
		File datsDir = null;

		boolean writeConfig = false;
		Properties config = new Properties();
		config.setProperty( "useDefaultUI", "false" );

		// Read the config file.
		InputStream in = null;
		try {
			if ( configFile.exists() ) {
				log.trace( "Loading properties from config file." );
				in = new FileInputStream( configFile );
				config.load( new InputStreamReader( in, "UTF-8" ) );
			} else {
				writeConfig = true; // Create a new cfg, but only if necessary.
			}
		}
		catch ( IOException e ) {
			log.error( "Error loading config.", e );
			showErrorDialog( "Error loading config from "+ configFile.getPath() );
		}
		finally {
			try {if ( in != null ) in.close();}
			catch ( IOException e ) {}
		}

		// Look-and-Feel.
		String useDefaultUI = config.getProperty( "useDefaultUI" );

		if ( useDefaultUI == null || !useDefaultUI.equals("true") ) {
			try {
				log.trace( "Using system Look and Feel" );
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch ( Exception e ) {
				log.error( "Error setting system Look and Feel.", e );
				log.info( "Setting 'useDefaultUI=true' in the config file will prevent this error." );
			}
		}
		else {
			log.debug( "Using default Look and Feel." );
		}

		// FTL Resources Path.
		String datsPath = config.getProperty( "ftlDatsPath" );

		if ( datsPath != null && datsPath.length() > 0 ) {
			log.info( "Using FTL dats path from config: "+ datsPath );
			datsDir = new File( datsPath );
			if ( FTLUtilities.isDatsDirValid( datsDir ) == false ) {
				log.error( "The config's ftlDatsPath does not exist, or it lacks data.dat." );
				datsDir = null;
			}
		}
		else {
			log.trace( "No FTL dats path previously set." );
		}

		// Find/prompt for the path to set in the config.
		if ( datsDir == null ) {
			datsDir = FTLUtilities.findDatsDir();
			if ( datsDir != null ) {
				int response = JOptionPane.showConfirmDialog( null, "FTL resources were found in:\n"+ datsDir.getPath() +"\nIs this correct?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
				if ( response == JOptionPane.NO_OPTION ) datsDir = null;
			}

			if ( datsDir == null ) {
				log.debug( "FTL dats path was not located automatically. Prompting user for location." );
				datsDir = FTLUtilities.promptForDatsDir( null );
			}

			if ( datsDir != null ) {
				config.setProperty( "ftlDatsPath", datsDir.getAbsolutePath() );
				writeConfig = true;
				log.info( "FTL dats located at: "+ datsDir.getAbsolutePath() );
			}
		}

		if ( datsDir == null ) {
			showErrorDialog( "FTL data was not found.\nFTL Profile Editor will now exit." );
			log.debug( "No FTL dats path found, exiting." );
			System.exit( 1 );
		}

		if ( writeConfig ) {
			OutputStream out = null;
			try {
				out = new FileOutputStream( configFile );
				String configComments = "FTL Profile Editor - Config File";

				OutputStreamWriter writer = new OutputStreamWriter( out, "UTF-8" );
				config.store( writer, configComments );
				writer.flush();
			}
			catch ( IOException e ) {
				log.error( "Error saving config to "+ configFile.getPath(), e );
				showErrorDialog( "Error saving config to "+ configFile.getPath() );
			}
			finally {
				try {if ( out != null ) out.close();}
				catch ( IOException e ) {}
			}
		}

		if ( writeConfig ) {
			String wipMsg = "";
			wipMsg += "FTL:Advanced Edition (1.5.4+) added lots of new info to profiles and saved games.\n";
			wipMsg += "\n";
			wipMsg += "Previous editions stored their profile in \"prof.sav\".\n";
			wipMsg += "AE uses \"ae_prof.sav\" instead.\n";
			wipMsg += "\n";
			wipMsg += "AE reads the old profile the first time you play to migrate scores and such.\n";
			wipMsg += "Profiles from all editions are fully editable.\n";
			wipMsg += "\n";
			wipMsg += "Much of the new saved game info has yet to be deciphered, so AE's \"continue.sav\" is \n";
			wipMsg += "only partially editable for now.\n";
			wipMsg += "\n";
			wipMsg += "Saved games from FTL 1.03.3 and earlier are still fully editable.\n";
			wipMsg += "\n";
			wipMsg += "An old saved game or profile will just ignore the new fields.\n";
			wipMsg += "\n";
			wipMsg += "\n";
			wipMsg += "If you encounter a read error opening a file, that means the editor saw something \n";
			wipMsg += "new that it doesn't recognize. Submitting a bug report would be helpful.";
			JOptionPane.showMessageDialog(null, wipMsg, "Work in Progress", JOptionPane.PLAIN_MESSAGE);
		}

		// Parse the dats.
		try {
			DefaultDataManager dataManager = new DefaultDataManager( datsDir );
			DataManager.setInstance( dataManager );
			dataManager.setDLCEnabledByDefault( true );
		}
		catch ( Exception e ) {
			log.error( "Error parsing FTL resources.", e );
			showErrorDialog( "Error parsing FTL resources." );
			System.exit(1);
		}

		try {
			FTLFrame frame = new FTLFrame( APP_NAME, APP_VERSION );
			frame.setVisible(true);
		}
		catch ( Exception e ) {
			log.error( "Exception while creating FTLFrame.", e );
			System.exit( 1 );
		}

	}


	private static void showErrorDialog( String message ) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
