package fr.enchantments.custom.helper;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketContainer;
import fr.enchantments.custom.loader.PluginLoader;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ExplosionHelper
{

    public static void doFakeExplosion(Location location, int radius) {
        PacketContainer fakeExplosion = PluginLoader.protocolManager.createPacket(Packets.Server.EXPLOSION);

        fakeExplosion.getDoubles().write(0, location.getX()).write(1, location.getY() + 1.25).write(2, location.getZ());
        fakeExplosion.getFloat().write(0, (float)radius);

        try
        {
            for (Player player : PluginLoader.pluginLoader.getServer().getWorld(location.getWorld().getName()).getPlayers())
            {

                if ( player.getLocation().distance(location) > 100) { continue; }

                PluginLoader.protocolManager.sendServerPacket(player, fakeExplosion);
            }
            location.getWorld().playSound(location, Sound.EXPLODE, 10, 1);
        }
        catch ( Throwable t ) { }
    }

}
