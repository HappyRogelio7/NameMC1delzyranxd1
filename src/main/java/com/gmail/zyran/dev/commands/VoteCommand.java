package com.gmail.zyran.dev.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.gmail.zyran.dev.NameMC;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class VoteCommand extends Command {

    private final Gson gson = new Gson();

    public VoteCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String aliases, String[] args) {
        if (sender instanceof Player) {
            executePlayer((Player) sender, args);
        } else {
            executeConsole((ConsoleCommandSender) sender, args);
        }
        return true;
    }

    private void executeConsole(ConsoleCommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Exceptions.noPlayer"));
            return;
        }

        HttpRequest httpRequest;
        try {
            httpRequest = HttpRequest.get(new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]));
            JsonObject jsonObject = gson.fromJson(httpRequest.body(), JsonObject.class);

            if (jsonObject == null) {
                sender.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Exceptions.noPremiumPlayer")
                        .replaceAll("<playerName>", args[0])
                );
                return;
            }

            String serverLinkAPI = NameMC.getNameMC().getConfigFile() .getString("Server.Links.server");
            String serverIP = NameMC.getNameMC().getConfigFile().getString("Server.ip");

            httpRequest = HttpRequest.get(
                    new URL(serverLinkAPI
                            .replaceAll("<uuid>", jsonObject.get("id").getAsString())
                            .replaceAll("<server>", serverIP)));

            boolean hasVoted = Boolean.parseBoolean(httpRequest.body());

            if (hasVoted) {
                sender.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Votes.playerHasVoted")
                        .replaceAll("<playerName>", args[0]));
            } else {
                sender.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Votes.playerHasNotVoted")
                        .replaceAll("<playerName>", args[0]));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void executePlayer(Player player, String[] args) {
        if (!player.hasPermission(NameMC.getNameMC().getConfigFile().getString("Server.permission"))) {
            player.sendMessage(NameMC.getNameMC().getMessages().getColouredString("General.noPermission"));
            return;
        }

        if (args.length < 1) {
            sendInfoCommand(player);
            return;
        }

        if (args[0].equalsIgnoreCase("verify")) {
            try {
                HttpRequest httpRequest;
                httpRequest = HttpRequest.get(new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName()));

                JsonObject jsonObject = gson.fromJson(httpRequest.body(), JsonObject.class);

                if (jsonObject == null) {
                    player.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Votes.needPremiumUser"));
                    return;
                }

                String serverLinkAPI = NameMC.getNameMC().getConfigFile().getString("Server.Links.server");
                String serverIP = NameMC.getNameMC().getConfigFile().getString("Server.ip");

                httpRequest = HttpRequest.get(new URL(serverLinkAPI
                        .replaceAll("<uuid>", player.getUniqueId().toString())
                        .replaceAll("<server>", serverIP)));

                boolean hasVoted = Boolean.parseBoolean(httpRequest.body());
                if (hasVoted) {
                    player.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Votes.registeredVote"));
                } else {
                    player.sendMessage(NameMC.getNameMC().getMessages() .getColouredString("Votes.notRegisteredVote"));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendInfoCommand(Player player) {
        player.sendMessage(NameMC.getNameMC().getMessages().getColouredString("Votes.voteMessage")
                .replaceAll("<voteCommand>", NameMC.getNameMC().getConfigFile().getColouredString("Commands.checkVote"))
                .replaceAll("<serverIP>", NameMC.getNameMC().getConfigFile().getColouredString("Server.ip"))
                .replaceAll("<serverName>", NameMC.getNameMC().getConfigFile().getColouredString("Server.serverName")));

    }

    private UUID setDashedUUID(String uuidString) {
        if (uuidString != null) {
            String uuid = uuidString.replace("\"", "");
            return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid
                    .substring(12, 16) + "-" + uuid
                    .substring(16, 20) + "-" + uuid.substring(20, 32));
        }
        return null;
    }
}