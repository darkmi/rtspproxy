/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   Copyright (C) 2005 - Matteo Merli - matteo.merli@gmail.com            *
 *                                                                         *
 ***************************************************************************/

package rtspproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * General configuration system.
 */
public class Config {

	private static Logger logger = Logger.getLogger(Config.class);
	private static Properties properties = new Properties();

	private static String rtspproxy_home;
	private static String name;
	private static String version;
	private static String proxySignature;

	protected Config() {
		rtspproxy_home = System.getProperty("rtspproxy.home");
		if (rtspproxy_home == null) {
			rtspproxy_home = System.getProperty("user.dir");
			if (rtspproxy_home == null)
				rtspproxy_home = "";
		}

		// Read program name and version
		Properties jarProps = new Properties();

		try {
			jarProps.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("META-INF/application.properties"));
			name = jarProps.getProperty("application.name");
			version = jarProps.getProperty("application.version");
		} catch (Exception e) {
			name = "RtspProxy";
			version = "";
		}

		proxySignature = name + " " + version + " (" + System.getProperty("os.name") + " / "
				+ System.getProperty("os.version") + " / " + System.getProperty("os.arch") + ")";

		String[] paths = new String[5];

		// Used for testing purposes:
		// checks for the configuration file
		paths[4] = "src" + File.separator + "resources" + File.separator + "conf" + File.separator
				+ "rtspproxy.properties";

		// Current directory configuration
		paths[3] = "rtspproxy.properties";

		// RtspProxy home folder
		paths[2] = rtspproxy_home + File.separator + "conf" + File.separator + "rtspproxy.properties";

		// Per user config
		paths[1] = System.getProperty("user.home", "") + File.separator + ".rtspproxy.properties";
		// System wide configuration (tipical in unix systems)
		paths[0] = "/etc/rtspproxy.properties";

		for (String path : paths) {
			try {
				properties.load(new FileInputStream(path));
				// Immediately apply debug settings!
				updateDebugSettings();

				logger.debug("Reading configurations from '" + path + "'");
				// break;

			} catch (IOException e) {
				// Silently ignore
			}
		}

		for (Object key : properties.keySet()) {
			logger.debug((String) key + " : " + properties.getProperty((String) key));
		}
	}

	public static String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return the value of an integer property
	 */
	public static int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(key));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Convert a list of comma separated integers string into an array of
	 * integers.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int[] getIntArray(String key, int defaultValue) {
		try {
			String toks[] = properties.getProperty(key).split(",");
			int res[] = new int[toks.length];
			int i = 0;
			for (String tok : toks) {
				res[i++] = Integer.parseInt(tok.trim());
			}
			return res;

		} catch (Exception e) {
			int res[] = { defaultValue };
			return res;
		}
	}

	/**
	 * Get a boolean property from config.
	 * 
	 * @param key
	 *        the name of the property
	 * @param defaultValue
	 *        its default value
	 * @return the boolean value
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		String value;

		try {
			value = properties.getProperty(key);
		} catch (Exception e) {
			return defaultValue;
		}

		// Try to convert a a String to a boolean
		if ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
			return true;
		} else if ("false".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
			return false;
		}

		return defaultValue;
	}

	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static void set(String key, String value) {
		properties.setProperty(key, value);
	}

	public static void setBoolean(String key, boolean value) {
		properties.setProperty(key, value ? "true" : "false");
	}

	/**
	 * @param key
	 * @param value
	 */
	public static void setInt(String key, int value) {
		properties.setProperty(key, Integer.toString(value));
	}

	public static void setIntArray(String key, int[] values) {
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i > 0)
				build.append(", ");
			build.append(Integer.toString(values[i]));
		}
		properties.setProperty(key, build.toString());
	}

	/**
	 * @return the home directory of the proxy installation
	 */
	public static String getHome() {
		return rtspproxy_home;
	}

	private static void updateDebugSettings() {
		Properties prop = new Properties();
		// common properties
		prop.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.A1.layout.ConversionPattern", "%7p [%t] (%F:%L) - %m%n");

		if (getBoolean("log.debug"))
			prop.setProperty("log4j.rootLogger", "DEBUG, A1");
		else
			// only write important messages
			prop.setProperty("log4j.rootLogger", "INFO, A1");

		if (getBoolean("log.logtofile")) {
			// save logs in a file
			String filename = get("log.file", "logs" + File.separator + "rtspproxy.log");
			prop.setProperty("log4j.appender.A1", "org.apache.log4j.RollingFileAppender");
			prop.setProperty("log4j.appender.A1.File", filename);

			// if logs directory does not exists, create it
			File logs = new File(rtspproxy_home + File.separator + "logs");
			if (!logs.exists())
				logs.mkdir();

		} else {
			// Log to console
			prop.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
		}

		PropertyConfigurator.configure(prop);
	}

	/**
	 * @return Returns the application name.
	 */
	public static String getName() {
		return name;
	}

	/**
	 * @return Returns the application version.
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * @return Returns the proxySignature.
	 */
	public static String getProxySignature() {
		return proxySignature;
	}
}
