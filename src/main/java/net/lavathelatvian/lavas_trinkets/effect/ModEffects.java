package net.lavathelatvian.lavas_trinkets.effect;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, LavasTrinketsMod.MODID);

    public static final Holder<MobEffect> OBSIDIAN_CRY_EFFECT = MOB_EFFECTS.register("unbreakable_cry",
            () -> new ObsidianCryEffect(MobEffectCategory.HARMFUL, 0x6006ab)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, "crying"), -0.1f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

            public static void register(IEventBus eventBus) {
                MOB_EFFECTS.register(eventBus);
            }

}
