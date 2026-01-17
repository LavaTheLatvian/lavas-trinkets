package net.lavathelatvian.lavas_trinkets.item.custom;

import net.lavathelatvian.lavas_trinkets.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RedemptionCrystalItem extends Item {
    public RedemptionCrystalItem(Properties properties) {super(properties);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.getLastDeathLocation().isPresent()) {
            Optional<GlobalPos> deathData = player.getLastDeathLocation();
            if (player instanceof ServerPlayer serverPlayer) {
                GlobalPos globalPos = deathData.get();

                ServerLevel targetLevel = serverPlayer.server.getLevel(globalPos.dimension());
                if (targetLevel == null) return InteractionResultHolder.fail(itemstack);

                BlockPos pos = globalPos.pos();
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                            pos.getX(), pos.getY(), pos.getZ(), 100, 0.25, 0.25, 0.25, 1);
                    serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL,
                            player.getX(), player.getY(), player.getZ(), 100, 0.25, 0.25, 0.25, 1);
                }
                serverPlayer.teleportTo(targetLevel, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, serverPlayer.getYRot(), serverPlayer.getXRot());
                level.playSound((Player) null, player, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.0F);
            }


            player.awardStat(Stats.ITEM_USED.get(this));
            itemstack.consume(1, player);
            return InteractionResultHolder.consume(itemstack);
        }
        else {
            player.displayClientMessage(Component.literal("§cTeleport failed - missing death location."), true);
            return InteractionResultHolder.success(itemstack);
        }
    }

}
