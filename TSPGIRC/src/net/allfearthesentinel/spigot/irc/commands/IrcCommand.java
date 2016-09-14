package net.allfearthesentinel.spigot.irc.commands;

import net.allfearthesentinel.spigot.irc.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jibble.pircbot.User;

/**
 * Created by sean on 13/09/16.
 */
public class IrcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin plugin = Plugin.getInstance();
        if (args.length < 1) {
            return doHelp(sender);
        }

        String argstr = "";
        for (int i = 1; i < args.length; i++) {
            String a = args[i];
            argstr += a + " ";
        }

        switch (args[0]) {
            case "help":
            case "h":
            case "?":
                return doHelp(sender);

            case "info":
                int numOps = 0;
                int numHops= 0;
                int numVoice = 0;
                int numNormal = 0;
                for (User user : plugin.bot.getUsers(plugin.getConfig().getString("irc.channel"))) {
                    if (user.isOp()) {
                        numOps++;
                    } else if (user.isHalfOp()) {
                        numHops++;
                    } else if (user.hasVoice()) {
                        numVoice++;
                    } else {
                        numNormal++;
                    }
                }
                sender.sendMessage(String.format(
                        "§6§l[§eTSPGIRC§6]§r §6Nickname: §f%s §6NickServ: §f%s",
                        plugin.bot.getName(),
                        plugin.getConfig().getString("irc.nickserv.username")
                ));

                sender.sendMessage(String.format(
                        "§6§l[§eTSPGIRC§6]§r §6Server: §f%s§e/§f%d §6SSL: %s",
                        plugin.bot.getServer(),
                        plugin.bot.getPort(),
                        (plugin.bot.useSSL() ? "§aYes" : "§cNo")
                ));

                sender.sendMessage(String.format(
                        "§6§l[§eTSPGIRC§6]§r §6Channel: §f%s §6Users: §f%d §6(§a%d§6/§d%d§6/§e%d§6/§f%d§6)",
                        plugin.getConfig().getString("irc.channel"),
                        plugin.bot.getUsers(plugin.getConfig().getString("irc.channel")).length,
                        numOps, numHops, numVoice, numNormal
                ));

                return true;

            case "ignoreuser":
                if (!sender.hasPermission("tspg.irc.ignoreuser")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                if (plugin.bot.ignoredUsers.contains(args[1])) {
                    plugin.bot.ignoredUsers.remove(args[1]);
                    sender.sendMessage("\u00A7aUser is no longer being ignored.");
                    return true;
                }

                plugin.bot.ignoredUsers.add(args[1]);
                sender.sendMessage("\u00A7aUser is now being ignored.");

                return true;

            case "ignoreplayer":
                if (!sender.hasPermission("tspg.irc.ignoreplayer")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                if (plugin.bot.ignoredPlayers.contains(args[1])) {
                    plugin.bot.ignoredPlayers.remove(args[1]);
                    sender.sendMessage("\u00A7aPlayer is no longer being ignored.");
                    return true;
                }

                plugin.bot.ignoredPlayers.add(args[1]);
                sender.sendMessage("\u00A7aPlayer is now being ignored.");

                return true;

            case "reload":
                if (!sender.hasPermission("tspg.irc.reload")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                plugin.reload();
                sender.sendMessage("\u00A7aPlugin reloaded.");

                return true;

            case "reconnect":
                if (!sender.hasPermission("tspg.irc.reconnect")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                Plugin.getInstance().bot.quitServer("Reconnect issued by " + sender.getName());
                sender.sendMessage("\u00A7aBot reconnecting.");

                return true;

            case "act":
                if (!sender.hasPermission("tspg.irc.say")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                String channel = Plugin.getInstance().getConfig().getString("irc.channel");
                plugin.bot.sendAction(channel, argstr);
                sender.sendMessage("\u00A7aAction sent to " + channel + ".");

                return true;

            case "say":
                if (!sender.hasPermission("tspg.irc.say")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                channel = Plugin.getInstance().getConfig().getString("irc.channel");
                plugin.bot.sendMessage(channel, argstr);
                sender.sendMessage("\u00A7aMessage sent to " + channel + ".");

                return true;

            case "raw":
                if (!sender.hasPermission("tspg.irc.raw")) {
                    sender.sendMessage("\u00A7cInsufficient permissions.");
                    return true;
                }

                plugin.bot.sendRawLineViaQueue(argstr);
                sender.sendMessage("\u00A7aRaw line sent.");

                return true;

            default:
                sender.sendMessage("§cUnknown command: see /irc help.");

                return true;
        }

        //return true;
    }

    public boolean doHelp(CommandSender sender) {
        sender.sendMessage("§6§l§n--- TSPG IRC Help ---§r");

        if (sender.hasPermission("tspg.irc.say"))
            sender.sendMessage("§6§lact §rSend a CTCP ACTION to the channel via the bot.§r");

        if (sender.hasPermission("tspg.irc.ignoreuser"))
            sender.sendMessage("§6§lignoreuser §r§6<nick> §rIgnore an IRC user.§r");

        if (sender.hasPermission("tspg.irc.ignoreplayer"))
            sender.sendMessage("§6§lignoreplayer §r§6<name> §rIgnore a player user.§r");

        sender.sendMessage("§6§linfo §rShow bot information.§r");

        if (sender.hasPermission("tspg.irc.raw"))
            sender.sendMessage("§6§lraw §rSend a raw message.§r");

        if (sender.hasPermission("tspg.irc.reconnect"))
            sender.sendMessage("§6§lreconnect §rReconnect the bot.§r");

        if (sender.hasPermission("tspg.irc.reload"))
            sender.sendMessage("§6§lreload §rReload the config.§r");

        if (sender.hasPermission("tspg.irc.say"))
            sender.sendMessage("§6§lsay §rSend a message to the bot channel via the bot.§r");

        return true;
    }
}
