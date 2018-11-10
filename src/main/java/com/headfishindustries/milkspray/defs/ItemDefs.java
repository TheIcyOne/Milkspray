package com.headfishindustries.milkspray.defs;

import com.headfishindustries.milkspray.MilkSpray;
import com.headfishindustries.milkspray.items.ItemBottledMilk;
import com.headfishindustries.milkspray.items.ItemMilkSplash;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemDefs {
	public ItemBottledMilk milk_bottle;
	public ItemMilkSplash milk_splash;
	
	public ItemDefs() {
		this.def();
	}
	
	private void def() {
		this.milk_bottle = (ItemBottledMilk) new ItemBottledMilk().setRegistryName(MilkSpray.MODID, "milk_bottle").setUnlocalizedName("bottle_milk");
		this.milk_splash = (ItemMilkSplash) new ItemMilkSplash().setRegistryName(MilkSpray.MODID, "milk_thrown").setUnlocalizedName("milk_thrown");
	}
	
	public void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().register(this.milk_bottle);
		e.getRegistry().register(this.milk_splash);
	}
}
