package sircow.preservedinferno.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.util.Unit;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import sircow.preservedinferno.Constants;

import java.util.function.Consumer;

public class CopperTridentSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(null, null, new Vector3f(1.0F, -1.0F, -1.0F), null);
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
        submitNodeCollector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, CopperTridentModel.TEXTURE, lightCoords, overlayCoords, outlineColor, null);
        if (hasFoil) {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, outlineColor, null);
        }
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
