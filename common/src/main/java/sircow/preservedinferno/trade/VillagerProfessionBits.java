package sircow.preservedinferno.trade;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public class VillagerProfessionBits {
    public static int getBit(ResourceKey<VillagerProfession> profKey) {
        if (profKey == VillagerProfession.ARMORER) return 0;
        if (profKey == VillagerProfession.BUTCHER) return 1;
        if (profKey == VillagerProfession.CARTOGRAPHER) return 2;
        if (profKey == VillagerProfession.CLERIC) return 3;
        if (profKey == VillagerProfession.FARMER) return 4;
        if (profKey == VillagerProfession.FISHERMAN) return 5;
        if (profKey == VillagerProfession.FLETCHER) return 6;
        if (profKey == VillagerProfession.LEATHERWORKER) return 7;
        if (profKey == VillagerProfession.LIBRARIAN) return 8;
        if (profKey == VillagerProfession.MASON) return 9;
        if (profKey == VillagerProfession.SHEPHERD) return 10;
        if (profKey == VillagerProfession.TOOLSMITH) return 11;
        if (profKey == VillagerProfession.WEAPONSMITH) return 12;
        return -1;
    }

    public static boolean hasAll(int bitMask) {
        return bitMask == 0b1111111111111;
    }
}
