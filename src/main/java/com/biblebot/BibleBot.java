package com.biblebot;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BibleBot implements ModInitializer {
    public static final String MOD_ID = "biblebot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("BibleBot inicializado - ¡Que la Palabra de Dios ilumine el servidor!");
        BibleBotScheduler.register();
    }
}
