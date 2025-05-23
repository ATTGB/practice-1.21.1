package com.pra.mixin;

import com.pra.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Unique
    private static final Identifier CROSSHAIR_TEXTURE = Identifier.of("minecraft", "textures/gui/crosshair.png");

    @Redirect(
            method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z")
    )
    private boolean doRenderCrosshair(Perspective perspective) {
        if (perspective.isFirstPerson()) {
            return true;
        }
        // Only show in third person if holding a bow
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.getMainHandStack().isOf(ModItems.DEFAULT_LIGHTSABER)) {
            return true;
        }
        return false;
    }

    @Redirect(
            method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V")
    )
    private void drawGuiTexture(DrawContext context, Identifier texture, int x, int y, int w, int h) {
        if (texture.equals(CROSSHAIR_TEXTURE)) {
            context.drawGuiTexture(texture, x, y, w, h);
        } else {
            context.drawGuiTexture(texture, x, y, w, h);
        }

    }}




