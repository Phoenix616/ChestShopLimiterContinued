package me.droreo002.cslimit.manager.logger;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.oreocore.utils.io.FileUtils;
import me.droreo002.oreocore.utils.time.TimestampBuilder;
import me.droreo002.oreocore.utils.time.TimestampUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

public final class LogFile {

    static final TimestampBuilder TIMESTAMP_BUILDER = TimestampBuilder.builder("dd-MM-yyyy");

    @Getter
    private final Logger logger = Logger.getLogger(Debug.class.getCanonicalName());

    @Getter
    private String currentLogFileName;
    @Getter
    private File logFile;
    @Getter
    private FileHandler logHandler;

    public LogFile() {
        this.currentLogFileName = getNextLogName();
        setup();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ChestShopLimiter.getInstance(), new LogFileUpdater(), 0L, 20L * 600L);
    }

    private void setup() {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        final File logsFolder = new File(plugin.getDataFolder(), "logs");
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        if (!logsFolder.exists()) logsFolder.mkdir();
        logFile = new File(plugin.getDataFolder(), "logs" + File.separator + currentLogFileName + ".log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            logHandler = new FileHandler(logFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        logHandler.setFormatter(new LogFormatter());
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger.addHandler(logHandler);
    }

    private String getNextLogName() {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        final File logsFolder = new File(plugin.getDataFolder(), "logs");
        if (logsFolder.listFiles() == null) return TIMESTAMP_BUILDER.getDateFormat().format(new Date()) + "_0";
        File[] logs = logsFolder.listFiles();
        List<File> sameFile = new ArrayList<>();
        for (File f : logs) {
            String fileName = FileUtils.getFileName(f, false);
            String date = TIMESTAMP_BUILDER.getDateFormat().format(new Date());
            if (fileName.contains(date)) sameFile.add(f);
        }

        String currentFileName = TIMESTAMP_BUILDER.getDateFormat().format(new Date());
        int currentNumber = 0;
        for (File f : sameFile) {
            String fileName = FileUtils.getFileName(f, false);
            try {
                int logFileNumber = Integer.parseInt(fileName.split("_")[1]);
                if (currentNumber < logFileNumber) {
                    currentFileName = fileName;
                    currentNumber = logFileNumber;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {} // Ignore
        }
        currentFileName = currentFileName.replace("_" + currentNumber, "");
        return currentFileName + "_" + (currentNumber + 1);
    }

    private class LogFileUpdater implements Runnable {

        @Override
        public void run() {
            Timestamp before = TimestampUtils.convertStringToTimestamp(currentLogFileName.split("_")[0], TIMESTAMP_BUILDER.getDateFormat());
            Timestamp now = TIMESTAMP_BUILDER.build();
            if (before.after(now)) {
                currentLogFileName = getNextLogName();
                setup();
            }
        }
    }
}
