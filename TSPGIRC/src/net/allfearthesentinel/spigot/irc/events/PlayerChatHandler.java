package net.allfearthesentinel.spigot.irc.events;

import net.allfearthesentinel.spigot.irc.Plugin;
import net.allfearthesentinel.spigot.irc.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class PlayerChatHandler implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (Plugin.getInstance().bot.ignoredPlayers.contains(player.getName())) {
            return;
        }

        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        String channel = Plugin.getInstance().getConfig().getString("irc.channel");

        Plugin.getInstance().bot.sendMessage(channel, String.format(
                "[%s] <%s%s" + Colors.NORMAL + "> %s",
                Utility.ircColourWorld(player.getWorld()),
                (player.isOp() ? Colors.CYAN : ""),
                playerName,
                message
        ));
    }
}
