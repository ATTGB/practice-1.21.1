package com.pra;

	import com.pra.Keybind.Keybinds;
	import com.pra.util.LightsaberViewHandler;
	import net.fabricmc.api.ClientModInitializer;
	import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class PracticeClient implements ClientModInitializer {

		@Override
		public void onInitializeClient() {

			Keybinds.register();
			LightsaberViewHandler.register();
		}
	}