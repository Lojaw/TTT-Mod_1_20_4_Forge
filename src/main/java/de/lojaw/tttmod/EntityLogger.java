package de.lojaw.tttmod;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class EntityLogger {
    private static final Logger logger = Logger.getLogger("EntityLogger");
    public static final String ENTITY_IDS_FILE = getUserModsDirectory() + File.separator + "entity_ids.txt";
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(ENTITY_IDS_FILE, true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logEntityId(int entityId, String playerName) {
        try {
            if (playerName != null) {
                logger.info("Spawned entity for player: " + playerName + " (ID: " + entityId + ")");
            } else {
                logger.info("Spawned entity (ID: " + entityId + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearLogFile() {
        try {
            fileHandler.close();
            logger.removeHandler(fileHandler);
            File logFile = new File(ENTITY_IDS_FILE);
            if (logFile.exists()) {
                logFile.delete();
            }
            fileHandler = new FileHandler(ENTITY_IDS_FILE, true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String getUserModsDirectory() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator + ".minecraft" + File.separator + "mods";
    }
}