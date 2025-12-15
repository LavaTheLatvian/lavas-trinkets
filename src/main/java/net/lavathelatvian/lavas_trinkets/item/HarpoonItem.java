package net.lavathelatvian.lavas_trinkets.item;

import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.lavathelatvian.lavas_trinkets.entity.ModEntities;
import net.lavathelatvian.lavas_trinkets.entity.custom.HarpoonProjectile;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class HarpoonItem extends Item {

    public HarpoonItem(Properties properties) {
        super(properties);
    }
    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)8.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)-2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }







//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
//        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
//                SoundEvents.TRIDENT_THROW, SoundSource.NEUTRAL, 0.7F, 0.6F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
//        if (!pLevel.isClientSide) {
//            HarpoonProjectile harpoonProjectile = new HarpoonProjectile(pPlayer, pLevel);
//            harpoonProjectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 0F);
//            pLevel.addFreshEntity(harpoonProjectile);
//        }
//
//        pPlayer.awardStat(Stats.ITEM_USED.get(this));
//        if (!pPlayer.getAbilities().instabuild) {
////            itemstack.shrink(1);
//            itemstack.getDisplayName();
//        }
//
//        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
//    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (i >= 10) {
                float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                if ((!(f > 0.0F) || player.isInWaterOrRain())) {
                    Holder<SoundEvent> holder = (Holder)EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
                    if (!level.isClientSide) {
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                        if (f == 0.0F) {
                            HarpoonProjectile harpoon = new HarpoonProjectile(player, level);
                            harpoon.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);

                            player.getItemInHand(entityLiving.getUsedItemHand()).set(ModDataComponents.HARPOON_SAVED, harpoon.getUUID());
                            player.getItemInHand(entityLiving.getUsedItemHand()).set(ModDataComponents.HARPOON_THROWN, true);

                            if (player.hasInfiniteMaterials()) {
                                harpoon.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(harpoon);

                            level.playSound((Player)null, harpoon, (SoundEvent)holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

                            if (!player.hasInfiniteMaterials()) {
//                                player.getInventory().removeItem(stack);
//                                CustomData.update(DataComponents.CUSTOM_DATA, entityLiving.getUseItem(), tag -> tag.putBoolean("thrown", true));
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }

    private Vec3 getDirection(Vec3 origin, Vec3 target){
        Vec3 direction = target.subtract(origin).normalize();
        return direction.scale(1/direction.length());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!player.getItemInHand(hand).get(ModDataComponents.HARPOON_THROWN)) {

            if (EnchantmentHelper.getTridentSpinAttackStrength(itemstack, player) > 0.0F && !player.isInWaterOrRain()) {
                return InteractionResultHolder.fail(itemstack);
            } else {

                player.startUsingItem(hand);
                return InteractionResultHolder.success(itemstack);
            }
        }

        else  {
            if (level instanceof ServerLevel serverLevel) {
                if (serverLevel.getEntity((itemstack.get(ModDataComponents.HARPOON_SAVED.get()))) != null) {
                    if (player.distanceTo(serverLevel.getEntity((itemstack.get(ModDataComponents.HARPOON_SAVED.get())))) < 120) {

                        Entity harpoon = serverLevel.getEntity(Objects.requireNonNull(itemstack.get(ModDataComponents.HARPOON_SAVED.get())));

                        HarpoonProjectile harpoon2 = (HarpoonProjectile) harpoon;

                        if (harpoon2.isStuck()) {
                            harpoon2.startPulling(harpoon);
                        }

                        Vec3 vec3 = player.getEyePosition().subtract(harpoon.position());
                        harpoon.setDeltaMovement(vec3.normalize());
                                //.scale(Math.max(0.4, player.distanceTo(harpoon)/50 )));


//                        if (this.getWorld().isClient) {
//                            this.lastRenderY = harpoon.getY();
//                        }
                        double length = vec3.length();
//                        Vec3 move = Math.min(length, 6);


                        //harpoon.moveTo(Math.round(harpoon.getX()), Math.round(harpoon.getY()), Math.round(harpoon.getZ()));

                        Vec3 flyVector = player.position().subtract(harpoon.position()).normalize().scale(0.2f);
//                        harpoon.push(flyVector.x, flyVector.y, flyVector.z);


                    }
                    else {
                        serverLevel.getEntity(Objects.requireNonNull(itemstack.get(ModDataComponents.HARPOON_SAVED.get()))).discard();
                        itemstack.set(ModDataComponents.HARPOON_SAVED, null);
                        itemstack.set(ModDataComponents.HARPOON_THROWN, false);
                        player.getCooldowns().addCooldown(this, 20);
                    }
                }
                else {
                    itemstack.set(ModDataComponents.HARPOON_SAVED, null);
                    itemstack.set(ModDataComponents.HARPOON_THROWN, false);
                    player.getCooldowns().addCooldown(this, 20);
                }
            }


            return InteractionResultHolder.success(itemstack);
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getEntity((stack.get(ModDataComponents.HARPOON_SAVED.get()))) != null) {
                if (entity.distanceTo(serverLevel.getEntity((stack.get(ModDataComponents.HARPOON_SAVED.get())))) < 1.1) {
                    // delete saved entity
                    serverLevel.getEntity(Objects.requireNonNull(stack.get(ModDataComponents.HARPOON_SAVED.get()))).discard();
                    // Set saved entity to null
                    stack.set(ModDataComponents.HARPOON_SAVED, null);
                    // allow to throw again
                    stack.set(ModDataComponents.HARPOON_THROWN, false);
                    // cooldown
                    if (entity instanceof Player player) {
                        player.getCooldowns().addCooldown(this, 20);
                    }
                }
            }
        }

    }


    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        if (stack.get(ModDataComponents.HARPOON_THROWN) == true) return UseAnim.NONE;
        else return UseAnim.SPEAR;
    }

    public int getDefaultProjectileRange() {
        return 15;
    }



    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
//        if(Screen.hasShiftDown()) {
//            tooltipComponents.add(Component.translatable("tooltip.tutorialmod.chisel.shift_down"));
//        } else {
//            tooltipComponents.add(Component.translatable("tooltip.tutorialmod.chisel"));
//        }

        if(stack.get(ModDataComponents.HARPOON_SAVED) != null) {
            tooltipComponents.add(Component.literal("saved harpoon " + stack.get(ModDataComponents.HARPOON_SAVED)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }




/**
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0) {
                    return;
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel) {
                        ServerLevel serverlevel = (ServerLevel)level;
                        if (!list.isEmpty()) {
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                        }
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }

    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
    }

    public static float getPowerForTime(int charge) {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

 */
}
