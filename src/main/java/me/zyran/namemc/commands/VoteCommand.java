package me.zyran.namemc.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.zyran.namemc.NameMC;
import me.zyran.namemc.utils.Callback;
import me.zyran.namemc.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand extends Command {

    private final Gson gson = new Gson();

    public VoteCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String aliases, String[] args) {
        if (!(sender instanceof Player)) {

            if (args.length < 1) {
                sender.sendMessage(NameMC.getMessages().getColouredString("Exceptions.noPlayer"));
                return true;
            }

            Util.getString("https://api.mojang.com/users/profiles/minecraft/" + args[0], new Callback<String>() {
                public void done(String json) {
                    try {
                        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                        if (jsonObject == null) {
                            sender.sendMessage(NameMC.getMessages().getColouredString("Exceptions.noPremiumPlayer")
                                    .replaceAll("<playerName>", args[0])
                            );
                            return;
                        }

                        String serverLinkAPI = NameMC.getConfiguration().getString("Server.Links.server");
                        String serverIP = NameMC.getConfiguration().getString("Server.ip");

                        Util.getString(serverLinkAPI
                                .replaceAll("<server>", serverIP)
                                .replace("<uuid>", Util
                                        .setDashedUUID(
                                                jsonObject
                                                        .get("id")
                                                        .getAsString())
                                        .toString()
                                ), new Callback<String>() {

                            public void done(String response) {
                                boolean hasVoted = Boolean.parseBoolean(response);
                                if (hasVoted) {
                                    sender.sendMessage(NameMC.getMessages().getColouredString("Votes.playerHasVoted")
                                            .replaceAll("<playerName>", args[0]));
                                } else {
                                    sender.sendMessage(NameMC.getMessages().getColouredString("Votes.playerHasNotVoted")
                                            .replaceAll("<playerName>", args[0]));
                                }
                            }

                            public void exception(Exception exception) {
                                System.out.println(exception.getMessage());
                            }

                        });
                    } catch (JsonSyntaxException e) {
                        System.out.println(e.getMessage());
                    }
                }

                public void exception(Exception exception) {
                    System.out.println(exception.getMessage());
                }
            });
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(NameMC.getConfiguration().getString("Server.permission"))){
            player.sendMessage(NameMC.getMessages().getColouredString("General.noPermission"));
            return false;
        }

        if (args.length < 1) {
            sendInfoCommand(player);
            return false;
        }

        if (args[0].equalsIgnoreCase("verify")) {
            Util.getString("https://api.mojang.com/users/profiles/minecraft/" + player.getName(), new Callback<String>() {

                public void done(String response) {
                    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                    if (jsonObject == null) {
                        sender.sendMessage(NameMC.getMessages().getColouredString("Votes.needPremiumUser"));
                        return;
                    }

                    String serverLinkAPI = NameMC.getConfiguration().getString("Server.Links.server");
                    String serverIP = NameMC.getConfiguration().getString("Server.ip");

                    Util.getString(serverLinkAPI
                            .replaceAll("<uuid>", player.getUniqueId().toString())
                            .replaceAll("<server>", serverIP), new Callback<String>() {

                        public void done(String response) {
                            boolean hasVoted = Boolean.parseBoolean(response);
                            if (hasVoted) {
                                sender.sendMessage(NameMC.getMessages().getColouredString("Votes.registeredVote"));
                            } else {
                                sender.sendMessage(NameMC.getMessages().getColouredString("Votes.notRegisteredVote"));
                            }
                        }

                        public void exception(Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                    });
                }

                public void exception(Exception exception) {
                    System.out.println(exception.getMessage());
                }
            });
            return false;
        }
        sendInfoCommand(player);
        return true;
    }

    private void sendInfoCommand(Player player) {
        player.sendMessage(NameMC.getMessages().getColouredString("Votes.voteMessage")
                .replaceAll("<voteCommand>", NameMC.getConfiguration().getColouredString("Commands.checkVote"))
                .replaceAll("<serverIP>", NameMC.getConfiguration().getColouredString("Server.ip"))
                .replaceAll("<serverName>", NameMC.getConfiguration().getColouredString("Server.serverName")));

    }
}