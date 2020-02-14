package fr.ethan.cachecache.Utils;

import fr.ethan.cachecache.Configs.MainConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Broadcast {
    /*public void broadcast(String message) {
    	broadcastMessage = CacheCache.getServer().broadcastMessage(CacheCache.getDescription().getName() + ": " + message);
    }*/

    public static void log(String message) { //fonction d'affichage en console
        System.out.println("[CacheCache] " + message);
    }

    public static void errorLog(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[CacheCache] " + ChatColor.RED + "ERREUR: " + ChatColor.RED + msg);
        if (MainConfig.reloader != null) {
            MainConfig.reloader.sendMessage(ChatColor.DARK_RED + "[CacheCache] " + ChatColor.RED + "ERREUR: " + ChatColor.RED + msg);
        }
    }
}