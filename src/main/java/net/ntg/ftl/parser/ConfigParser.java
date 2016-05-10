package net.ntg.ftl.parser;

import net.ntg.ftl.FTLAdventureVisualiser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;


public class ConfigParser {

	private static final Logger log = LogManager.getLogger(ConfigParser.class);

	public ConfigParser() {
	}


	public Properties readConfig() {
		Properties config = new Properties();
		// Read the config file.
		InputStream in = null;
		try {
			if (FTLAdventureVisualiser.configFile.exists()) {
				log.trace("Loading properties from config file");
				in = new FileInputStream(FTLAdventureVisualiser.configFile);
				config.load(new InputStreamReader(in, "UTF-8"));
			} else {
				writeConfig(config);
			}
		} catch (IOException e) {
			log.error("Error loading config", e);
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {}
		}
		return config;
	}

	public void addConfigProperty(String key, String value) {
		writeConfig((Properties) readConfig().setProperty(key, value));
	}

	public void writeConfig(Properties config) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(FTLAdventureVisualiser.configFile);
			String configComments = FTLAdventureVisualiser.APP_NAME + " " + FTLAdventureVisualiser.APP_VERSION + " - Config File";
			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			config.store(writer, configComments);
			writer.flush();
		} catch (IOException e) {
			log.error("Error saving config to "+ FTLAdventureVisualiser.configFile.getPath(), e);
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {}
		}
	}
}
