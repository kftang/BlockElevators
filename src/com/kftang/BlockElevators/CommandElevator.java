package com.kftang.BlockElevators;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Kenny Tang (c) 2017
 */
class CommandElevator implements CommandExecutor {
    private Server server;
    CommandElevator(Server server) {
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
