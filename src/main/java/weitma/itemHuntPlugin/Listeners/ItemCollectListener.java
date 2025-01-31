package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.TeamManager;

public class ItemCollectListener implements Listener {

    private final ItemHuntPlugin plugin;

    public ItemCollectListener(ItemHuntPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event){
        if(plugin.isChallengeStarted()) {
            if (event.getEntity() instanceof Player player) {
                Material itemPickedUp = event.getItem().getItemStack().getType();
                checkMaterial(itemPickedUp, player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(plugin.isChallengeStarted()) {
            if (event.getWhoClicked() instanceof Player player) {
                if(event.getCurrentItem() != null){
                    Material itemPickedUp = event.getCurrentItem().getType();
                    checkMaterial(itemPickedUp, player);
                }
            }
        }
    }

    public void checkMaterial(Material itemPickedUp, Player player){
        Material itemPlayerHasToCollect = plugin.getItemToCollect(TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId()));

        if (itemPickedUp == itemPlayerHasToCollect) {
            plugin.successfullPickup(player, itemPickedUp, false, false);
        }
    }

}
