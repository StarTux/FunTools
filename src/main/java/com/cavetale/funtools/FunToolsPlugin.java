package com.cavetale.funtools;

import org.bukkit.plugin.java.JavaPlugin;

public final class FunToolsPlugin extends JavaPlugin {
    private FunToolsListener listener;

    @Override
    public void onEnable() {
        this.listener = new FunToolsListener(this);
        getServer().getPluginManager().registerEvents(this.listener, this);
    }
}
