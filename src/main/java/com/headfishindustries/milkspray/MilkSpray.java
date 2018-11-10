package com.headfishindustries.milkspray;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.headfishindustries.milkspray.defs.ItemDefs;
import com.headfishindustries.milkspray.proxy.CommonProxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid=MilkSpray.MODID, version=MilkSpray.VERSION, acceptedMinecraftVersions="[1.12, 1.13]")
public class MilkSpray {
	public static final String MODID = "milkspray";
	public static final String VERSION = "%gradle.version%";
	public static final String NAME = "Milk Spray";
	 public static final Logger LOGGER = LogManager.getLogger(MODID);
	 
	 @Instance(MODID)
	 public static MilkSpray instance;
	 
	 @SidedProxy(clientSide="com.headfishindustries.milkspray.proxy.ClientProxy", serverSide="com.headfishindustries.milkspray.proxy.CommonProxy")
	 static CommonProxy proxy;
	
	TileEntityBrewingStand t = new TileEntityBrewingStand();
	public static ItemDefs items = new ItemDefs();
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
		
	}
	
	@SubscribeEvent
	public static void itemReg(RegistryEvent.Register<Item> e) {
		items.registerItems(e);
	}
	
	@SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {
		ModelLoader.setCustomModelResourceLocation(items.milk_bottle, 0, new ModelResourceLocation(MODID + ":" + "milk_bottle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(items.milk_splash, 0, new ModelResourceLocation(MODID + ":" + "milk_splash", "inventory"));
		ModelLoader.setCustomModelResourceLocation(items.milk_splash, 1, new ModelResourceLocation(MODID + ":" + "milk_linger", "inventory"));
	}
	
}
