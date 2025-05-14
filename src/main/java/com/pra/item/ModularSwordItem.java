package com.pra.item;

import com.pra.models.ModComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class ModularSwordItem extends Item {
    public ModularSwordItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        String blade = stack.getOrDefault(ModComponents.BLADE_TYPE, "default_blade");
        String hilt = stack.getOrDefault(ModComponents.HILT_TYPE, "default_hilt");

        tooltip.add(Text.literal("Blade: " + blade));
        tooltip.add(Text.literal("Hilt: " + hilt));
        }

    }