package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

//            if(event.getItem() == null){
//                itemUsed = player.getInventory().getItemInMainHand();
//            }

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

            Bukkit.getLogger().info("Item used: " + itemUsedMeta.getCustomModelData());

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
            if(itemUsedMeta.getCustomModelData() == ItemHuntPlugin.UPDRAFT_ITEM) {
                player.getPassengers().forEach(passenger -> passenger.eject());
                Location playerLocation = player.getLocation();
                for(int i = (int) Math.round(playerLocation.getY() + 2); i<180; i++){
                    Location checkLocation = playerLocation.clone().add(0, i - playerLocation.getY(), 0);
                    Bukkit.getLogger().info(String.valueOf((int) Math.round(playerLocation.getY())));
                    Bukkit.getLogger().info(checkLocation.toString());
                    if(!checkLocation.getBlock().getType().isAir()){
                        Bukkit.getLogger().info("Block ungleich der zu suchen: " + (checkLocation.getBlock().getType() != plugin.getItemToCollect(player.getUniqueId())));
                        Bukkit.getLogger().info("Keine Luft: " + (!checkLocation.getBlock().getType().isAir()));
                        player.sendMessage("You can't use the item here because there is a block above you!");
                        return;
                    }
                }
                playerLocation.setY(180);
                Location blockBelow = playerLocation.clone().add(0, -1, 0);
                blockBelow.getBlock().setType(Material.GLASS); // Set the block below to glass

                // Set surrounding blocks to glass
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x != 0 || z != 0) {
                            Location surroundingBlock = blockBelow.clone().add(x, 0, z);
                            surroundingBlock.getBlock().setType(Material.GLASS);
                        }
                    }
                }
                player.setVelocity(player.getVelocity().setY(5));
                if(player.teleport(playerLocation))
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
            }


        }

    }
}
