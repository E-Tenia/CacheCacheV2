package fr.ethan.cachecache.Utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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

    public static void cchelp(Player sender) {
        if (sender instanceof Player == true) {
            TextComponent helpHeader = new TextComponent("§3-- Commandes des Utilisateurs --\n");

            TextComponent startCommandAleat = new TextComponent("§6/cc start");
            TextComponent startCommandAleat2 = new TextComponent("§r : Lance une partie sur une map aléatoire.\n");
            startCommandAleat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.BLUE + "Exécuter la commande").create()));
            startCommandAleat.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cc start"));

            TextComponent startCommand = new TextComponent("§6/cc start <nom>");
            startCommand.addExtra("§r : Lance une partie sur la map précisée.\n");

            TextComponent helpMessage = new TextComponent();
            helpMessage.addExtra(helpHeader);
            helpMessage.addExtra(startCommandAleat);
            helpMessage.addExtra(startCommandAleat2);
            helpMessage.addExtra(startCommand);

            sender.spigot().sendMessage(helpMessage);
        } else {
            sender.sendMessage("\n§3Commandes des Utilisateurs:\n§6cc start\ncc start <nom de partie>");
        }
    }
}