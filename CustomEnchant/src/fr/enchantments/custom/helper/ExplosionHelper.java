package fr.enchantments.custom.helper;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketContainer;

import fr.enchantments.custom.loader.PluginLoader;

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


    public static void doSmoke(Location location, int radius)
    {
        Random lolRandom = new Random();

        for ( int X=-radius; X<radius; X++ )
        {
            for ( int Y=-radius; Y<radius; Y++ )
            {
                for ( int Z=-radius; Z<radius; Z++ )
                {
                    Location newLocation = new Location(location.getWorld(), X+location.getX(), Y+location.getY(), Z+location.getZ());

                    if ( newLocation.getWorld().getBlockAt(newLocation).getTypeId() != 0 ) { continue; }

                    newLocation.getWorld().playEffect(newLocation, Effect.SMOKE, lolRandom.nextInt(9), 5);
                }
            }
        }
    }

}
