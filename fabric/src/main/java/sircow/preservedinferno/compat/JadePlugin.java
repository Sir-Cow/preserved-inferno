package sircow.preservedinferno.compat;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.CauldronBlock;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;
import sircow.preservedinferno.block.custom.SparklingBlackstoneBlock;
import sircow.preservedinferno.block.entity.PreservedCauldronBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    public static final Identifier CAULDRON_PROGRESS = Constants.id("cauldron_progress");
    public static final Identifier SPARKLING_BLACKSTONE_STAGE = Constants.id("sparkling_blackstone_stage");
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(PreservedCauldronDataProvider.INSTANCE, PreservedCauldronBlockEntity.class);
    }

    @Override
    public void registerClient(@NonNull IWailaClientRegistration registration) {
        registration.registerBlockComponent(PreservedCauldronComponentProvider.INSTANCE, CauldronBlock.class);
        registration.registerBlockComponent(SparklingBlackstoneProvider.INSTANCE, SparklingBlackstoneBlock.class);
    }
}
