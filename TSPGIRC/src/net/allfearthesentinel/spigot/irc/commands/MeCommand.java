package net.allfearthesentinel.spigot.irc.commands;

import net.allfearthesentinel.spigot.irc.Plugin;
import net.allfearthesentinel.spigot.irc.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class MeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Player player = null;
        String name = "";
        String action = "";

        for (String a : args) {
            action += a + " ";
        }

        if (sender instanceof Player) {
            player = (Player) sender;
            name = player.getDisplayName();
        } else {
            name = "Console";
        }

        Bukkit.getServer().broadcastMessage(String.format(
                "§6* §e%s §6%s",
                name,
                action

        ));

        String channel = Plugin.getInstance().getConfig().getString("irc.channel");

        if (sender instanceof Player) {
            if (Plugin.getInstance().bot.ignoredPlayers.contains(player.getName())) {
                return true;
            }

            Plugin.getInstance().bot.sendMessage(channel, String.format(
                    "[%s] * %s%s" + Colors.NORMAL + " %s",
                    Utility.ircColourWorld(player.getWorld()),
                    (player.isOp() ? Colors.CYAN : ""),
                    sender.getName(),
                    action
            ));
        }


        return true;
    }
}
