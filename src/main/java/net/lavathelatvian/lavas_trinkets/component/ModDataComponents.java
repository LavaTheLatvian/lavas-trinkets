package net.lavathelatvian.lavas_trinkets.component;

import com.mojang.serialization.Codec;
import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.openjdk.nashorn.api.tree.UnaryTree;

import javax.swing.text.html.parser.Entity;
import javax.xml.crypto.Data;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ModDataComponents {

public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, LavasTrinketsMod.MODID);

public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HARPOON_THROWN = register("harpoon_thrown",
                    builder -> builder.persistent(Codec.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> THROWING = register("throwing",
            builder -> builder.persistent(Codec.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> HARPOON_SAVED = register("harpoon_saved",
            builder -> builder.persistent(UUIDUtil.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WRAITH_CHARGES = register("wraith_charges",
            builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> GRAPPLER_ARROW = register("grappler_arrow",
            builder -> builder.persistent(UUIDUtil.CODEC));



private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(
        String name,
        UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
}

public static void register(IEventBus eventBus) {
    DATA_COMPONENT_TYPES.register(eventBus);
}
}
