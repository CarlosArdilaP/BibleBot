package com.biblebot;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibleBotScheduler {

    // Todas las referencias directas o indirectas a las tres Personas de la Trinidad
    // El orden importa: las frases más largas van primero para que no sean consumidas
    // por términos más cortos (ej. "Espíritu Santo" antes que "Espíritu").
    private static final Pattern TRINITY_PATTERN = Pattern.compile(
            "Hijo del hombre|Espíritu Santo|Jesucristo" +
            "|Dios|Señor|Cristo|Jesús|Padre|Hijo|Espíritu" +
            "|Altísimo|Omnipotente|Verbo|Cordero"
    );

    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(BibleBotScheduler::onTick);
        BibleBot.LOGGER.info("Scheduler registrado — intervalo: {} minuto(s) ({} ticks)",
                BibleBot.CONFIG.intervalMinutes, BibleBot.CONFIG.getIntervalTicks());
    }

    private static void onTick(MinecraftServer server) {
        if (server.getPlayerList().getPlayers().isEmpty()) return;

        tickCounter++;
        if (tickCounter >= BibleBot.CONFIG.getIntervalTicks()) {
            tickCounter = 0;
            sendVerse(server);
        }
    }

    private static void sendVerse(MinecraftServer server) {
        BibleVerse verse = BibleVerseProvider.getRandom();

        MutableComponent message = Component.literal("[BibleBot] ").withStyle(ChatFormatting.DARK_AQUA)
                .append(Component.literal("✝ ").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(verse.reference() + " ").withStyle(ChatFormatting.YELLOW))
                .append(Component.literal("» ").withStyle(ChatFormatting.GRAY))
                .append(colorizeText(verse.text()));

        server.getPlayerList().getPlayers().forEach(player ->
                player.sendSystemMessage(message)
        );

        BibleBot.LOGGER.info("[BibleBot] {}", verse.reference());
    }

    // Divide el texto del versículo y aplica dorado a cada referencia trinitaria.
    private static MutableComponent colorizeText(String text) {
        MutableComponent result = Component.empty();
        Matcher matcher = TRINITY_PATTERN.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                result.append(Component.literal(text.substring(lastEnd, matcher.start()))
                        .withStyle(ChatFormatting.WHITE));
            }
            result.append(Component.literal(matcher.group()).withStyle(ChatFormatting.GOLD));
            lastEnd = matcher.end();
        }

        if (lastEnd < text.length()) {
            result.append(Component.literal(text.substring(lastEnd)).withStyle(ChatFormatting.WHITE));
        }

        return result;
    }
}
