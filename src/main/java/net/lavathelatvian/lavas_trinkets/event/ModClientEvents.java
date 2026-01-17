package net.lavathelatvian.lavas_trinkets.event;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.effect.ModEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;

@EventBusSubscriber(modid = LavasTrinketsMod.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onPlayerHeartTypeEvent(PlayerHeartTypeEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide() && player.hasEffect(ModEffects.OBSIDIAN_CRY_EFFECT)) {
            event.setType(Gui.HeartType.valueOf("lavas_trinkets_crying"));
        }
    }
}
