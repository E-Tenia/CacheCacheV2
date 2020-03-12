package fr.ethan.cachecache.Commands;

import fr.ethan.cachecache.GameElements.GameCycle;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.Broadcast;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommands implements CommandExecutor {
    private static CacheCache plugin = CacheCache.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cc")) {
            if(args.length > 0) {
                switch(args[0]) {
                    case "help":
                        Broadcast.cchelp(Bukkit.getPlayerExact(sender.getName()));
                        break;
                    case "start": //TODO : à revoir pour optimiser la structure de donnée
                        String name;
                        if(args.length == 1) { name = null; } else { name = args[1]; }
                        GameCycle game = new GameCycle(name);

                        CacheCache.gameQueue.put(game.getName(),game);
                        game.runTaskTimer(plugin,0,20);
                        break;
                    case "join":
                    	if(!(sender instanceof Player)) {
                    		sender.sendMessage("§4[CacheCache] Seul les Joueurs sont autorisés à exécuter cette commande.");
                    		return true;
                    	}
                    	for(GameCycle gm : CacheCache.gamelist) {
                    		if(gm.name == args[1]) {
                    			if(gm.lobbyTime > 0) {
                    				gm.addPlayer(Bukkit.getPlayerExact(sender.getName()));
                    			}
                    			else {
                    				sender.sendMessage("La partie a déjà commencé.");
                    			}
                    		}
                    	}
                        break;
                    default:
                        sender.sendMessage("§4Erreur de commande. Essayez /cc help.");
                        break;
                }
            } else {
                sender.sendMessage("§4Erreur de commande. Essayez /cc help.");
            }
        }
        return false;
    }
}