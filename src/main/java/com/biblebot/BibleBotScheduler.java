package com.biblebot;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

public class BibleBotScheduler {

    // Intervalo por defecto: 5 minutos (20 ticks/segundo × 60 × 5)
    private static final int INTERVAL_TICKS = 20 * 60 * 5;
    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(BibleBotScheduler::onTick);
        BibleBot.LOGGER.info("Scheduler de BibleBot registrado. Intervalo: {} ticks ({} minutos)",
                INTERVAL_TICKS, INTERVAL_TICKS / (20 * 60));
    }

    private static void onTick(MinecraftServer server) {
        if (server.getPlayerList().getPlayers().isEmpty()) return;

        tickCounter++;
        if (tickCounter >= INTERVAL_TICKS) {
            tickCounter = 0;
            sendVerse(server);
        }
    }

    private static void sendVerse(MinecraftServer server) {
        BibleVerse verse = BibleVerseProvider.getRandom();

        MutableComponent message = Component.literal("✝ [BibleBot] ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(verse.reference()).withStyle(ChatFormatting.AQUA))
                .append(Component.literal(" — ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("\"" + verse.text() + "\"").withStyle(ChatFormatting.WHITE));

        server.getPlayerList().getPlayers().forEach(player ->
                player.sendSystemMessage(message)
        );

        BibleBot.LOGGER.info("[BibleBot] Versículo enviado: {}", verse.reference());
    }
}
