package sircow.preservedinferno.mixin;

import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GameRules.class)
public class GameRulesMixin {
    // change default max minecart speed
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=max_minecart_speed")), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;registerInteger(Ljava/lang/String;Lnet/minecraft/world/level/gamerules/GameRuleCategory;IIILnet/minecraft/world/flag/FeatureFlagSet;)Lnet/minecraft/world/level/gamerules/GameRule;", ordinal = 0), index = 2)
    private static int pinferno$maxMinecartSpeedGameRule(int val) {
        return 32;
    }
    // set the playersSleepingPercentage gamerule to 0 by default
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=players_sleeping_percentage")), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;registerInteger(Ljava/lang/String;Lnet/minecraft/world/level/gamerules/GameRuleCategory;II)Lnet/minecraft/world/level/gamerules/GameRule;", ordinal = 0), index = 2)
    private static int pinferno$playersSleepingPercentageGameRule(int val) {
        return 0;
    }
}
