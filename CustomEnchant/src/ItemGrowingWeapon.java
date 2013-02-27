
import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.Item;
import net.minecraft.server.v1_4_R1.ItemStack;
import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.World;

public class ItemGrowingWeapon extends Item 
{
        private int weaponDamage;
        private int growth;

                public ItemGrowingWeapon (int i)
                {
                                super(i);
                                maxStackSize = 1;
                setMaxDurability(250);
                weaponDamage = 4;
                growth = 0;
                }

        public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
        {
                if (growth < 1000)
                        growth++;
        }
        
        public void storeGrowth ( ItemStack itemstack, int growth )
        {
                if (itemstack.getTag() == null)
                {
                        itemstack.setTag(new NBTTagCompound());
                }
                itemstack.getTag().setInt("growth", growth);                
        }

        public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
        {
                        itemstack.damage(1, entityliving1);
                        return true;
        }

                public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
                {
                        itemstack.damage(2, entityliving);
                        return true;
                }

        public boolean isFull3D()
        {
                        return true;
        }

        public int getDamageVsEntity(Entity entity)
        {
                        return weaponDamage + (growth/100);
        }


        public int getMaxItemUseDuration(ItemStack itemstack)
        {
                return 0x11940;
        }
}