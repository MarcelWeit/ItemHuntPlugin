package weitma.itemHuntPlugin.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;

public class HungerListener implements Listener {

    @EventHandler
    public void onGettingHungry(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

}
