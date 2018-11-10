package com.headfishindustries.milkspray.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBottledMilk extends Item{
	public ItemBottledMilk() {
		this.setMaxStackSize(8);
		this.setCreativeTab(CreativeTabs.BREWING);
	}
	
	 @SideOnly(Side.CLIENT)
	    public ItemStack getDefaultInstance()
	    {
	        return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.WATER);
	    }

	 	/**
	 	 * Shamelessly stolen from ItemPotion then tweaked mercilessly.
	 	 **/
	 
	    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	    {
	        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

	        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
	        {
	            stack.shrink(1);
	        }

	        if (entityplayer instanceof EntityPlayerMP)
	        {
	            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
	        }

	        if (!worldIn.isRemote)
	        {
	        	entityplayer.clearActivePotions();
	        }

	        if (entityplayer != null)
	        {
	            entityplayer.addStat(StatList.getObjectUseStats(this));
	        }

	        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
	        {
	            if (stack.isEmpty())
	            {
	                return new ItemStack(Items.GLASS_BOTTLE);
	            }

	            if (entityplayer != null)
	            {
	                entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
	            }
	        }

	        return stack;
	    }

	    public int getMaxItemUseDuration(ItemStack stack)
	    {
	        return 32;
	    }

	    public EnumAction getItemUseAction(ItemStack stack)
	    {
	        return EnumAction.DRINK;
	    }

	    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	    {
	        playerIn.setActiveHand(handIn);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	    }
}
