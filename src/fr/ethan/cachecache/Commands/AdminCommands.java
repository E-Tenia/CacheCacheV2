package fr.ethan.cachecache.Commands;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.Listeners.Game;
import fr.ethan.cachecache.Listeners.GameCycle;
import fr.ethan.cachecache.Listeners.PlayerManager;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.Broadcast;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {
    private static CacheCache plugin = CacheCache.plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("acc")) {
            switch(args[0]) {
                case "setgame":
                    if (sender instanceof Player == true) { if(args.length != 4) { sender.sendMessage("Bonne utilisation : /acc setgame <nom> <durée> <limites>"); return true; }
                        // paramètres : name , time , limite , location
                        Game.setGame(args[1],args[2],args[3],Bukkit.getPlayerExact(sender.getName()).getLocation());
                    } else if(sender instanceof Player == false) {
                        sender.sendMessage("§4[CacheCache] Seul les Joueurs sont autorisés à exécuter cette commande.");
                    }
                    break;
                case "spawngame":
                    // paramètres : name , player
                    Game.spawnGame(args[1],Bukkit.getPlayerExact(sender.getName()));
                    break;
                case "remgame":
                    // paramètres : name
                    Game.removeGame(args[1]);
                    break;
                case "cancel":
                    break;
                case "clear":
                    PlayerManager.saveInventory(Bukkit.getPlayerExact(sender.getName()));
                    break;
                case "restore":
                    try {
                        PlayerManager.restoreInventory(Bukkit.getPlayerExact(sender.getName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "gamelist":
                    GameConfig.printListGame(GameConfig.listGame(),Bukkit.getPlayerExact(sender.getName()));
                    break;
                default:
                    sender.sendMessage("§4Erreur de commande. Essayez /acc help.");
                    break;
            }
        }
        return false;
    }
}
