package com.biblebot;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

public class BibleBotScheduler {

    private static int tickCounter = 0;
    private static volatile boolean paused = false;

    public static boolean isPaused() { return paused; }
    public static void pause()       { paused = true; }
    public static void resume()      { paused = false; }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(BibleBotScheduler::onTick);
        BibleBot.LOGGER.info("Scheduler registrado — intervalo: {} minuto(s) ({} ticks)",
                BibleBot.CONFIG.intervalMinutes, BibleBot.CONFIG.getIntervalTicks());
    }

    private static void onTick(MinecraftServer server) {
        if (paused) return;
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

    // Parses <gold>...</gold> tags embedded in the verse text and applies gold/white coloring.
    private static MutableComponent colorizeText(String text) {
        MutableComponent result = Component.empty();
        final String OPEN = "<gold>";
        final String CLOSE = "</gold>";
        int i = 0;
        while (i < text.length()) {
            int tagStart = text.indexOf(OPEN, i);
            if (tagStart == -1) {
                result.append(Component.literal(text.substring(i)).withStyle(ChatFormatting.WHITE));
                break;
            }
            if (tagStart > i) {
                result.append(Component.literal(text.substring(i, tagStart)).withStyle(ChatFormatting.WHITE));
            }
            int tagEnd = text.indexOf(CLOSE, tagStart + OPEN.length());
            if (tagEnd == -1) {
                result.append(Component.literal(text.substring(tagStart + OPEN.length())).withStyle(ChatFormatting.GOLD));
                break;
            }
            result.append(Component.literal(text.substring(tagStart + OPEN.length(), tagEnd)).withStyle(ChatFormatting.GOLD));
            i = tagEnd + CLOSE.length();
        }
        return result;
    }
}
