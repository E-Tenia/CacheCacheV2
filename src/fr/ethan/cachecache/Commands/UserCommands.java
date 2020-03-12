package fr.ethan.cachecache.Commands;

import com.sun.istack.internal.NotNull;
import fr.ethan.cachecache.GameElements.GameCycle;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.Broadcast;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommands implements CommandExecutor {
    private static CacheCache plugin = CacheCache.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String msg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cc")) {
            if(args.length > 0) {
                switch(args[0]) {
                    case "help":
                        Broadcast.cchelp(Bukkit.getPlayerExact(sender.getName()));
                        break;
                    case "start":
                        String name;
                        if(args.length == 1) { name = null; }
                        else if(args.length == 2){ name = args[1];
                            if(CacheCache.gameQueue.containsKey(args[1])){
                                sender.sendMessage(ChatColor.RED + "La partie existe déjà");
                            return true;
                            }
                        }
                        else {sender.sendMessage("Erreur, trop d'arguments : /cc start <name>"); return true;}
                        GameCycle game = new GameCycle(name);

                        CacheCache.gameQueue.put(game.getName(),game);
                        game.runTaskTimer(plugin,0,20);
                        break;
                    case "gamequeue":
                        CacheCache.printGameQueue(Bukkit.getPlayerExact(sender.getName()));
                        break;
                    case "join":
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "Seul les Joueurs sont autorisés à exécuter cette commande.");
                            return true;
                        } else {
                            if(args.length != 2) { sender.sendMessage(ChatColor.RED + "Bonne utilisation : /cc join <nom de partie>");
                            } else {
                                if(CacheCache.gameQueue.get(args[1]).lobbyTime > 0) {
                                    if(!CacheCache.inGame.contains(Bukkit.getPlayerExact(sender.getName()))) {
                                        CacheCache.inGame.add(Bukkit.getPlayerExact(sender.getName()));
                                        CacheCache.gameQueue.get(args[1]).addPlayer(Bukkit.getPlayerExact(sender.getName()));
                                    }
                                    else{
                                        sender.sendMessage(ChatColor.RED + "Vous êtes déjà en jeu");
                                    }
                                }
                                else {
                                    sender.sendMessage(ChatColor.RED + "La partie a déjà commencé.");
                                }
                            }
                        }
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Erreur de commande. Essayez /cc help.");
                        break;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Erreur de commande. Essayez /cc help.");
            }
        }
        return false;
    }
}