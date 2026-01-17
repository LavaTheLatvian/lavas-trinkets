package net.lavathelatvian.lavas_trinkets.item.custom;

import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.lavathelatvian.lavas_trinkets.effect.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class GrapplerItem extends CrossbowItem {
    public GrapplerItem(Properties properties) {
        super(properties);
    }
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_ONLY;
    }

// Bullshit code from mojang
    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            vector3f1 = (new Vector3f(vector3f)).cross(vec3.toVector3f());
        }
        Vector3f vector3f2 = (new Vector3f(vector3f)).rotateAxis(((float)Math.PI / 2F), vector3f1.x, vector3f1.y, vector3f1.z);
        return (new Vector3f(vector3f)).rotateAxis(angle * ((float)Math.PI / 180F), vector3f2.x, vector3f2.y, vector3f2.z);
    }
    private static float getShotPitch(RandomSource random, int index) {
        return index == 0 ? 1.0F : getRandomShotPitch((index & 1) == 1, random);
    }
    private static float getRandomShotPitch(boolean isHighPitched, RandomSource random) {
        float f = isHighPitched ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }
    private static float getShootingPower(ChargedProjectiles projectile) {
        return 1.7F;
    }




    // actual shooting part
    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getZ() - shooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * (double)0.2F;
            vector3f = getProjectileShotVector(shooter, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(angle * ((float)Math.PI / 180F)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = shooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, inaccuracy);
        // Sets data component to save arrow for later use
        shooter.getItemInHand(shooter.getUsedItemHand()).set(ModDataComponents.GRAPPLER_ARROW, projectile.getUUID());
        float f = getShotPitch(shooter.getRandom(), index);
        shooter.level().playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, shooter.getSoundSource(), 1.0F, f);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ChargedProjectiles chargedprojectiles = (ChargedProjectiles)itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(level, player, hand, itemstack, getShootingPower(chargedprojectiles), 1.0F, (LivingEntity)null);
            return InteractionResultHolder.consume(itemstack);
        } else
            if (level instanceof ServerLevel serverLevel) {

                if (serverLevel.getEntity((itemstack.get(ModDataComponents.GRAPPLER_ARROW.get()))) != null) {
                    Entity arrow = serverLevel.getEntity(Objects.requireNonNull(itemstack.get(ModDataComponents.GRAPPLER_ARROW.get())));
                    AbstractArrow arrow1 = (AbstractArrow) arrow;

                    boolean grapple = false;

                    if (!level.getBlockState(arrow.getOnPos().above()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().below()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().north()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().east()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().south()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().west()).is(Blocks.AIR)) grapple = true;

                    if (!level.getBlockState(arrow.getOnPos().north().east()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().south().east()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().south().west()).is(Blocks.AIR)) grapple = true;
                    if (!level.getBlockState(arrow.getOnPos().north().west()).is(Blocks.AIR)) grapple = true;


                    if (grapple) {
                        if (!player.isCrouching()) {
                            Vec3 vec3 = arrow.position().subtract(player.getEyePosition()).scale(0.12f);
                            if (arrow.getY() < player.getY()) vec3 = arrow.position().add(0, 1.5, 0).subtract(player.getEyePosition()).scale(0.12f);
                            player.setDeltaMovement(vec3);
                            player.hurtMarked = true;
                        }
                        arrow.discard();
                        player.getCooldowns().addCooldown(this, 40);
                    }
                }
                else if (!player.getProjectile(itemstack).isEmpty()) player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
            else return InteractionResultHolder.pass(itemstack);
    }

}
