package sircow.preservedinferno.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.RegisterItemChecker;
import sircow.preservedinferno.trigger.ModTriggers;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    // modify axe attackDamage
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;axe(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"), index = 1)
    private static float preserved_inferno$modifyAttackDamage(float attackDamage) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.AXES.contains(RegisterItemChecker.itemName)) {
                if (Set.of("iron_axe", "copper_axe").contains(RegisterItemChecker.itemName)) {
                    attackDamage = 4.0F;
                }
                else if (Objects.equals(RegisterItemChecker.itemName, "golden_axe")) {
                    attackDamage = 6.0F;
                }
                else if (Set.of("wooden_axe", "stone_axe").contains(RegisterItemChecker.itemName)) {
                    attackDamage = 3.0F;
                }
            }
        }
        return attackDamage;
    }
    // modify axe attackSpeed
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;axe(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"), index = 2, remap = false)
    private static float preserved_inferno$modifyAttackSpeed(float attackSpeed) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.AXES.contains(RegisterItemChecker.itemName)) {
                attackSpeed = -2.8F;
            }
            RegisterItemChecker.flip = false;
        }
        return attackSpeed;
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void preserved_inferno$beforeSetBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, Level level, BlockPos blockPos, Player player, Optional<BlockState> optional, ItemStack itemStack) {
        if (optional.isEmpty()) return;

        BlockState before = level.getBlockState(blockPos);

        if (before.getBlock() instanceof WeatheringCopper) {
            Optional<BlockState> prev = WeatheringCopper.getPrevious(before);

            if (prev.isPresent() && prev.get().getBlock() == optional.get().getBlock()) {
                Constants.LOG.info("yes");
                if (player instanceof ServerPlayer serverPlayer) {
                    ModTriggers.SCRAPE_COPPER.trigger(serverPlayer);
                }
            }
        }
    }
}
