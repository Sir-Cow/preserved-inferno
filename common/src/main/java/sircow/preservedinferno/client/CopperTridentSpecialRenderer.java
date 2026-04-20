package sircow.preservedinferno.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;

import java.util.function.Consumer;

public class CopperTridentSpecialRenderer implements NoDataSpecialModelRenderer {
    private final CopperTridentModel model;
    public static final ModelLayerLocation COPPER_TRIDENT = new ModelLayerLocation(Constants.id("copper_trident"), "main");

    public CopperTridentSpecialRenderer(CopperTridentModel model) {
        this.model = model;
    }

    @Override
    public void submit(
            final @NonNull PoseStack poseStack,
            final SubmitNodeCollector submitNodeCollector,
            final int lightCoords,
            final int overlayCoords,
            final boolean hasFoil,
            final int outlineColor
    ) {
        submitNodeCollector.submitModelPart(
                this.model.root(), poseStack, this.model.renderType(CopperTridentModel.TEXTURE), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor
        );
    }

    @Override
    public void getExtents(final @NonNull Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<CopperTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new CopperTridentSpecialRenderer.Unbaked());

        @Override
        public @NonNull MapCodec<CopperTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        public CopperTridentSpecialRenderer bake(final SpecialModelRenderer.BakingContext context) {
            return new CopperTridentSpecialRenderer(new CopperTridentModel(context.entityModelSet().bakeLayer(COPPER_TRIDENT)));
        }
    }
}
