package sircow.preservedinferno.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
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
            @NotNull ItemDisplayContext displayContext,
            PoseStack poseStack,
            @NotNull SubmitNodeCollector nodeCollector,
            int packedLight,
            int packedOverlay,
            boolean hasFoil,
            int outlineColor
    ) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        nodeCollector.submitModelPart(
                this.model.root(), poseStack, this.model.renderType(CopperTridentModel.TEXTURE), packedLight, packedOverlay, null, false, hasFoil, -1, null, outlineColor
        );
        poseStack.popPose();
    }

    @Override
    public void getExtents(@NonNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(poseStack, consumer);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<CopperTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new CopperTridentSpecialRenderer.Unbaked());

        @Override
        public @NotNull MapCodec<CopperTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new CopperTridentSpecialRenderer(new CopperTridentModel(context.entityModelSet().bakeLayer(COPPER_TRIDENT)));
        }
    }
}
