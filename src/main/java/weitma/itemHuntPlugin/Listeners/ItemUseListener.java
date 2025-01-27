package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class ItemUseListener implements Listener {

    private final ItemHuntPlugin plugin;

    public ItemUseListener(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSkipItemUse(PlayerInteractEvent event) {
        if (event.getPlayer() instanceof Player player) {
            ItemStack itemUsed = event.getItem();
            ItemMeta itemUsedMeta;

            try {
                assert itemUsed != null;
                itemUsedMeta = itemUsed.getItemMeta();
            } catch(NullPointerException e){
                Bukkit.getLogger().info("ItemMeta is null");
                return;
            }

            if (itemUsed.getType().isAir() || itemUsedMeta == null) {
                return;
            }

            if(!itemUsedMeta.hasCustomModelData()){
                return;
            }

            if (itemUsedMeta.getCustomModelData() == ItemHuntPlugin.SKIPITEM_ID) {

                event.setCancelled(true);

                if (!plugin.isChallengeStarted() || plugin.isChallengeFinished()) {
                    player.sendMessage(ChatColor.RED + "The challenge has not started yet or has already finished!");
                    return;
                }

                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(ChatColor.RED + "Your inventory is full! Make some room before using skip item!");
                    return;
                }
                itemUsed.setAmount(itemUsed.getAmount() - 1);
                plugin.skipItemToCollect(player.getUniqueId());
            }
            if(itemUsedMeta.getCustomModelData() == ItemHuntPlugin.BACKPACK_ID){
                event.setCancelled(true);
                player.openInventory(plugin.getBackpackInventory(player.getUniqueId()));
            }
            if(itemUsedMeta.getCustomModelData() == ItemHuntPlugin.SPECIAL_ROCKET_ID){
                Bukkit.getLogger().info("Special Rocket used");
                ItemStack specialRocket = plugin.getSpecialRocket();
                int slot = event.getPlayer().getInventory().getHeldItemSlot();
                Bukkit.getLogger().info(slot + "");
                specialRocket.setAmount(2);
                event.getPlayer().getInventory().setItem(slot, specialRocket);
            }

        }

    }
}
