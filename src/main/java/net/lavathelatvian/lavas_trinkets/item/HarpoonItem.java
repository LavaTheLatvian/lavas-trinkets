package net.lavathelatvian.lavas_trinkets.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.lavathelatvian.lavas_trinkets.component.ModDataComponents;
import net.lavathelatvian.lavas_trinkets.entity.custom.HarpoonProjectile;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Objects;

public class HarpoonItem extends Item {

    public HarpoonItem(Properties properties) {
        super(properties);
    }
    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        if (entity.level() instanceof ServerLevel serverLevel && stack.get(ModDataComponents.HARPOON_THROWN) && serverLevel.getEntity(stack.get(ModDataComponents.HARPOON_SAVED)) != null) {
            Entity harpoon = serverLevel.getEntity(Objects.requireNonNull(stack.get(ModDataComponents.HARPOON_SAVED.get())));
            HarpoonProjectile harpoon2 = (HarpoonProjectile) harpoon;
            harpoon2.stopPulling(harpoon);
        }
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (Boolean.TRUE.equals(stack.get(ModDataComponents.HARPOON_THROWN))) {
            if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(stack.get(ModDataComponents.HARPOON_SAVED)) != null) {
                Entity harpoon = serverLevel.getEntity(Objects.requireNonNull(stack.get(ModDataComponents.HARPOON_SAVED.get())));
                HarpoonProjectile harpoon2 = (HarpoonProjectile) harpoon;
                harpoon2.stopPulling(harpoon);
            }
        }
        else {
            if (entityLiving instanceof Player player) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                if (i >= 10) {
                    float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                    if ((!(f > 0.0F) || player.isInWaterOrRain())) {
                        Holder<SoundEvent> holder = (Holder) EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
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

                                level.playSound((Player) null, harpoon, (SoundEvent) holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            }
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                    }
                }
            }
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (level instanceof ServerLevel serverLevel ) {
            if (!(entity instanceof Player player)) return;
            if (stack.get(ModDataComponents.HARPOON_THROWN) && serverLevel.getEntity(Objects.requireNonNull(stack.get(ModDataComponents.HARPOON_SAVED))) != null) {
                Entity harpoon = serverLevel.getEntity(Objects.requireNonNull(stack.get(ModDataComponents.HARPOON_SAVED.get())));
                HarpoonProjectile harpoon2 = (HarpoonProjectile) harpoon;
                Vec3 vec3 = entity.getEyePosition().subtract(harpoon.position());
                harpoon.setDeltaMovement(vec3.normalize());
            }
        }
    }


    public void stopPulling(Level level, LivingEntity livingEntity, ItemStack stack) {

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

                    Entity harpoon = serverLevel.getEntity(Objects.requireNonNull(itemstack.get(ModDataComponents.HARPOON_SAVED.get())));
                    HarpoonProjectile harpoon2 = (HarpoonProjectile) harpoon;

                    if (player.distanceTo(serverLevel.getEntity((itemstack.get(ModDataComponents.HARPOON_SAVED.get())))) < 80 ) {
                        player.startUsingItem(hand);
                        harpoon2.startPulling(harpoon);
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
            double pickUpRange;
            if (serverLevel.getEntity((stack.get(ModDataComponents.HARPOON_SAVED.get()))) != null) {
                HarpoonProjectile harpoon = (HarpoonProjectile) serverLevel.getEntity((stack.get(ModDataComponents.HARPOON_SAVED.get())));
                if (harpoon.pullTimer < 20) ++harpoon.pullTimer;
                if (harpoon.hasImpacted || harpoon.pullTimer > 19) pickUpRange = 2;
                else pickUpRange = 0.5;

                if (entity.distanceTo(serverLevel.getEntity((stack.get(ModDataComponents.HARPOON_SAVED.get())))) < pickUpRange && !entity.isSpectator()) {
                    // delete saved entity
                    serverLevel.getEntity(Objects.requireNonNull(stack.get(ModDataComponents.HARPOON_SAVED.get()))).discard();
                    // Set saved entity to null
                    stack.set(ModDataComponents.HARPOON_SAVED, null);
                    // allow to throw again
                    stack.set(ModDataComponents.HARPOON_THROWN, false);
                    // cooldown
                    if (entity instanceof Player player) player.getCooldowns().addCooldown(this, 30);

                }
            }
        }

    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (stack.get(ModDataComponents.HARPOON_THROWN)) return 500000;
        else return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        if (stack.get(ModDataComponents.HARPOON_THROWN) == true) return UseAnim.BOW;
        else return UseAnim.SPEAR;
    }

}
