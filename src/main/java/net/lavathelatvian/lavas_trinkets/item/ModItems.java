package net.lavathelatvian.lavas_trinkets.item;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.lavathelatvian.lavas_trinkets.item.custom.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.lavathelatvian.lavas_trinkets.item.tiers.ModTiers.MYTHIC;

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
                            ))
    ));
    public static final Supplier<HarpoonItem> HARPOON_ITEM = ITEMS.register("harpoon", () -> new HarpoonItem(
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .component(ModDataComponents.HARPOON_THROWN.get(), false)
                    .component(ModDataComponents.THROWING.get(), false)
    ));

    public static final Supplier<RedemptionCrystalItem> CRYSTAL_OF_REDEMPTION = ITEMS.register("crystal_of_redemption", () -> new RedemptionCrystalItem(
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
    ));
    public static final Supplier<WraithItem> WRAITH = ITEMS.register("wraith", () -> new WraithItem(
            Tiers.NETHERITE,
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .component(ModDataComponents.WRAITH_CHARGES.get(), 0)
                    .attributes(
                            WraithItem.createAttributes(
                                    Tiers.NETHERITE,
                                    6,
                                    -2.6f
                            ))));
    /**
    public static final Supplier<ProsperityPickaxeItem> PROSPERITY_PICKAXE = ITEMS.register("pickaxe_of_prosperity", () -> new ProsperityPickaxeItem(
            Tiers.DIAMOND,
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .attributes(
                            ProsperityPickaxeItem.createAttributes(
                                    MYTHIC, 1, -2f
                            )))); */
    public static final Supplier<ObidianSickleItem> CRYING_SICKLE = ITEMS.register("sickle_of_tears", () -> new ObidianSickleItem(
            Tiers.NETHERITE,
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .attributes(ObidianSickleItem.createAttributes())
    ));
    public static final Supplier<GrapplerItem> GRAPPLER = ITEMS.register("grappler", () -> new GrapplerItem(
            new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .stacksTo(1)
                    .component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)
    ));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
