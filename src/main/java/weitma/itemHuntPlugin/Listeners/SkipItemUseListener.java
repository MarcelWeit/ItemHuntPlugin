package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class SkipItemUseListener implements Listener {

    private final ItemHuntPlugin plugin;

    public SkipItemUseListener(ItemHuntPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSkipItemUse(PlayerInteractEvent event) {
        if (event.getPlayer() instanceof Player player){
            if(plugin.isChallengeStarted()) {
                ItemStack itemUsed = event.getItem();

                if (itemUsed == null || itemUsed.getType().isAir()) {
                    return;
                }

                if (ChatColor.stripColor(itemUsed.getItemMeta().getDisplayName()).equals("Skip Item")) {
                    player.sendMessage("You used the skip item!");
                    itemUsed.setAmount(itemUsed.getAmount() - 1);
                    plugin.skipItemToCollect(player.getUniqueId());
                }
            }

        }
    }

}
