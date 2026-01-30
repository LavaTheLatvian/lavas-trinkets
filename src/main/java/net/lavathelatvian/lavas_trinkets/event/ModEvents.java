package net.lavathelatvian.lavas_trinkets.event;

import net.lavathelatvian.lavas_trinkets.LavasTrinketsMod;
import net.lavathelatvian.lavas_trinkets.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

//@EventBusSubscriber(modid = LavasTrinketsMod.MODID)
public class ModEvents {
/**
    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        event.getRareTrades().add((entity, random) ->
                new MerchantOffer(
                        new ItemCost(Items.EMERALD, 20),              // cost
                        new ItemStack(ModItems.PROSPERITY_PICKAXE.get(), 1),     // result
                        1,   // max uses
                        1,    // villager XP (ignored for wandering trader)
                        0.05F // price multiplier
                )
        );
    }
*/
}
