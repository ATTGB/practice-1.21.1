package com.pra.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item MODULAR_SWORD = new ModularSwordItem(new Item.Settings().maxCount(1));

    public static void registerItems() {

        Registry.register(Registries.ITEM, Identifier.of("pra", "modular_sword"), MODULAR_SWORD);
    }
}