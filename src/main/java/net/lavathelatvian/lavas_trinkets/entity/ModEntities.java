package net.lavathelatvian.lavas_trinkets.entity;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.entity.custom.HarpoonProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, LavasTrinketsMod.MODID);

//    public static final Supplier<EntityType<EntityName>> TEST_ENTITY =
//            ENTITY_TYPES.register("test_entity", () -> EntityType.Builder.of(TestEntity::new, MobCategory.CREATURE)
//                    .sized(1.25f, 3f).build("test_entity"));
//public static final Supplier<EntityType<ThrownHarpoon>> HARPOON =
//        ENTITY_TYPES.register("harpoon", () -> EntityType.Builder.<ThrownHarpoon>of(ThrownHarpoon::new, MobCategory.MISC)
//                .sized(0.5f, 1.15f).build("harpoon"));
    public static final Supplier<EntityType<HarpoonProjectile>> HARPOON_THROWN =
        ENTITY_TYPES.register("harpoon", () -> EntityType.Builder.<HarpoonProjectile>of(HarpoonProjectile::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .build("harpoon")
        );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
