package weitma.itemHuntPlugin.Listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Iterator;
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

        TextComponent message = new TextComponent("Spenden: ");
        TextComponent link = new TextComponent(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Paypal");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.paypal.me/marcelweithoener"));
        message.addExtra(link);
        player.spigot().sendMessage(message);

    }
}
