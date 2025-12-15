package net.lavathelatvian.lavas_trinkets.item;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.lavathelatvian.lavas_trinkets.item.tiers.RareWeaponTier.MYTHIC;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LavasTrinketsMod.MODID);


    //ITEMS is a DeferredRegister<Item>
    public static final Supplier<SwordItem> NEEDLE_ITEM = ITEMS.register("needle", () -> new NeedleItem(
            MYTHIC,
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .attributes(
                            NeedleItem.createAttributes(
                                    MYTHIC,
                                    6,
                                    -2.6f
                            ))));
    public static final Supplier<HarpoonItem> HARPOON_ITEM = ITEMS.register("harpoon", () -> new HarpoonItem(
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .component(ModDataComponents.HARPOON_THROWN.get(), false)
                    .attributes(
                            HarpoonItem.createAttributes()
                    )


    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
