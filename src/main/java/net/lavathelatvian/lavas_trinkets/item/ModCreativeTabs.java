package net.lavathelatvian.lavas_trinkets.item;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LavasTrinketsMod.MODID);

    public static final Supplier<CreativeModeTab> BISMUTH_ITEMS_TAB = CREATIVE_MODE_TAB.register("trinkets_gear_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.NEEDLE_ITEM.get()))
                    .title(Component.translatable("creativetab.lavas_trinkets.gear"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.NEEDLE_ITEM.get());
                        output.accept(ModItems.HARPOON_ITEM.get());
                        output.accept(ModItems.WRAITH.get());
                        output.accept(ModItems.CRYING_SICKLE.get());
                        output.accept(ModItems.PROSPERITY_PICKAXE.get());
                        output.accept(ModItems.CRYSTAL_OF_REDEMPTION.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
