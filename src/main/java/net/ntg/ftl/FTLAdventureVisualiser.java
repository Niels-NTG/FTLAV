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
import javax.swing.UIManager;
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
	public static Map<String, Boolean> enabledRecordingHeaders;


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

		// TODO make ftlav-config.cfg in a seperate class to make it acessible from other places than just here
		File configFile = new File("ftlav-config.cfg");
		File datsDir = null;

		boolean writeConfig = false;
		Properties config = new Properties();
		config.setProperty("useDefaultUI", "false");

		// Read the config file.
		InputStream in = null;
		try {
			if (configFile.exists()) {
				log.trace("Loading properties from config file.");
				in = new FileInputStream(configFile);
				config.load(new InputStreamReader(in, "UTF-8"));
			} else {
				writeConfig = true; // Create a new cfg, but only if necessary.
			}
		} catch (IOException e) {
			log.error("Error loading config.", e);
			showErrorDialog("Error loading config from " + configFile.getPath());
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {}
		}

		// Look-and-Feel.
		String useDefaultUI = config.getProperty("useDefaultUI");

		if (useDefaultUI == null || !useDefaultUI.equals("true")) {
			try {
				log.trace("Using system Look and Feel");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				log.error("Error setting system Look and Feel.", e);
				log.info("Setting 'useDefaultUI=true' in the config file will prevent this error.");
			}
		} else {
			log.debug("Using default Look and Feel.");
		}

		// FTL Resources Path.
		String datsPath = config.getProperty("ftlDatsPath");

		if (datsPath != null && datsPath.length() > 0) {
			log.info("Using FTL dats path from config: " + datsPath);
			datsDir = new File(datsPath);
			if (FTLUtilities.isDatsDirValid(datsDir) == false) {
				log.error("The config's ftlDatsPath does not exist, or it lacks data.dat.");
				datsDir = null;
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
				writeConfig = true;
				log.info("FTL dats located at: "+ datsDir.getAbsolutePath());
			}
		}

		if (datsDir == null) {
			showErrorDialog("FTL data was not found.\nFTL Adventure Visualiser will now exit.");
			log.debug("No FTL dats path found, exiting.");
			System.exit(1);
		}

		if (writeConfig) {
			OutputStream out = null;
			try {
				out = new FileOutputStream(configFile);
				String configComments = "FTL Adventure Visualiser - Config File";

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

		// if (writeConfig) {
		// 	String wipMsg = "";
		// 	wipMsg += "FTL: Faster Than Light Adventure Visualiser (FTLAV) is a tool to visualize your FTL playsessions\n";
		// 	wipMsg += "\n";
		// 	wipMsg += "This program is build on top of the FTL Profile Editor by Vhati\n";
		// 	wipMsg += "\n";
		// 	wipMsg += "This application requires the \"continue.sav\" from the FTL game directory.\n";
		// 	wipMsg += "This is where your current game progress is stored.\n";
		// 	wipMsg += "This application can and will not modify this or any other files.\n";
		// 	wipMsg += "\n";
		// 	wipMsg += "If you encounter a read error opening a file, that means the editor saw something \n";
		// 	wipMsg += "new that it doesn't recognize. Submitting a bug report would be helpful.";
		// 	JOptionPane.showMessageDialog(null, wipMsg, "Work in Progress", JOptionPane.PLAIN_MESSAGE);
		// }

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


	private static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

}