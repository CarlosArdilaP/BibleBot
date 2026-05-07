package com.biblebot;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BibleBot implements ModInitializer {
    public static final String MOD_ID = "biblebot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static BibleBotConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = BibleBotConfig.load();
        LOGGER.info("BibleBot inicializado — intervalo: {} minuto(s)", CONFIG.intervalMinutes);
        BibleBotScheduler.register();
    }
}
