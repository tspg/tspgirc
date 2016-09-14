package net.allfearthesentinel.spigot.irc;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.allfearthesentinel.spigot.irc.commands.IrcCommand;
import net.allfearthesentinel.spigot.irc.commands.MeCommand;
import net.allfearthesentinel.spigot.irc.events.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by sean on 12/09/16.
 */
public class Plugin extends JavaPlugin {
    private static Plugin instance = null;
    public Logger logger;
    public Configuration config;
    public Bot bot;
    public BukkitTask tasks;
    private List<String> broadcastQueue;
    private Thread botThread;
    public boolean pluginUnloading = false;

    @Override
    public void onEnable() {
        instance = this;

        logger = this.getLogger();
        logger.info("Hello!");

        config = this.getConfig();
        initConfig();

        broadcastQueue = new ArrayList<String>();

        tasks = new Tasks(this).runTaskTimer(this, 0, 1);

        botThread = new Thread(() -> bot = new Bot());
        botThread.setName("TSPGIRC Bot Thread");
        botThread.run();

        getServer().getPluginManager().registerEvents(new PlayerChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerChangedWorldHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(), this);

        getCommand("irc").setExecutor(new IrcCommand());
        getCommand("me").setExecutor(new MeCommand());
    }

    @Override
    public void onDisable() {
        pluginUnloading = true;
        String className = this.getClass().getCanonicalName();
        bot.quitServer(className + ".onDisable() called - Plugin disabled");
    }

    public void reload() {
        String oldNick = getConfig().getString("irc.nickname");
        String oldNSUser = getConfig().getString("irc.nickserv.username");
        String oldNSPass = getConfig().getString("irc.nickserv.password");

        reloadConfig();

        String newNick = getConfig().getString("irc.nickname");
        String newNSUser = getConfig().getString("irc.nickserv.username");
        String newNSPass = getConfig().getString("irc.nickserv.password");

        if (!newNick.equals(oldNick)) {
            bot.changeNick(newNick);
        }

        if (!newNSUser.equals(oldNSUser) || !newNSPass.equals(oldNSPass)) {
            bot.sendRawLine("NICKSERV IDENTIFY " + newNSUser + " " + newNSPass);
        }
    }

    /**
     * Sets configuration defaults.
     */
    private void initConfig() {
        config.addDefault("irc.verbose", false);
        config.addDefault("irc.server", "irc.example.com");
        config.addDefault("irc.port", 6667);
        config.addDefault("irc.ssl", false);
        config.addDefault("irc.nickname", "TSPGIRC");
        config.addDefault("irc.ident", "tspg");
        config.addDefault("irc.realname", "The Sentinel's Playground");
        config.addDefault("irc.channel", "#tspg-bottest");
        config.addDefault("irc.channel_key", "");
        config.addDefault("irc.verify_ssl", false);

        config.addDefault("irc.nickserv.username", "");
        config.addDefault("irc.nickserv.password", "");

        config.options().copyDefaults(true);
        saveConfig();
    }

    public static Plugin getInstance() {
        return instance;
    }

    public void addToBroadcastQueue(String message) {
        broadcastQueue.add(message);
    }

    public List<String> getBroadcastQueue() {
        return broadcastQueue;
    }
}
