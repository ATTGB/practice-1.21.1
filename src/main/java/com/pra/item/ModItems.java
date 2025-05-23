package com.pra.item;

import com.pra.item.custom.LightSaberItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item DEFAULT_LIGHTSABER = registerItems("default_lightsaber",
            new LightSaberItem(ModToolMaterials.LIGHTSABER, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.LIGHTSABER, 7, -1.3f))
            ));

    public static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of("pra", name), item);
    }

    public static void registerItems() {
    }
}