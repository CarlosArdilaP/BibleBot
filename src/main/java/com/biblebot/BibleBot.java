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
        BibleBotLang.load(CONFIG.language);
        BibleVerseProvider.load(CONFIG.language);
        LOGGER.info("BibleBot inicializado — idioma: {}, intervalo: {} minuto(s)", CONFIG.language, CONFIG.intervalMinutes);
        BibleBotScheduler.register();
        BibleBotCommands.register();
    }
}
