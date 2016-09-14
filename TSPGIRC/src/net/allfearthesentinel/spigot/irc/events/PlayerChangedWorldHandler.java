package net.allfearthesentinel.spigot.irc.events;

import net.allfearthesentinel.spigot.irc.Plugin;
import net.allfearthesentinel.spigot.irc.Utility;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class PlayerChangedWorldHandler implements Listener {
    @EventHandler
    public void onWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (Plugin.getInstance().bot.ignoredPlayers.contains(player.getName())) {
            return;
        }

        String playerName = event.getPlayer().getName();
        World from = event.getFrom();

        String channel = Plugin.getInstance().getConfig().getString("irc.channel");

        Plugin.getInstance().bot.sendMessage(channel, String.format(
                Colors.YELLOW + "%%-%% " + Colors.NORMAL + "%s%s" + Colors.NORMAL + " moved from world %s to %s",
                (player.isOp() ? Colors.CYAN : ""),
                playerName,
                Utility.ircColourWorld(from),
                Utility.ircColourWorld(player.getWorld())
        ));
    }
}
