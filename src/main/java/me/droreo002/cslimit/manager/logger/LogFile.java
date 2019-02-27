package me.droreo002.cslimit.manager.logger;

import lombok.Getter;
import lombok.Setter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.oreocore.utils.io.FileUtils;
import me.droreo002.oreocore.utils.misc.TimeStampUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

public final class LogFile {

    // TODO : Log file complete, data complete. Now continue command and core, then we're DONE!. WHOOOO!

    @Getter
    private final Logger logger = Logger.getLogger(Debug.class.getCanonicalName());
    @Getter
    private final TimeStampUtils utils = new TimeStampUtils("dd-MM-yyyy");

    @Getter
    @Setter
    private String currentLogFileName;
    @Getter
    @Setter
    private File logFile;
    @Getter
    @Setter
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
        logHandler.setFormatter(new CSLFormatter());
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger.addHandler(logHandler);
    }

    private String getNextLogName() {
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        final File logsFolder = new File(plugin.getDataFolder(), "logs");
        if (logsFolder.listFiles() == null) return utils.getDateFormat().format(new Date()) + "_0";
        File[] logs = logsFolder.listFiles();
        List<File> sameFile = new ArrayList<>();
        for (File f : logs) {
            String fileName = FileUtils.getFileName(f, false);
            String date = utils.getDateFormat().format(new Date());
            if (fileName.contains(date)) sameFile.add(f);
        }
        String currentFileName = utils.getDateFormat().format(new Date());
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

    private class CSLFormatter extends Formatter {

        private final String logFormat = ChestShopLimiter.getInstance().getConfigManager().getMemory().getLogFormat();

        @Override
        public String format(LogRecord record) {
            return logFormat
                    .replace("%date", utils.getCurrentTimestampString())
                    .replace("%logLevel", String.valueOf(record.getLevel()))
                    .replace("%message", formatMessage(record)) + System.lineSeparator();
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }

    private class LogFileUpdater implements Runnable {

        @Override
        public void run() {
            Timestamp before = utils.convertStringToTimestamp(currentLogFileName.split("_")[0]);
            Timestamp now = utils.getCurrentTimestamp();
            if (before.after(now)) {
                currentLogFileName = getNextLogName();
                setup();
            }
        }
    }
}
