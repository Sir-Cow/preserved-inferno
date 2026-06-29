package sircow.preservedinferno.item.custom;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.entity.custom.ThrownPreservedLingeringBottle;
import sircow.preservedinferno.entity.custom.ThrownPreservedSplashBottle;
import sircow.preservedinferno.item.ModItems;

public class PreservedBottleItem extends Item implements ProjectileItem {
    public PreservedBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean lingering = isLingering(stack);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), lingering ? SoundEvents.LINGERING_POTION_THROW : SoundEvents.SPLASH_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (level instanceof ServerLevel serverLevel) {
            if (lingering) Projectile.spawnProjectileFromRotation(ThrownPreservedLingeringBottle::new, serverLevel, stack, player, -20.0F, 0.5F, 1.0F);
            else Projectile.spawnProjectileFromRotation(ThrownPreservedSplashBottle::new, serverLevel, stack, player, -20.0F, 0.5F, 1.0F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull Projectile asProjectile(@NonNull Level level, @NonNull Position pos, @NonNull ItemStack stack, @NonNull Direction direction) {
        if (isLingering(stack)) return new ThrownPreservedLingeringBottle(level, pos.x(), pos.y(), pos.z(), stack);
        return new ThrownPreservedSplashBottle(level, pos.x(), pos.y(), pos.z(), stack);
    }

    private static boolean isLingering(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.LINGERING_LAVA_BOTTLE || item == ModItems.LINGERING_MILK_BOTTLE || item == ModItems.LINGERING_HONEY_BOTTLE;
    }

    @Override
    public ProjectileItem.@NonNull DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder()
                .uncertainty(ProjectileItem.DispenseConfig.DEFAULT.uncertainty() * 0.5F)
                .power(ProjectileItem.DispenseConfig.DEFAULT.power() * 1.25F)
                .build();
    }
}
