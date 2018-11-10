package com.headfishindustries.milkspray.proxy;

import com.headfishindustries.milkspray.MilkSpray;
import com.headfishindustries.milkspray.entities.EntitySplashMilk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPotion;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		RenderingRegistry.registerEntityRenderingHandler(EntitySplashMilk.class, renderManager -> new RenderSnowball<>(renderManager, MilkSpray.items.milk_bottle, Minecraft.getMinecraft().getRenderItem()));
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		}
}
