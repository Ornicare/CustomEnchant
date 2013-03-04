package fr.enchantments.custom.runnables;

import fr.enchantments.custom.storage.Storage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableDeadSnowBallRemover extends BukkitRunnable
{
    private Player player;
    public RunnableDeadSnowBallRemover(Player player) { this.player = player; }
    public void run() { if ( Storage.SNOWBALLOWNER.containsKey(player) ) { Storage.SNOWBALLOWNER.remove(player); } }
}