package fr.ethan.cachecache.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Broadcast {
    public static void broadcaster(String message) {
        Bukkit.broadcastMessage("§3[CacheCache] §r: " + message);
    }

    public static void log(String message) { //fonction d'affichage en console
        System.out.println("[CacheCache] " + message);
    }

    public static void errorLog(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[CacheCache] " + ChatColor.RED + "ERREUR: " + ChatColor.RED + msg);
    }
}