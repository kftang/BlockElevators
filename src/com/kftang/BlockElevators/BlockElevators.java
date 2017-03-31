package com.kftang.BlockElevators;

import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Kenny Tang (c) 2017
 */
public class BlockElevators extends JavaPlugin {
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        //Setup config
        List<String> elevatorBlocks = new ArrayList<>();
        elevatorBlocks.add("DIAMOND_BLOCK");
        elevatorBlocks.add("EMERALD_BLOCK");
        getConfig().addDefault("elevatorAllowableBlocks", elevatorBlocks);
        getConfig().addDefault("elevatorMaxDistance", -1);
        getConfig().addDefault("elevatorCooldown", -1);
        getConfig().addDefault("elevatorMaxAllowed", -1);
        getConfig().addDefault("elevatorEnableCustomAmounts", false);
        getConfig().options().copyDefaults(true);
        this.saveConfig();

        //Register Events
        getServer().getPluginManager().registerEvents(new ElevatorListener(getConfig()), this);

        //Register Commands
        getCommand("elevator").setExecutor(new CommandElevator(getServer()));
    }
}
