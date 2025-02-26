package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class NightListener implements Listener {

    @EventHandler
    public void onNightChange(TimeSkipEvent event){
        if(event.getSkipReason().equals(TimeSkipEvent.SkipReason.NIGHT_SKIP)){
            event.getWorld().setTime(0);
        }
    }
}
