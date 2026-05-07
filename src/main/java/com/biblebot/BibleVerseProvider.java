package com.biblebot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BibleVerseProvider {
    private static final Random RANDOM = new Random();
    private static List<BibleVerse> verses = new ArrayList<>();

    public static void load(String language) {
        List<BibleVerse> loaded = new ArrayList<>();
        String path = "/assets/biblebot/lang/" + language + "/verses.json";
        try (InputStream stream = BibleVerseProvider.class.getResourceAsStream(path)) {
            if (stream == null) {
                BibleBot.LOGGER.warn("[BibleBot] No se encontró verses.json para el idioma '{}'.", language);
                verses = loaded;
                return;
            }
            JsonArray array = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonArray.class);
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                loaded.add(new BibleVerse(
                        obj.get("book").getAsString(),
                        obj.get("chapter").getAsInt(),
                        obj.get("verse").getAsInt(),
                        obj.get("text").getAsString()
                ));
            }
            BibleBot.LOGGER.info("[BibleBot] {} versículos cargados para idioma '{}'.", loaded.size(), language);
        } catch (Exception e) {
            BibleBot.LOGGER.error("[BibleBot] Error al cargar versículos para idioma '{}'.", language, e);
        }
        verses = loaded;
    }

    public static BibleVerse getRandom() {
        if (verses.isEmpty()) {
            return new BibleVerse("Juan", 3, 16,
                    "Porque tanto amó Dios al mundo que dio a su Hijo único, para que todo el que crea en él no perezca, sino que tenga vida eterna.");
        }
        return verses.get(RANDOM.nextInt(verses.size()));
    }

    public static List<BibleVerse> getAll() {
        return Collections.unmodifiableList(verses);
    }
}
