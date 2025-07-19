package com.Discord.DiscordBot.commands;

import com.Discord.DiscordBot.classes.TyperacerPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.HashMap;

public class TyperacerCommand {
    private static final HashMap<Long, TyperacerPlayer> activeGames = new HashMap<>();

    public TyperacerCommand(SlashCommandInteractionEvent event) {
        long channelId = event.getChannel().getIdLong();

        if (activeGames.containsKey(channelId)) {
            event.reply("A TypeRacer game is already running in this channel!").setEphemeral(true).queue();
            return;
        }

        try {
            TyperacerPlayer game = new TyperacerPlayer(event);
            activeGames.put(channelId, game);
            event.reply("Typeracer game starting in 5 seconds!!!").queue();
        } catch (Exception e) {
            event.reply("Failed to start game: " + e.getMessage()).setEphemeral(true).queue();
        }
    }

    public static CommandData getCommandData() {
        return Commands.slash("typeracer", "Plays a 20 word typeracer");
    }

    public static HashMap<Long, TyperacerPlayer> getActiveGames() {
        return activeGames;
    }

    public static void endGame(long channelId) {
        TyperacerPlayer game = activeGames.remove(channelId);
        if (game != null) {
            game.cleanup();
        }
    }
}