package sircow.preservedinferno.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieVillager.class)
public abstract class ZombieVillagerMixin {
    @Inject(method = "initializeVillagerData", at = @At("HEAD"), cancellable = true)
    private void preserved_inferno$joblessVillager(CallbackInfoReturnable<VillagerData> cir) {
        ZombieVillager self = (ZombieVillager)(Object)this;
        Level level = self.level();

        Holder<VillagerProfession> noneProfession = level.registryAccess().lookupOrThrow(Registries.VILLAGER_PROFESSION).getOrThrow(VillagerProfession.NONE);

        VillagerData data = Villager.createDefaultVillagerData().withType(level.registryAccess(), VillagerType.byBiome(level.getBiome(self.blockPosition()))).withProfession(noneProfession);

        cir.setReturnValue(data);
    }
}
