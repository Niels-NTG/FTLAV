package net.ntg.ftl;

import net.blerf.ftl.model.Profile;
import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.DefaultDataManager;
import net.blerf.ftl.parser.SavedGameParser;
import net.ntg.ftl.parser.ConfigParser;
import net.ntg.ftl.ui.FTLFrame;
import net.vhati.modmanager.core.FTLUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jdom2.JDOMException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class FTLAdventureVisualiser {

	private static final Logger log = LogManager.getLogger(FTLAdventureVisualiser.class);

	public static final String APP_NAME = "FTL Adventure Visualiser";
	public static final int APP_VERSION = 3;

	public static final File configFile = new File("FTLAVconfig.cfg");
	public static File gameStateFile;
	public static File profileFile;

	public static SavedGameParser.SavedGameState gameState = null;
	public static SavedGameParser.ShipState shipState = null;
	public static SavedGameParser.ShipState nearbyShipState = null;
	public static List<SavedGameParser.CrewState> playerCrewState;
	public static List<SavedGameParser.CrewState> enemyCrewState;
	public static SavedGameParser.EnvironmentState environmentState = null;
	public static ArrayList<SectorDot> sectorArray = new ArrayList<>();
	public static String fileChangedTimeStamp;

	public static Profile profile = null;

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

		ConfigParser configParser = new ConfigParser();
		Properties config = configParser.readConfig();

		// FTL Resources Path.
		String datsPath = config.getProperty("ftlDatsPath");
		String continuePath = config.getProperty("ftlContinuePath");
		String profilePath = config.getProperty("ftlProfilePath");

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
				} else {
					log.error(candidateSaveFile.getAbsolutePath() + " doesn't seem to be a valid FTL save file because it doesn't exist or is invalid");
				}

				File candidateProfileFile;
				if (profilePath != null) {
					log.info("The config's ftlProfilePath exists");
					candidateProfileFile = new File(profilePath);
				} else {
					log.info("No path to prof.sav found in config. Guessing possible location...");
					candidateProfileFile = new File(FTLUtilities.findUserDataDir(), "prof.sav");
				}
				if (candidateProfileFile.exists()) {
					profileFile = candidateProfileFile;
					config.setProperty("ftlProfilePath", candidateProfileFile.getAbsolutePath());
				} else {
					log.error(candidateSaveFile.getAbsolutePath() + " doesn't seem to be a valid FTL profile file because it doesn't exist or is invalid");
				}

				configParser.writeConfig(config);

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
					"FTL resources were found in:\n"+
					datsDir.getPath() + "\n" +
					"Is this correct?",
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
				File candidateProfileFile = new File(FTLUtilities.findUserDataDir(), "prof.sav");
				if (candidateProfileFile.exists()) {
					profileFile = candidateProfileFile;
					config.setProperty("ftlProfilePath", candidateProfileFile.getAbsolutePath());
				}

				configParser.writeConfig(config);
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


	private static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

}