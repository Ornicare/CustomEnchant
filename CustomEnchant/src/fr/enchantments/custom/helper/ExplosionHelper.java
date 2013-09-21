package fr.enchantments.custom.helper;

import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;

import fr.enchantments.custom.loader.PluginLoader;

public class ExplosionHelper
{

	/**
	 * State of the next enderdragon portal
	 */
    private static int illegalPortal  = 0;


	public static void doFakeExplosion(Location location, int radius) {
        PacketContainer fakeExplosion = PluginLoader.protocolManager.createPacket(Packets.Server.EXPLOSION);

        fakeExplosion.getDoubles().write(0, location.getX()).write(1, location.getY() + 1.25).write(2, location.getZ());
        fakeExplosion.getFloat().write(0, (float)radius);
        
        //Move players 
//        fakeExplosion.getFloat().write(1, 2F).write(2, 2F).write(3, 2F);
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
	
	public static void megaJump(Location location, int radius, Player player) {
        PacketContainer fakeExplosion = PluginLoader.protocolManager.createPacket(Packets.Server.EXPLOSION);

        fakeExplosion.getDoubles().write(0, location.getX()).write(1, location.getY() + 1.25).write(2, location.getZ());
        fakeExplosion.getFloat().write(0, (float)radius);
        
//        Move players 
        fakeExplosion.getFloat().write(0, 2F).write(2, (float)radius).write(0, 2F);
        try
        {
            PluginLoader.protocolManager.sendServerPacket(player, fakeExplosion);
            location.getWorld().playSound(location, Sound.CAT_MEOW, 10, 1);
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

    public static void playDragonDeath(Location location) {
    	EnderDragon e = (EnderDragon)location.getWorld().spawn(location, EnderDragon.class);
        e.playEffect(EntityEffect.DEATH);
        e.setHealth(0.0D);
        illegalPortal ++;
    }

	public static void playPotionEffect(Location loc, short level) {
//	    PacketConstructor blockBreakConstructor  = PluginLoader.pluginLoader.getProtocolManager().createPacketConstructor(Packets.Server.WORLD_EVENT, 2002, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 35, false);
	         
	    
	    
	    PacketContainer fakeExplosion = PluginLoader.pluginLoader.getProtocolManager().createPacket(61);


	    
//	        PacketContainer packet = blockBreakConstructor.createPacket(61, 2002,loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0);
	        try
	        {
	            for (Player player : PluginLoader.pluginLoader.getServer().getWorld(loc.getWorld().getName()).getPlayers())
	            {

	                if ( player.getLocation().distance(loc) > 100) { continue; }

	        	    fakeExplosion.getSpecificModifier(int.class).
	        	    write(0, 2002).
	    	        write(1, (int) loc.getX()).
	    	        write(2, (int) loc.getY()).
	        	    write(3, (int) loc.getZ());
	    	    fakeExplosion.getSpecificModifier(byte.class).
	    	        write(0, (byte) loc.getY());
	                PluginLoader.protocolManager.sendServerPacket(player, fakeExplosion);
	            }
	        }
	        catch ( Throwable t ) { }

//		location.getWorld().playEffect(location, Effect.POTION_BREAK, 1018);
	}
	
	public static void playBlackHoleEffect(Location location) {
		location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
	}


	/**
	 * Test if the enderdragon portal is leagaly createed.
	 * 
	 * @return
	 */
	public static boolean getPortalLegality() {
		return illegalPortal!=0;
	}


	public static void setPortalLegality() {
		illegalPortal --;
	}

	public static void blowEntities(Entity proj, double d, double distance) {
	    Location loc =proj.getLocation();
//		for (Entity ent : proj.getNearbyEntities(distance, distance, distance))
//        {
//			
//			if(ent instanceof LivingEntity) {
//				Vector entV = ent.getVelocity();
//				Vector diff = ent.getLocation().toVector().subtract(loc.toVector());
//				entV = entV.add(diff.normalize().clone().multiply(force));
//				PluginLoader.pluginLoader.getLogger().log(Level.SEVERE," "+entV.getX()+" "+entV.getY()+" "+entV.getZ());
//				((LivingEntity)ent).setVelocity(entV.clone());
//				
//				
//			}
	    
	    for(Entity ent : proj.getNearbyEntities(distance, distance, distance)) {
//       	 if(ent instanceof LivingEntity) ent.setVelocity(ent.getVelocity().add(ent.getLocation().clone().subtract(proj.getLocation().clone()).toVector().normalize().multiply(1+distance-ent.getLocation().distance(proj.getLocation()))));
	    	 /*if(ent instanceof LivingEntity)*/ ent.setVelocity(ent.getVelocity().add(ent.getLocation().clone().subtract(proj.getLocation().clone()).toVector()/*.normalize()*/.multiply(d)));
	         
	    }

//        }
    }
		
	

}
