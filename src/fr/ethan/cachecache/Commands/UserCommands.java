package fr.ethan.cachecache.Commands;

import fr.ethan.cachecache.Configs.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UserCommands implements CommandExecutor {
    /*private final CacheCache plugin;
	public Commands(CacheCache instance) {
		this.plugin = instance;
	}*/

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        Location spawnPartie = new Location(Bukkit.getWorld("world"),(MainConfig.spawnPosition[0]),(MainConfig.spawnPosition[1]),(MainConfig.spawnPosition[2]));

        if (cmd.getName().equalsIgnoreCase("test")) { //affiche un message si la commande réussie (A RETIRER)
            if(args.length > 0) {
                switch(args[0]) {
                    case "spawn":
                        Bukkit.getPlayerExact(sender.getName()).teleport(spawnPartie);
                }
            }

            sender.sendMessage("La commande de test a fonctionné.");
            return true;
        } else if (cmd.getName().equalsIgnoreCase("cc")) {
            if(args.length > 0) {
                switch(args[0]) {
                    case "infos":
                        //sender.sendMessage("§a==Informations de partie==\n§6Spawn de base : §r" );
                        sender.sendMessage(MainConfig.spawnPosition[0]+","+MainConfig.spawnPosition[1]+","+MainConfig.spawnPosition[2]);
                        //return true;
                        break;
                    default:
                        sender.sendMessage("§4Erreur de commande.");
                        break;
                }
            }
        }
        return false;
    }
}