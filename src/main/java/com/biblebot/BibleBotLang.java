package com.biblebot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class BibleBotLang {

    private static Map<String, String> messages = Collections.emptyMap();

    public static void load(String language) {
        String path = "/assets/biblebot/lang/" + language + "/messages.json";
        try (InputStream stream = BibleBotLang.class.getResourceAsStream(path)) {
            if (stream == null) {
                BibleBot.LOGGER.warn("[BibleBot] No se encontró messages.json para el idioma '{}'.", language);
                return;
            }
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            messages = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), type);
            BibleBot.LOGGER.info("[BibleBot] Idioma '{}' cargado ({} claves).", language, messages.size());
        } catch (Exception e) {
            BibleBot.LOGGER.error("[BibleBot] Error al cargar messages.json para '{}'.", language, e);
        }
    }

    public static String get(String key, Object... args) {
        String template = messages.getOrDefault(key, key);
        for (int i = 0; i < args.length; i++) {
            template = template.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return template;
    }
}
