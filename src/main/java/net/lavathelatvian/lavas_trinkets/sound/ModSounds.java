package net.lavathelatvian.lavas_trinkets.sound;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, LavasTrinketsMod.MODID);


    public static final Supplier<SoundEvent> SICKLE_CHARGE = registerSoundEvent("sickle_charge");
    public static final Supplier<SoundEvent> SICKLE_SHATTER = registerSoundEvent("sickle_shatter");
    public static final Supplier<SoundEvent> SLICE = registerSoundEvent("slice");
    public static final Supplier<SoundEvent> WRAITH_CHARGE = registerSoundEvent("wraith_charge");
    public static final Supplier<SoundEvent> WRAITH_HIT = registerSoundEvent("wraith_hit");
    public static final Supplier<SoundEvent> HORNET_SHAW = registerSoundEvent("hornet_shaw");
    public static final Supplier<SoundEvent> NEEDLE_SHING = registerSoundEvent("needle_shing");
    public static final Supplier<SoundEvent> HARPOON_STAB = registerSoundEvent("harpoon_stab");


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LavasTrinketsMod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
