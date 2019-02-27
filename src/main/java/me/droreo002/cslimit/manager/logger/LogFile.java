package me.droreo002.cslimit.manager.logger;

import lombok.Getter;
import lombok.Setter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.oreocore.utils.misc.TimeStampUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public final class LogFile {

    @Getter
    private final Logger logger = Logger.getLogger(Debug.class.getCanonicalName());
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
        this.currentLogFileName = TimeStampUtils.getCurrentTimestampString();
        setup();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ChestShopLimiter.getInstance(), new LogFileUpdater(), 0L, 20L * 600L);
    }

    private void setup() {
        logFile = new File(ChestShopLimiter.getInstance().getDataFolder(), "logs" + File.separator + currentLogFileName + ".log");
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
        logger.removeHandler(logHandler);
        logger.addHandler(logHandler);
    }


    private class CSLFormatter extends Formatter {

        // Create a DateFormat to format the logger timestamp.
        private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        private final String logFormat = ChestShopLimiter.getInstance().getConfigManager().getMemory().getLogFormat();

        @Override
        public String format(LogRecord record) {
            return logFormat
                    .replace("%date", df.format(new Date(record.getMillis())))
                    .replace("%logLevel", String.valueOf(record.getLevel()))
                    .replace("%message", formatMessage(record));
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
            Timestamp before = TimeStampUtils.convertStringToTimestamp(currentLogFileName);
            Timestamp now = TimeStampUtils.getCurrentTimestamp();
            if (before.after(now)) {
                currentLogFileName = TimeStampUtils.convertTimestampToString(now);
                setup();
            }
        }
    }
}
