package com.blendycat.companies;

import com.blendycat.companies.sql.DBManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by EvanMerz on 8/6/17.
 */
public class Companies extends JavaPlugin {
    public static Economy eco = null;
    public static DBManager db;

    @Override
    public void onEnable(){
        if (!setupEconomy() ) {
            getLogger().info("disabled due to vault not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupConfig();
        saveDefaultConfig();
        saveConfig();

        db = new DBManager(this);
        db.startConnection();
        db.createTables();
    }

    @Override
    public void onDisable(){
        saveConfig();
    }

    public void reload(){
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    private void setupConfig(){
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("db_host", "");
        getConfig().addDefault("db_name", "");
        getConfig().addDefault("db_username","");
        getConfig().addDefault("db_password","");
    }
}
