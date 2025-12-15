package net.lavathelatvian.lavas_trinkets.event;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.entity.client.HarpoonProjectileModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = LavasTrinketsMod.MODID)
public class ModEventBusEvents {
@SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(HarpoonProjectileModel.LAYER_LOCATION, HarpoonProjectileModel::createBodyLayer);
}
}

