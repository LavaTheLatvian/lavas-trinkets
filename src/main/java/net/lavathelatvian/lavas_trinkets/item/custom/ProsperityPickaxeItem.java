package net.lavathelatvian.lavas_trinkets.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

public class ProsperityPickaxeItem extends PickaxeItem {
    public ProsperityPickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide) {
            if (state.is(Tags.Blocks.ORES))
            ExperienceOrb.award(
                    (ServerLevel) level,
                    pos.getCenter(),
                    3 // amount of XP
            );
        }

        return super.mineBlock(stack, level, state, pos, miningEntity);
    }



}
