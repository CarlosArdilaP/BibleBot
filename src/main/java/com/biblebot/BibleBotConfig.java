package com.biblebot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BibleBotConfig {

    @SerializedName("interval_minutes")
    public int intervalMinutes = 5;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("biblebot.json");

    public static BibleBotConfig load() {
        if (!Files.exists(CONFIG_PATH)) {
            BibleBotConfig defaults = new BibleBotConfig();
            defaults.save();
            BibleBot.LOGGER.info("Configuración de BibleBot creada en: {}", CONFIG_PATH);
            return defaults;
        }
        try (Reader reader = new InputStreamReader(Files.newInputStream(CONFIG_PATH), StandardCharsets.UTF_8)) {
            BibleBotConfig cfg = GSON.fromJson(reader, BibleBotConfig.class);
            return cfg != null ? cfg : new BibleBotConfig();
        } catch (IOException e) {
            BibleBot.LOGGER.error("Error al leer biblebot.json, usando valores por defecto", e);
            return new BibleBotConfig();
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = new OutputStreamWriter(Files.newOutputStream(CONFIG_PATH), StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            BibleBot.LOGGER.error("Error al guardar biblebot.json", e);
        }
    }

    public int getIntervalTicks() {
        return Math.max(1, intervalMinutes) * 20 * 60;
    }
}
