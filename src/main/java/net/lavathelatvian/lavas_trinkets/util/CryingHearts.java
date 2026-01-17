package net.lavathelatvian.lavas_trinkets.util;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class CryingHearts {
    public static final ResourceLocation FULL = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_full");
    public static final ResourceLocation FULL_BLINKING = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_full_blinking");
    public static final ResourceLocation HALF = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_half");
    public static final ResourceLocation HALF_BLINKING = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_half_blinking");
    public static final ResourceLocation HARDCORE_FULL = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_hardcore_full");
    public static final ResourceLocation HARDCORE_BLINKING = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_full_blinking");
    public static final ResourceLocation HARDCORE_HALF = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_half");
    public static final ResourceLocation HARDCORE_HALF_BLINKING = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "hud/heart/crying_half_blinking");
    public static final EnumProxy<Gui.HeartType> HEART_TYPE_ENUM_PROXY = new EnumProxy<>(
            Gui.HeartType.class, FULL, FULL_BLINKING, HALF, HALF_BLINKING, HARDCORE_FULL, HARDCORE_BLINKING, HARDCORE_HALF, HARDCORE_HALF_BLINKING
    );
}
