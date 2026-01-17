package net.lavathelatvian.lavas_trinkets.item.custom;

import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.lavathelatvian.lavas_trinkets.sound.ModSounds;
import net.lavathelatvian.lavas_trinkets.util.CompatManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.minecraft.util.Mth.floor;

public class WraithItem extends SwordItem {
    public WraithItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    static boolean playHitSounds = !CompatManager.isBetterCombatLoaded();

    private boolean isCriticalHit(Player player) {
        return player.fallDistance > 0.0F
                && !player.onGround()
                && !player.isSprinting()
                && !player.isInWater()
                && player.getVehicle() == null
                && !player.hasEffect(MobEffects.BLINDNESS)
                && player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (isCriticalHit(player)) {
                if (stack.get(ModDataComponents.WRAITH_CHARGES) < 10) stack.set(ModDataComponents.WRAITH_CHARGES, stack.get(ModDataComponents.WRAITH_CHARGES) + 1 );


                if (stack.get(ModDataComponents.WRAITH_CHARGES) == 3 || stack.get(ModDataComponents.WRAITH_CHARGES) == 6)
                    player.level().playSound((Player) null, player, ModSounds.WRAITH_CHARGE.get(), SoundSource.PLAYERS, 2.0F, 1);
                else if (stack.get(ModDataComponents.WRAITH_CHARGES) == 9)
                    player.level().playSound((Player) null, player, ModSounds.WRAITH_CHARGE.get(), SoundSource.PLAYERS, 4.0F, 0.8f);

                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SOUL, target.getX(), target.getY(0.5)-0.6, target.getZ(),
                            20,
                            0.3,
                            1.3,
                            0.3,
                            0.05);
                }
            }
            if (playHitSounds)
            player.level().playSound(null, player, ModSounds.WRAITH_HIT.get(), SoundSource.PLAYERS, 1.5F, (float) (1.0F + player.getRandom().nextGaussian() / 10f));
        }
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.get(ModDataComponents.WRAITH_CHARGES) > 2) {
            int speed = floor(stack.get(ModDataComponents.WRAITH_CHARGES) / 3);
            int duration = (10 - (speed * 2)) * 20;
            if (speed > 0) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, speed-1));
                player.level().playSound(null, player, ModSounds.WRAITH_CHARGE.get(), SoundSource.PLAYERS, 5.0F, 0.6f);
                stack.set(ModDataComponents.WRAITH_CHARGES, 0);
                player.getCooldowns().addCooldown(this, duration + 60);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    // removes durability loss
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        return true;
    }

    public boolean isBarVisible(ItemStack stack) {
        return stack.get(ModDataComponents.WRAITH_CHARGES) / 3 > 0;
    }

    public int getBarWidth(ItemStack stack) {
        int speed = floor(stack.get(ModDataComponents.WRAITH_CHARGES) / 3);
        return speed * 4;
    }

    public int getBarColor(ItemStack stack) {
        int speed = floor(stack.get(ModDataComponents.WRAITH_CHARGES) / 3);
        if (speed == 1) return 0x277889;
        else if (speed == 2) return 0x439d96;
        else return 0x9edae7;
    }

}
