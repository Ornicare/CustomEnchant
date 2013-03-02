package fr.enchantments.custom.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerManager
{

    private static final String pluginName = "CustomEnchantments";
    private static final String pluginVersion = "v0.1-Alpha";

    private static Logger Logger;
    public static void setLogger(Logger newLogger) { Logger = newLogger; }

    public static void log(String logMessage) { Logger.log(Level.INFO, getLoggerPrefix() + logMessage); }
    public static void logError(String logMessage) { Logger.log(Level.WARNING, getLoggerPrefix() + logMessage); }
    public static void logFatalError(String logMessage) { Logger.log(Level.SEVERE, getLoggerPrefix() + logMessage); }

    private static String getLoggerPrefix() { return "[" + pluginName + " - " + pluginVersion + "] "; }

}
