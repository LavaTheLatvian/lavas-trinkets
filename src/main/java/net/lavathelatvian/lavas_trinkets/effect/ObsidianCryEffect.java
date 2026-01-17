package net.lavathelatvian.lavas_trinkets.effect;

import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;

public class ObsidianCryEffect extends MobEffect {
    public ObsidianCryEffect(MobEffectCategory category, int color) {
        super(category, color);
    }


    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Registry<DamageType> dTypeReg = livingEntity.damageSources().damageTypes;
        Holder.Reference<DamageType> dType = (Holder.Reference)dTypeReg.getHolder(NeoForgeMod.POISON_DAMAGE).orElse(dTypeReg.getHolderOrThrow(DamageTypes.MAGIC));
        livingEntity.hurt(new DamageSource(dType), 2);
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FALLING_OBSIDIAN_TEAR, livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(),
                    1,
                    serverLevel.random.nextGaussian() * 0.3D,
                    serverLevel.random.nextGaussian() * 0.3D,
                    serverLevel.random.nextGaussian() * 0.3D,
                    0.3D);
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 25 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }

}
