package com.pra;


import com.pra.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Practice implements ModInitializer {
 public static final String MOD_ID = "pra";

 @Override
 public void onInitialize() {
  ModItems.registerItems();

  ServerTickEvents.END_WORLD_TICK.register(world -> {
  });
 }
}