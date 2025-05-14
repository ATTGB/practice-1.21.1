package com.pra.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModularSwordUnbakedModel implements UnbakedModel {
    private static final Identifier BASE_MODEL = Identifier.of("item/handheld");

    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
    }

    @Nullable
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        UnbakedModel baseUnbakedModel = null; // Replace with actual logic to load the model
        BakedModel baseModel = null;
        if (baseUnbakedModel != null) {
            baseModel = baseUnbakedModel.bake(baker, textureGetter, rotationContainer);
        }

        return baseModel;
    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> modelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return List.of(
                new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of("pra:item/blade_variant")),
                new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of("pra:item/hilt_variant"))
        );
    }
}