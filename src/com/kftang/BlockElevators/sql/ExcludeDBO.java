package com.kftang.BlockElevators.sql;

import org.bukkit.Location;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Kenny Tang (c) 2017
 */
public class ExcludeDBO {
    private Connection connection;

    ExcludeDBO() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        if(!new File("plugins/BlockElevators/exclude.sqlite").exists()) {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/BlockElevators/exclude.sqlite");
            connection.createStatement().executeQuery("");
        } else {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/BlockElevators/exclude.sqlite");
        }
    }

    boolean isExcluded(Location location) {
        return false;
    }

    boolean addExcludedBlock(Location location) {
        return true;
    }
}
