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
    private static final List<BibleVerse> VERSES = new ArrayList<>();

    static {
        loadVerses();
    }

    private static void loadVerses() {
        try (InputStream stream = BibleVerseProvider.class.getResourceAsStream("/assets/biblebot/verses.json")) {
            if (stream == null) {
                BibleBot.LOGGER.error("No se encontró el archivo verses.json");
                return;
            }
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            JsonArray array = new Gson().fromJson(reader, JsonArray.class);

            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                VERSES.add(new BibleVerse(
                        obj.get("book").getAsString(),
                        obj.get("chapter").getAsInt(),
                        obj.get("verse").getAsInt(),
                        obj.get("text").getAsString()
                ));
            }
            BibleBot.LOGGER.info("BibleBot: {} versículos cargados.", VERSES.size());
        } catch (Exception e) {
            BibleBot.LOGGER.error("Error al cargar los versículos", e);
        }
    }

    public static BibleVerse getRandom() {
        if (VERSES.isEmpty()) {
            return new BibleVerse("Juan", 3, 16,
                    "Porque tanto amó Dios al mundo que dio a su Hijo único, para que todo el que crea en él no perezca, sino que tenga vida eterna.");
        }
        return VERSES.get(RANDOM.nextInt(VERSES.size()));
    }

    public static List<BibleVerse> getAll() {
        return Collections.unmodifiableList(VERSES);
    }
}
