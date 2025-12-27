package net.lavathelatvian.lavas_trinkets.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NeedleItem extends SwordItem {
    public NeedleItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
// Grab the item stack in hand
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (!player.isFallFlying()) {
                player.setDeltaMovement(new Vec3(
                        (player.getLookAngle().x + player.getDeltaMovement().x()) * (1),
                        (player.getLookAngle().y + player.getDeltaMovement().y()) / 2,
                        (player.getLookAngle().z + player.getDeltaMovement().z()) * (1)
                ));
                player.hurtMarked = true;
                player.getCooldowns().addCooldown(this, 100);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
