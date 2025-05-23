package com.pra.mixin;

import com.pra.Keybind.Keybinds;
import com.pra.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void offsetForBodyCam(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!(focusedEntity instanceof ClientPlayerEntity player)) return;
        if (client.options.getPerspective() != Perspective.THIRD_PERSON_BACK) return;

        // Only apply when holding the lightsaber
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        boolean holdingLightsaber =
                main.getItem() == ModItems.DEFAULT_LIGHTSABER ||
                        off.getItem() == ModItems.DEFAULT_LIGHTSABER;
        if (!holdingLightsaber) return;

        // Interpolated player position and eye position
        double px = MathHelper.lerp(tickDelta, player.prevX, player.getX());
        double py = MathHelper.lerp(tickDelta, player.prevY, player.getY());
        double pz = MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
        Vec3d playerPos = new Vec3d(px, py, pz);
        Vec3d eyePos = player.getEyePos();

        // Get pitch and yaw
        float yaw = player.getYaw(tickDelta);
        float pitch = player.getPitch(tickDelta);

        // Constants for offset distances
        final double back = -0.5;
        final double up = 0.5;
        final double sideOffset = 2.0;

        Vec3d offset;

        if (pitch > 85.0f) {
            // Looking nearly straight up
            float yawRad = (float) Math.toRadians(yaw);
            Vec3d forwardYaw = new Vec3d(-MathHelper.sin(yawRad), 0, MathHelper.cos(yawRad)).normalize();
            Vec3d rightYaw = new Vec3d(-forwardYaw.z, 0, forwardYaw.x).normalize();

            offset = rightYaw.multiply(Keybinds.isRightShoulder ? sideOffset : -sideOffset)
                    .add(forwardYaw.multiply(-back))
                    .add(new Vec3d(0, up + 0.5, 0));
        } else if (pitch < -85.0f) {
            // Looking nearly straight down
            float yawRad = (float) Math.toRadians(yaw);
            Vec3d forwardYaw = new Vec3d(-MathHelper.sin(yawRad), 0, MathHelper.cos(yawRad)).normalize();
            Vec3d rightYaw = new Vec3d(-forwardYaw.z, 0, forwardYaw.x).normalize();

            offset = rightYaw.multiply(Keybinds.isRightShoulder ? sideOffset : -sideOffset)
                    .add(forwardYaw.multiply(back))
                    .add(new Vec3d(0, up + 0.5, 0));
        } else {
            // Normal calculation
            float yawRad = (float) Math.toRadians(yaw);
            float pitchRad = (float) Math.toRadians(pitch);

            Vec3d lookDir = new Vec3d(
                    -MathHelper.sin(yawRad) * MathHelper.cos(pitchRad),
                    -MathHelper.sin(pitchRad),
                    MathHelper.cos(yawRad) * MathHelper.cos(pitchRad)
            ).normalize();

            Vec3d worldUp = new Vec3d(0, 1, 0);
            Vec3d rightVec = lookDir.crossProduct(worldUp);

            // Defensive: if rightVec too small (near vertical lookDir), fallback to X axis
            if (rightVec.lengthSquared() < 1e-6) {
                rightVec = new Vec3d(1, 0, 0);
            } else {
                rightVec = rightVec.normalize();
            }

            offset = lookDir.multiply(back)
                    .add(rightVec.multiply(Keybinds.isRightShoulder ? sideOffset : -sideOffset))
                    .add(worldUp.multiply(up));
        }

        Vec3d idealPos = eyePos.add(offset);

        // Raycast to prevent clipping
        RaycastContext ctx = new RaycastContext(
                eyePos, idealPos,
                RaycastContext.ShapeType.VISUAL,
                RaycastContext.FluidHandling.NONE,
                player
        );
        HitResult result = player.getWorld().raycast(ctx);
        if (result.getType() != HitResult.Type.MISS) {
            idealPos = eyePos.add(idealPos.subtract(eyePos).normalize()
                    .multiply(result.getPos().distanceTo(eyePos) - 0.1));
        }

        // Smooth interpolation (optional)
        Camera self = (Camera) (Object) this;
        Vec3d current = self.getPos();
        Vec3d smoothed = current.lerp(idealPos, 0.25);

        // Set position using reflection
        try {
            Method setPos = Camera.class.getDeclaredMethod("setPos", Vec3d.class);
            setPos.setAccessible(true);
            setPos.invoke(self, smoothed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
