package com.pra.Keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyBinding toggleShoulderKey;

    public static boolean isRightShoulder = true;

    public static void register() {
        toggleShoulderKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pra.toggle_shoulder",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.pra.controls"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleShoulderKey.wasPressed()) {
                isRightShoulder = !isRightShoulder; // Toggle the shoulder
                System.out.println("Shoulder toggled: " + (isRightShoulder ? "Right" : "Left")); // Debug log
            }
        });
    }
}