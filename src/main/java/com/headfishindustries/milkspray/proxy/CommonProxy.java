package com.headfishindustries.milkspray.proxy;

import com.headfishindustries.milkspray.MilkSpray;
import com.headfishindustries.milkspray.entities.EntitySplashMilk;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
	
	public void init(FMLInitializationEvent e) {
		BrewingRecipeRegistry.addRecipe(new ItemStack(Items.SPLASH_POTION), new ItemStack(Items.MILK_BUCKET), new ItemStack(MilkSpray.items.milk_splash));
		BrewingRecipeRegistry.addRecipe(new ItemStack(Items.LINGERING_POTION), new ItemStack(Items.MILK_BUCKET), new ItemStack(MilkSpray.items.milk_splash, 1, 1));
		BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM), new ItemStack(Items.MILK_BUCKET), new ItemStack(MilkSpray.items.milk_bottle));
	}

	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(MilkSpray.class);
		
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation("milk_thrown"), EntitySplashMilk.class, "milk_thrown", id++, MilkSpray.instance, 64, 1, true);
	}

}
