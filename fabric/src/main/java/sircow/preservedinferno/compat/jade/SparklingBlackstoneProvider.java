package sircow.preservedinferno.compat.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.block.custom.SparklingBlackstoneBlock;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class SparklingBlackstoneProvider implements IBlockComponentProvider {
    public static final SparklingBlackstoneProvider INSTANCE = new SparklingBlackstoneProvider();

    @Override
    public void appendTooltip(@NonNull ITooltip tooltip, @NonNull BlockAccessor accessor, @NonNull IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        int stage = state.getValue(SparklingBlackstoneBlock.STAGE);
        Component text;
        switch (stage) {
            case 0 -> text = Component.literal("0%").withStyle(ChatFormatting.WHITE);
            case 1 -> text = Component.literal("25%").withStyle(ChatFormatting.WHITE);
            case 2 -> text = Component.literal("50%").withStyle(ChatFormatting.WHITE);
            case 3 -> text = Component.literal("75%").withStyle(ChatFormatting.WHITE);
            case 4 -> text = Component.literal("Mature").withStyle(ChatFormatting.GREEN);
            default -> text = Component.literal(stage + "");
        }
        tooltip.add(Component.translatable("tooltip.jade.crop_growth", text));
    }

    @Override
    public @NonNull Identifier getUid() {
        return JadePlugin.SPARKLING_BLACKSTONE_STAGE;
    }
}
