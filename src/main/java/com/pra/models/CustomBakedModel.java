package com.pra.models;

    import net.minecraft.block.BlockState;
    import net.minecraft.client.render.model.BakedModel;
    import net.minecraft.client.render.model.BakedQuad;
    import net.minecraft.client.render.model.json.ModelOverrideList;
    import net.minecraft.client.render.model.json.ModelTransformation;
    import net.minecraft.client.texture.Sprite;
    import net.minecraft.client.texture.SpriteAtlasTexture;
    import net.minecraft.client.util.SpriteIdentifier;
    import net.minecraft.util.Identifier;
    import net.minecraft.util.math.Direction;
    import net.minecraft.util.math.random.Random;
    import org.jetbrains.annotations.Nullable;

    import java.util.List;
    import java.util.function.Function;

public class CustomBakedModel implements BakedModel {
    private final BakedModel baseModel;
    private final Function<SpriteIdentifier, Sprite> textureGetter;

    public CustomBakedModel(BakedModel baseModel, Function<SpriteIdentifier, Sprite> textureGetter) {
        this.baseModel = baseModel;
        this.textureGetter = textureGetter;
    }

    private Sprite getBladeSprite(String bladeType) {

        String normalizedBladeType = switch (bladeType) {
            case "blade_two" -> "2";
            default -> "1";
        };

        Identifier bladeTexture = switch (normalizedBladeType) {
            default -> Identifier.of("pra:item/default_blade");
            case "2" -> Identifier.of("pra:item/blade_two");

        };
        return textureGetter.apply(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, bladeTexture));
    }

    private Sprite getHiltSprite(String hiltType) {
        Identifier hiltTexture = switch (hiltType) {
            default -> Identifier.of("pra:item/default_hilt");
        };
        return textureGetter.apply(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, hiltTexture));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return baseModel.useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth() {
        return baseModel.hasDepth();
    }

    @Override
    public boolean isSideLit() {
        return baseModel.isSideLit();
    }

    @Override
    public boolean isBuiltin() {
        return baseModel.isBuiltin();
    }

    @Override
    public Sprite getParticleSprite() {
        return baseModel.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return baseModel.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return baseModel.getOverrides();
    }
}