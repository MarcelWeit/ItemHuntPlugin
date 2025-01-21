package weitma.itemHuntPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import weitma.itemHuntPlugin.Commands.StartChallengeCommand;

public final class ItemHuntPlugin extends JavaPlugin {

    private boolean challengeStarted;

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("startchallenge").setExecutor(new StartChallengeCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
    }

    public void setChallengeStarted(boolean challengeStarted) {
        this.challengeStarted = challengeStarted;
    }
}
