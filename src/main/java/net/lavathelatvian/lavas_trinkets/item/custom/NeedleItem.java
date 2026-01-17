package net.lavathelatvian.lavas_trinkets.item.custom;

import net.lavathelatvian.lavas_trinkets.sound.ModSounds;
import net.lavathelatvian.lavas_trinkets.util.CompatManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NeedleItem extends SwordItem {
    public NeedleItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    static boolean playHitSounds = !CompatManager.isBetterCombatLoaded();

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (!player.isFallFlying()) {
                player.setDeltaMovement(new Vec3(
                        (player.getLookAngle().x + player.getDeltaMovement().x()) * (1.3),
                        (player.getLookAngle().y + player.getDeltaMovement().y()) / 1.5,
                        (player.getLookAngle().z + player.getDeltaMovement().z()) * (1.3)
                ));
                player.hurtMarked = true;
                player.getCooldowns().addCooldown(this, 100);

                if (1 > (Math.random() / 0.01))
                    player.level().playSound(null, player, ModSounds.HORNET_SHAW.get(), SoundSource.PLAYERS, 10, 1f);
                else player.level().playSound(null, player, ModSounds.NEEDLE_SHING.get(), SoundSource.PLAYERS, 5, 1f);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && playHitSounds)
        player.level().playSound(null, player, ModSounds.HARPOON_STAB.get(), SoundSource.PLAYERS, 1, (float) (1.0F + player.getRandom().nextGaussian() / 10f));
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        return true;
    }

}
