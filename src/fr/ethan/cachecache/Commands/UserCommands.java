package fr.ethan.cachecache.Commands;

import fr.ethan.cachecache.Listeners.GameCycle;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.Broadcast;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
                        if (sender instanceof Player == true) { //TODO : faire ça en methode de broadcast pour épurer
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
                        break;
                    case "start": //TODO : à revoir pour optimiser la structure de donnée
                        String name;
                        if(args.length == 1) { name = null; } else { name = args[1]; }
                        GameCycle game = new GameCycle(name);
                        game.runTaskTimer(plugin,0,20);
                        break;
                    case "join":
                        //TODO : rejoindre par identifiant de partie
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