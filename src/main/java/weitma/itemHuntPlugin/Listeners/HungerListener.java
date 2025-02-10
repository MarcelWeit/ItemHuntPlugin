package weitma.itemHuntPlugin.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerListener implements Listener {

    // gets registered in StartChallengeCommand depending on the parameter
    @EventHandler
    public void onGettingHungry(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

}
