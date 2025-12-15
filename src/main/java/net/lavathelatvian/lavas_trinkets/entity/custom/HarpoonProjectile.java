package net.lavathelatvian.lavas_trinkets.entity.custom;

import net.lavathelatvian.lavas_trinkets.entity.ModEntities;
import net.lavathelatvian.lavas_trinkets.item.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class HarpoonProjectile extends AbstractArrow {
    private float rotation;
    public Vec2 groundedOffset;


    public HarpoonProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public HarpoonProjectile(LivingEntity entity, Level level) {
        super(ModEntities.HARPOON_THROWN.get(), entity, level, new ItemStack(ModItems.HARPOON_ITEM.get()), null);
    }


    public void startPulling(Entity entity) {
        if (this == entity) {
//            this.entityData.set(PULLING, true);
            this.inGround = false;
//            this.setNoPhysics(true);
        }
    }


    public boolean isStuck() {
        return this.inGround;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.HARPOON_ITEM.get());
    }

    public float getRenderingRotation() {
        rotation +=0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }
    public boolean isGrounded() {return inGround;}

    @Override
    public void playerTouch(Player entity) {
        if (!this.level().isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0 && this.tryPickup(entity)) {
//            this.getOwner();
////            entity.take(this, 1);
////            CustomData.update(DataComponents.CUSTOM_DATA, entity.getUseItem(), tag -> tag.putBoolean("thrown", false));
//            this.discard();
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {

        Entity entity = result.getEntity();
        float damage = 8.0F;

        Entity owner = this.getOwner();
        DamageSource damagesource = this.damageSources().thrown(this, (Entity)(owner == null ? this : owner));
        Level var7 = this.level();
        if (var7 instanceof ServerLevel serverlevel) {
            // apply enchant damage
//            damage = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, damage);
            // +1 damage under water
            if (entity.isUnderWater()) ++damage;
        }

        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

//            var7 = this.level();
//            if (var7 instanceof ServerLevel) {
//                ServerLevel serverlevel1 = (ServerLevel)var7;
//                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource, this.getWeaponItem());
//            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.005, -0.05, -0.005));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 0.6F);


    }






    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
//        this.setNoPhysics(true);
        if(result.getDirection() == Direction.SOUTH) {
            groundedOffset = new Vec2(215f, 180f);
        }
        if(result.getDirection() == Direction.NORTH) {
            groundedOffset = new Vec2(215f, 0f);
        }
        if(result.getDirection() == Direction.EAST) {
            groundedOffset = new Vec2(215f, -90f);
        }
        if(result.getDirection() == Direction.WEST) {
            groundedOffset = new Vec2(215f, 90f);
        }
        if(result.getDirection() == Direction.DOWN) {
            groundedOffset = new Vec2(115f, 180f);
        }
        if(result.getDirection() == Direction.UP) {
            groundedOffset = new Vec2(285f, 180f);
        }
    }

}
