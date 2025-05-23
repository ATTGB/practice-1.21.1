package com.pra.util;

import com.pra.item.ModItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.item.ItemStack;

public class LightsaberViewHandler {

    private static Perspective previousPerspective = null;
    private static boolean forced = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            ItemStack mainHand = client.player.getMainHandStack();
            ItemStack offHand = client.player.getOffHandStack();

            boolean isHoldingLightsaber =
                    mainHand.getItem() == ModItems.DEFAULT_LIGHTSABER ||
                            offHand.getItem() == ModItems.DEFAULT_LIGHTSABER;

            if (isHoldingLightsaber) {
                if (!forced) {
                    previousPerspective = client.options.getPerspective();
                    forced = true;
                }

                if (client.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
                    client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                }

            } else if (forced) {
                if (previousPerspective != null) {
                    client.options.setPerspective(previousPerspective);
                    previousPerspective = null;
                }
                forced = false;
            }
        });
    }
}
