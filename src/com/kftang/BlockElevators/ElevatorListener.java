package com.kftang.BlockElevators;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.*;

/**
 * Kenny Tang (c) 2017
 * TODO: Cooldowns
 * TODO: Command
 * TODO: Excludes
 * TODO: Use the elevator state
 */
public class ElevatorListener implements Listener {
    private List<String> elevatorBlocks = new ArrayList<>();
    private Map<UUID, Long> elevatorCooldowns;
    private final int maxDistance;
    private enum Direction {UP, DOWN}
    private enum ElevateState {INVALID, PERMISSION, VALID, NONELEVATOR}

    ElevatorListener(FileConfiguration config) {
        //Cooldowns per player
        elevatorCooldowns = new HashMap<>();
        //Get the allowed elevator blocks from the config
        elevatorBlocks = config.getStringList("elevatorAllowableBlocks");
        //Get the max distance of the elevator from the config
        maxDistance = config.getInt("elevatorMaxDistance");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getY() < event.getTo().getY() && event.getPlayer().getLocation().getY() - ((int)event.getPlayer().getLocation().getY()) < .1) {
            elevate(Direction.UP, event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            elevate(Direction.DOWN, event.getPlayer());
        }
    }

    private ElevateState elevate(Direction direction, Player player) {
        //Check for the elevator block
        //If the block under the player is one of the elevator blocks
        if (isElevatorBlock(player.getLocation().subtract(0, 1, 0).getBlock())) {
            //Check Permissions
            if(!player.hasPermission("blockelevators.elevate")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use elevators!"); //TODO: Move this line maybe?
                return ElevateState.PERMISSION;
            }
            //Get the next block
            Location nextElevator = findNextElevator(player.getLocation(), direction);
            if(nextElevator == null)
                return ElevateState.INVALID;
            player.teleport(nextElevator);
            //Make the cool sound
            player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1, 0);
            return ElevateState.VALID;
        }
        return ElevateState.NONELEVATOR;
    }

    private Location findNextElevator(Location location, Direction direction) {
        //Start one block above player or 2 below (since we need to go below the elevator block)
        int originaloffset = direction == Direction.UP ? 1 : -2;
        int nextamt = direction == Direction.UP ? 1 : -1;
        //Iterator for finding the next elevator block
        Location blockIterator = location.clone().add(0, originaloffset, 0);
        //Current block is not an elevator block or is an elevator but does not have space and we have not reached max distance
        for (int i = 0; (!isElevatorBlock(blockIterator.getBlock()) || isElevatorBlock(blockIterator.getBlock()) && !hasSpace(blockIterator)) && (i < maxDistance || maxDistance == -1); i++) {
            blockIterator.add(0, nextamt, 0);
            if (blockIterator.getY() > 256 || blockIterator.getY() < 1)
                return null;
        }
        if (isElevatorBlock(blockIterator.getBlock()))
            return blockIterator.add(0, 1, 0);
        else
            return null;
    }

    private boolean hasSpace(Location location) {
        //If blocks on top of the elevator block are not both air
        Location temp = location.clone();
        return temp.add(0, 1, 0).getBlock().getType().equals(Material.AIR) && temp.add(0, 1, 0).getBlock().getType().equals(Material.AIR);
    }

    private boolean isElevatorBlock(org.bukkit.block.Block block) {
        return elevatorBlocks.contains(block.getType().toString());
    }
}
