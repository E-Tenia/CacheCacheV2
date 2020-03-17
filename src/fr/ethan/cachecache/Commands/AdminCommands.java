package fr.ethan.cachecache.Commands;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.GameElements.GameCycle;
import fr.ethan.cachecache.Mains.CacheCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                    // paramètres : name , time , limite , location
                    if (sender instanceof Player == true) { if(args.length != 4) { sender.sendMessage("Bonne utilisation : /acc setgame <nom> <durée> <limites>"); return true; }
                        GameConfig.setGame(args[1],args[2],args[3],Bukkit.getPlayerExact(sender.getName()).getLocation());
                    } else if(sender instanceof Player == false) {
                        sender.sendMessage("§4[CacheCache] Seul les Joueurs sont autorisés à exécuter cette commande.");
                    }
                    break;
                case "spawngame":
                    // paramètres : name , player
                    GameConfig.spawnGame(args[1],Bukkit.getPlayerExact(sender.getName()));
                    break;
                case "remgame":
                    // paramètres : name
                    GameConfig.removeGame(args[1]);
                    break;
                case "cancel":
                    if (sender instanceof Player) {
                        if(CacheCache.gameQueue.isEmpty()) {
                            sender.sendMessage(ChatColor.RED + "Il n'y a pas de partie en cours.");
                        } else {
                            if (args[1].equals("all")) {
                                CacheCache.gameQueue.entrySet().forEach(entry -> {
                                    entry.getValue().cancelGame();
                                });
                                sender.sendMessage(ChatColor.GREEN + "Toutes les parties ont été annulées.");
                            } else if (args.length == 1) {
                                sender.sendMessage(ChatColor.RED + "Veuillez indiquer une partie à annuler. (all pour toutes les annuler)");
                            } else if(!args[1].equals("all") || args.length == 2) {
                                CacheCache.gameQueue.get(args[1]).cancelGame();
                                sender.sendMessage(ChatColor.GREEN + "La partie à bien été annulée.");
                            }
                        }
                    } else if(!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "Seul les Joueurs sont autorisés à exécuter cette commande.");
                    }
                    break;
                case "plist":
                    //GameCycle.printPlayerList(args[1],Bukkit.getPlayerExact(sender.getName()));
                    break;
                case "glist":
                    GameConfig.printListGame(GameConfig.listGame(),Bukkit.getPlayerExact(sender.getName()));
                    break;
                case "launch": //TODO : à terminer
                    CacheCache.gameQueue.get(args[1]).lobbyTimer.cancel();
                    CacheCache.gameQueue.get(args[1]).startGame();
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Erreur de commande. Essayez /acc help.");
                    break;
            }
        }
        return false;
    }
}
