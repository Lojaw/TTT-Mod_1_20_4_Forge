package de.lojaw.tttmod;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.File;

public class LoggerUtils {
    public static final Logger logger = Logger.getLogger("EventLogger");
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("C:\\Users\\jpsch\\AppData\\Roaming\\.minecraft\\mods\\logger.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearLogFile() {
        fileHandler.close();
        File logFile = new File("C:\\Users\\jpsch\\AppData\\Roaming\\.minecraft\\mods\\logger.txt");
        if (logFile.exists()) {
            logFile.delete();
        }
        try {
            fileHandler = new FileHandler("C:\\Users\\jpsch\\AppData\\Roaming\\.minecraft\\mods\\logger.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
