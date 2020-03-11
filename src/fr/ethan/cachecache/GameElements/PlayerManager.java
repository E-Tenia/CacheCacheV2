package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.InventorySerializer;
import org.bukkit.entity.Player;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.*;

import static fr.ethan.cachecache.Utils.InventorySerializer.stringToInventory;

public class PlayerManager {
    private static CacheCache plugin = CacheCache.plugin;

    public static void saveInventory(Player p) {
        JSONObject userDataObject = new JSONObject();
        userDataObject.put("name", p.getName());
        userDataObject.put("inventory" , InventorySerializer.inventoryToString(p.getInventory()) );

        //Write JSON file
        try (FileWriter file = new FileWriter(plugin.getDataFolder() + File.separator + "usercaches"+ File.separator + p.getUniqueId() + ".json")) {
            file.write(userDataObject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //on vide l'inventaire
        p.getInventory().clear();
    }

    public static void restoreInventory(Player p) throws Exception {
        FileReader reader = new FileReader(plugin.getDataFolder() + File.separator + "usercaches"+ File.separator + p.getUniqueId() + ".json");
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        String serializedInventory = (String) jsonObject.get("inventory");

        stringToInventory(serializedInventory,p.getInventory());
    }
}
