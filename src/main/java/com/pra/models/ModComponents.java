package com.pra.models;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<String> BLADE_TYPE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("pra", "blade_type"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static final ComponentType<String> HILT_TYPE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("pra", "hilt_type"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static void initialize() {
        // Initialization logic if needed
    }
}
