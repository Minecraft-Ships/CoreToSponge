package org.core.implementation.sponge.platform;

import org.core.adventureText.AText;
import org.core.logger.Logger;
import org.jetbrains.annotations.NotNull;

public class SLogger implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    public SLogger(org.apache.logging.log4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(@NotNull String log) {
        this.logger.info(log);
    }

    @Override
    public void log(@NotNull AText log) {
        this.logger.info(log.toPlain());
    }

    @Override
    public void warning(@NotNull String log) {
        this.logger.warn(log);
    }

    @Override
    public void error(@NotNull String log) {
        this.logger.error(log);
    }
}
