package sircow.preservedinferno.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import sircow.preservedinferno.Constants;

import java.util.Set;

public class CopperTridentSpecialRenderer implements NoDataSpecialModelRenderer {
    private final CopperTridentModel model;

    public CopperTridentSpecialRenderer(CopperTridentModel model) {
        this.model = model;
    }

    @Override
    public void render(@NotNull ItemDisplayContext displayContext, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource, this.model.renderType(Constants.id("textures/entity/copper_trident.png")), false, hasFoilType);
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void getExtents(@NotNull Set<Vector3f> output) {
        PoseStack poseStack = new PoseStack();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<CopperTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new CopperTridentSpecialRenderer.Unbaked());

        @Override
        public @NotNull MapCodec<CopperTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new CopperTridentSpecialRenderer(new CopperTridentModel(modelSet.bakeLayer(ThrownCopperTridentRenderer.COPPER_TRIDENT)));
        }
    }
}
