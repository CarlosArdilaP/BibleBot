package com.biblebot;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.permissions.Permissions;

public class BibleBotCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess,
                environment) -> dispatcher.register(Commands.literal("bible")
                        .requires(source -> source.getServer() == null
                                || !source.getServer().isDedicatedServer()
                                || source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(Commands.literal("pause").executes(BibleBotCommands::pause))
                        .then(Commands.literal("resume").executes(BibleBotCommands::resume))
                        .then(Commands.literal("reload").executes(BibleBotCommands::reload))));
    }

    private static int pause(CommandContext<CommandSourceStack> ctx) {
        if (BibleBotScheduler.isPaused()) {
            ctx.getSource().sendFailure(prefix()
                    .append(Component.literal(BibleBotLang.get("cmd.pause.already_paused"))
                            .withStyle(ChatFormatting.RED)));
        } else {
            BibleBotScheduler.pause();
            ctx.getSource().sendSuccess(() -> prefix()
                    .append(Component.literal(BibleBotLang.get("cmd.pause.success")).withStyle(ChatFormatting.YELLOW)),
                    false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int resume(CommandContext<CommandSourceStack> ctx) {
        if (!BibleBotScheduler.isPaused()) {
            ctx.getSource().sendFailure(prefix()
                    .append(Component.literal(BibleBotLang.get("cmd.resume.already_active"))
                            .withStyle(ChatFormatting.RED)));
        } else {
            BibleBotScheduler.resume();
            ctx.getSource().sendSuccess(() -> prefix()
                    .append(Component.literal(BibleBotLang.get("cmd.resume.success")).withStyle(ChatFormatting.GREEN)),
                    false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int reload(CommandContext<CommandSourceStack> ctx) {
        try {
            BibleBotConfig newConfig = BibleBotConfig.loadStrict();
            BibleBot.CONFIG = newConfig;
            BibleBotLang.load(newConfig.language);
            BibleVerseProvider.load(newConfig.language);
            String msg = BibleBotLang.get("cmd.reload.success", newConfig.language, newConfig.intervalMinutes);
            ctx.getSource().sendSuccess(() -> prefix()
                    .append(Component.literal(msg).withStyle(ChatFormatting.GREEN)), false);
            BibleBot.LOGGER.info("[BibleBot] Configuración recargada — idioma: {}, intervalo: {} minuto(s).",
                    newConfig.language, newConfig.intervalMinutes);
        } catch (Exception e) {
            BibleBot.LOGGER.error("[BibleBot] No se pudo recargar biblebot.json — se mantiene la config actual.", e);
            ctx.getSource().sendFailure(prefix()
                    .append(Component.literal(BibleBotLang.get("cmd.reload.error")).withStyle(ChatFormatting.RED)));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static MutableComponent prefix() {
        return Component.literal("[BibleBot] ").withStyle(ChatFormatting.DARK_AQUA);
    }
}
