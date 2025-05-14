package com.pra;

import com.pra.item.ModItems;
import com.pra.models.ModComponents;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Practice implements ModInitializer {
	public static final String MOD_ID = "pra";

	@Override
	public void onInitialize() {
		ModComponents.initialize(); // Register components before the registry is frozen
		ModItems.registerItems();
	}
}