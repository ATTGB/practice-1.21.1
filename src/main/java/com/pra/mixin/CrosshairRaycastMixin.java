package com.pra.mixin;

import com.pra.Keybind.Keybinds;
import com.pra.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class CrosshairRaycastMixin {

    @Inject(method = "on", at = @At("HEAD"), cancellable = true)
    private void overrideRaycast(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient)(Object)this;

        if (client.player == null || client.world == null) return;
        if (client.options.getPerspective() != Perspective.THIRD_PERSON_BACK) return;

        ItemStack main = client.player.getMainHandStack();
        ItemStack off = client.player.getOffHandStack();
        boolean holdingLightsaber =
                main.getItem() == ModItems.DEFAULT_LIGHTSABER ||
                        off.getItem() == ModItems.DEFAULT_LIGHTSABER;
        if (!holdingLightsaber) return;

        Camera camera = client.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        float yaw = camera.getYaw();
        float pitch = camera.getPitch();

        Vec3d lookDir = getLookDir(yaw, pitch);
        Vec3d worldUp = new Vec3d(0, 1, 0);
        Vec3d rightVec = lookDir.crossProduct(worldUp).normalize();

        final double back = -0.5;
        final double up = 0.5;
        final double sideOffset = 2.0;

        Vec3d offset = lookDir.multiply(back)
                .add(rightVec.multiply(Keybinds.isRightShoulder ? sideOffset : -sideOffset))
                .add(worldUp.multiply(up));

        Vec3d raycastStart = camPos.add(offset);
        double reach = client.interactionManager.hasCreativeInventory() ? 6.0 : 4.5;
        Vec3d raycastEnd = raycastStart.add(lookDir.multiply(reach));

        RaycastContext context = new RaycastContext(
                raycastStart, raycastEnd,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                client.player
        );
        HitResult hit = client.world.raycast(context);

        client.crosshairTarget = hit;
        client.targetedEntity = null; // Optional: expand with entity logic

        ci.cancel(); // prevent default behavior
    }

    private Vec3d getLookDir(float yaw, float pitch) {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);
        return new Vec3d(
                -MathHelper.sin(yawRad) * MathHelper.cos(pitchRad),
                -MathHelper.sin(pitchRad),
                MathHelper.cos(yawRad) * MathHelper.cos(pitchRad)
        ).normalize();
    }
}
