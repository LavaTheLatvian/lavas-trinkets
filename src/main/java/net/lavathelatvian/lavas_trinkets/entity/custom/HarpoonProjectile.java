package net.lavathelatvian.lavas_trinkets.entity.custom;

import net.lavathelatvian.lavas_trinkets.entity.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;

public class HarpoonProjectile extends AbstractArrow {
    private float rotation;
    public Vec2 groundedOffset;
    public boolean hasImpacted;
    public int pullTimer;


    public HarpoonProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public HarpoonProjectile(LivingEntity entity, Level level) {
        super(ModEntities.HARPOON_THROWN.get(), entity, level, new ItemStack(Items.AIR), entity.getUseItem());
    }

    public void setPickupItemStackOrigin(ItemStack stack) {
        this.setPickupItemStack(stack);
    }

    public void startPulling(Entity entity) {
        if (this == entity) {
            this.inGround = false;
            ++this.pullTimer;
            this.setNoPhysics(true);
        }
    }

    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    public void stopPulling(Entity entity) {
        if (this == entity) {
            this.setNoPhysics(false);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.AIR);
    }

    public float getRenderingRotation() {
        rotation +=0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Override
    public void playerTouch(Player entity) {}

    @Override
    protected void onHitEntity(EntityHitResult result) {

        Entity entity = result.getEntity();
        float damage = 8.0F;
        this.hasImpacted = true;

        Entity owner = this.getOwner();
        DamageSource damagesource = this.damageSources().thrown(this, (Entity)(owner == null ? this : owner));
        Level var7 = this.level();
        if (var7 instanceof ServerLevel serverlevel) {
            // apply enchant damage *UNFINISHED*
            damage = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, damage);

            // +1 damage under water
            if (entity.isUnderWater()) ++damage;
        }

        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

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
        this.hasImpacted = true;
        this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
        super.onHitBlock(result);
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

    protected float getWaterInertia() {
        return 0.98F;
    }
}
