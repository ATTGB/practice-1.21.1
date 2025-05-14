package com.pra.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;

@Environment(EnvType.CLIENT)
public class ModularSwordModelPlugin implements ModelLoadingPlugin {
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            ModelIdentifier id = context.topLevelId();
            if (id != null && "pra".equals(id.id().getNamespace()) && "modular_sword".equals(id.id().getPath())) {
                return new ModularSwordUnbakedModel();
            }
            return original;
        });
    }
}
