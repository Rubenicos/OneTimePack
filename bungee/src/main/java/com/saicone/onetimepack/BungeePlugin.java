package com.saicone.onetimepack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

public class BungeePlugin extends Plugin implements Listener, OneTimePack.Provider {

    private static BungeePlugin instance;

    public static BungeePlugin get() {
        return instance;
    }

    public BungeePlugin() {
        new OneTimePack(this);
        instance = this;
    }

    @Override
    public void onLoad() {
        OneTimePack.get().onLoad();
    }

    @Override
    public void onEnable() {
        OneTimePack.get().onEnable();
        this.getProxy().getPluginManager().registerListener(this, this);
        this.getProxy().getPluginManager().registerCommand(this, new BungeeCommand());
    }

    @Override
    public void onDisable() {
        OneTimePack.get().onDisable();
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        OneTimePack.get().getPacketHandler().clear(event.getPlayer().getUniqueId());
    }

    @Override
    public @NotNull File getPluginFolder() {
        return getDataFolder();
    }

    @Override
    public void log(int level, @NotNull String s) {
        switch (level) {
            case 1:
                getLogger().log(Level.SEVERE, s);
                break;
            case 2:
                getLogger().log(Level.WARNING, s);
                break;
            default:
                getLogger().log(Level.INFO, s);
                break;
        }
    }

    @NotNull
    private static BaseComponent[] parseComponent(@NotNull String s) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static class BungeeCommand extends Command {

        public BungeeCommand() {
            super("onetimepack", "onetimepack.use");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
                final long start = System.currentTimeMillis();
                OneTimePack.get().onReload();
                final long time = System.currentTimeMillis() - start;
                sender.sendMessage(parseComponent("&aPlugin successfully reloaded [&f" + time + " ms&a]"));
                return;
            }
            sender.sendMessage(parseComponent("&a&lOneTimePack &e&lv" + BungeePlugin.get().getDescription().getVersion()));
        }
    }
}