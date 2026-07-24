package sircow.preservedinferno.compat.jade;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

public class PreservedCauldronComponentProvider implements IBlockComponentProvider {
    public static final PreservedCauldronComponentProvider INSTANCE = new PreservedCauldronComponentProvider();

    @Override
    public void appendTooltip(@NonNull ITooltip tooltip, @NonNull BlockAccessor accessor, @NonNull IPluginConfig config) {
        PreservedCauldronDataProvider.Data data = PreservedCauldronDataProvider.INSTANCE.decodeFromData(accessor).orElse(null);
        if (data == null) return;

        ItemStack input = data.inventory().get(0);
        ItemStack input2 = data.inventory().get(1);
        ItemStack output = data.inventory().get(2);

        if (input.isEmpty() && input2.isEmpty() && output.isEmpty()) return;

        tooltip.add(JadeUI.item(input).alignSelfCenter());
        tooltip.append(JadeUI.progressArrow(data.total() == 0 ? 0 : (float) data.progress() / data.total()).alignSelfCenter().settings($ -> $.paddingHorizontal(3)));
        tooltip.append(JadeUI.item(output).alignSelfCenter());
        tooltip.append(JadeUI.spacer(18, 0));
        tooltip.append(JadeUI.item(input2).alignSelfCenter());
    }

    @Override
    public @NonNull Identifier getUid() {
        return JadePlugin.CAULDRON_PROGRESS;
    }
}
