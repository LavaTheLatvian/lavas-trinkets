package net.lavathelatvian.lavas_trinkets.item.custom;

import net.lavathelatvian.lavas_trinkets.effect.ModEffects;
import net.lavathelatvian.lavas_trinkets.sound.ModSounds;
import net.lavathelatvian.lavas_trinkets.util.CompatManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ObidianSickleItem extends HoeItem {

    static boolean playHitSounds = !CompatManager.isBetterCombatLoaded();

    public ObidianSickleItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)5.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)-2.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!entity.level().isClientSide) {
            ServerLevel world = (ServerLevel) entity.level();
            world.sendParticles(ParticleTypes.FALLING_OBSIDIAN_TEAR, entity.getX(), entity.getY(0.5)-0.6, entity.getZ(),
                    1,
                    world.random.nextGaussian() * 0.8D,
                    world.random.nextGaussian() * 0.2D,
                    world.random.nextGaussian() * 0.8D,
                    0.5D);
        }
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }


    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack, entity) - timeLeft;
        if (i >= 20 && entity instanceof Player player) {
            if (!player.level().isClientSide) {
                ServerLevel serverLevel = (ServerLevel) player.level();
                serverLevel.sendParticles(ParticleTypes.FALLING_OBSIDIAN_TEAR, player.getX(), player.getY(0.5)-0.6, player.getZ(),
                        150,
                        2,
                        0.3,
                        2,
                        3);
                serverLevel.sendParticles(ParticleTypes.FALLING_OBSIDIAN_TEAR, player.getX(), player.getY(0.5)-0.6, player.getZ(),
                        100,
                        2,
                        1.5,
                        2,
                        3);

                AABB box = player.getBoundingBox().inflate(3.0);

                for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class, box)) {
                    if (target != entity) {
                        int damage = 0;
                        if (target.hasEffect(ModEffects.OBSIDIAN_CRY_EFFECT)) {
                            damage = target.getEffect(ModEffects.OBSIDIAN_CRY_EFFECT).getDuration() / 10;
                            target.push(0, 1, 0);
                        } else target.push(0, 0.5f, 0);

                        serverLevel.sendParticles(ParticleTypes.FALLING_OBSIDIAN_TEAR, target.getX(), target.getY(0.5)-0.6, target.getZ(),
                                150,
                                0.3,
                                1.3,
                                0.3,
                                3);

                        Registry<DamageType> dTypeReg = target.damageSources().damageTypes;
                        Holder.Reference<DamageType> dType = (Holder.Reference) dTypeReg.getHolder(DamageTypes.MAGIC).orElse(dTypeReg.getHolderOrThrow(DamageTypes.MAGIC));
                        target.hurt(new DamageSource(dType), 4 + damage);

                        target.removeEffect(ModEffects.OBSIDIAN_CRY_EFFECT);
                    }
                }

                player.level().playSound(null, player, ModSounds.SICKLE_SHATTER.get(), SoundSource.PLAYERS, 20, 0.8f);


            }
            player.getCooldowns().addCooldown(this, 300);
        }
        else Minecraft.getInstance().getSoundManager().stop(ModSounds.SICKLE_CHARGE.get().getLocation(), SoundSource.PLAYERS);
        super.onStopUsing(stack, entity, timeLeft);
    }

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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        level.playSound(null, player, ModSounds.SICKLE_CHARGE.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
        return super.use(level, player, hand);
    }



    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && playHitSounds)
            player.level().playSound(null, player, ModSounds.SLICE.get(), SoundSource.PLAYERS, 1.5F, (float) (1.0F + player.getRandom().nextGaussian() / 10f));
        if (attacker instanceof Player player && isCriticalHit(player) && !target.hasEffect(ModEffects.OBSIDIAN_CRY_EFFECT))
        target.addEffect(new MobEffectInstance(ModEffects.OBSIDIAN_CRY_EFFECT, 300, 1));
    }

}
