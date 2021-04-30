package cz.pvpcraft.GGGEDR.PcMyMenus.Logs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;


public class ConsoleHider extends AbstractFilter {
    private void registerFilter() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(this);
    }

    @Override
    public Result filter(LogEvent event) {
        return isLoggable(event.getMessage().getFormattedMessage(), event.getLevel());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return isLoggable(msg.getFormattedMessage(), level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return isLoggable(msg, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return isLoggable(msg.toString(), level);
    }

    private Result isLoggable(String msg, Level level) {
        if (level != null && level.intLevel() >= 300) {
            if (msg != null) {
                if (msg.contains("Warning: Nashorn engine is planned to be removed from a future JDK release")) {
                    return Result.DENY;
                }
            }
        }
        return Result.NEUTRAL;
    }

    public ConsoleHider() {
        registerFilter();
    }
}
