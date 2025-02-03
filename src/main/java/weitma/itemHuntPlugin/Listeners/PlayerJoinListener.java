package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import weitma.itemHuntPlugin.ItemHuntPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        List<NamespacedKey> recipeKeys = new ArrayList<>();

        Bukkit.getServer().recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                recipeKeys.add(shapelessRecipe.getKey());
            } else if (recipe instanceof ShapedRecipe shapedRecipe) {
                recipeKeys.add(shapedRecipe.getKey());
            }
        });

        player.discoverRecipes(recipeKeys);

        player.sendMessage(ChatColor.GREEN + "You have discovered all recipes! (most of them)");

        ItemStack teamItem = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta teamItemMeta = teamItem.getItemMeta();
        teamItemMeta.setDisplayName(ChatColor.RED + "Open Teams");
        teamItemMeta.setCustomModelData(ItemHuntPlugin.TEAM_ITEM);
        teamItem.setItemMeta(teamItemMeta);
        if(player.getInventory().getItem(4) == null){
            player.getInventory().setItem(4, teamItem);
        } else {
            player.getInventory().addItem(teamItem);
        }

    }
}
