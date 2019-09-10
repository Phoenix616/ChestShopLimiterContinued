package me.droreo002.cslimit.manager.logger;

import me.droreo002.cslimit.ChestShopLimiter;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    private final String logFormat = ChestShopLimiter.getInstance().getConfigManager().getMemory().getLogFormat();

    @Override
    public String format(LogRecord record) {
        return logFormat
                .replace("%date%", LogFile.TIMESTAMP_BUILDER.buildAsString())
                .replace("%logLevel%", String.valueOf(record.getLevel()))
                .replace("%message%", formatMessage(record)) + System.lineSeparator();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}