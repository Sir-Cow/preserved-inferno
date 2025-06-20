package sircow.preservedinferno.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedinferno.other.HeatAccessor;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.AFTER))
    private <S extends EntityRenderState> void preserved_inferno$injectFlameLayer(
            S renderState, double xOffset, double yOffset, double zOffset, PoseStack poseStack, MultiBufferSource buffer, int packedLight, EntityRenderer<?, S> renderer, CallbackInfo ci
    ) {
        if (renderState instanceof PlayerRenderState playerRenderState) {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(playerRenderState.id);
                if (entity instanceof AbstractClientPlayer player
                        && player instanceof HeatAccessor accessor
                        && accessor.preserved_inferno$getHeat() >= 100
                        && !player.isOnFire()
                        && !player.isCreative()
                ) {
                    Quaternionf cameraOrientation = ((EntityRenderDispatcher)(Object)this).cameraOrientation();
                    renderFlame(poseStack, buffer, renderState, Mth.rotationAroundAxis(Mth.Y_AXIS, cameraOrientation, new Quaternionf()));
                }
            }
        }
    }

    @Unique
    private void renderFlame(PoseStack poseStack, MultiBufferSource bufferSource, EntityRenderState renderState, Quaternionf cameraRotation) {
        TextureAtlasSprite fire0 = ModelBakery.FIRE_0.sprite();
        TextureAtlasSprite fire1 = ModelBakery.FIRE_1.sprite();
        poseStack.pushPose();
        float f = renderState.boundingBoxWidth * 1.4F;
        poseStack.scale(f, f, f);
        float g = 0.5F;
        float h = 0.0F;
        float i = renderState.boundingBoxHeight / f;
        float j = 0.0F;
        poseStack.mulPose(cameraRotation);
        poseStack.translate(0.0F, 0.0F, 0.3F - (int)i * 0.02F);
        float k = 0.0F;
        int l = 0;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());

        for (PoseStack.Pose pose = poseStack.last(); i > 0.0F; l++) {
            TextureAtlasSprite textureAtlasSprite3 = l % 2 == 0 ? fire0 : fire1;
            float m = textureAtlasSprite3.getU0();
            float n = textureAtlasSprite3.getV0();
            float o = textureAtlasSprite3.getU1();
            float p = textureAtlasSprite3.getV1();
            if (l / 2 % 2 == 0) {
                float q = o;
                o = m;
                m = q;
            }

            addFireVertex(pose, vertexConsumer, -g - 0.0F, 0.0F - j, k, o, p);
            addFireVertex(pose, vertexConsumer, g - 0.0F, 0.0F - j, k, m, p);
            addFireVertex(pose, vertexConsumer, g - 0.0F, 1.4F - j, k, m, n);
            addFireVertex(pose, vertexConsumer, -g - 0.0F, 1.4F - j, k, o, n);
            i -= 0.45F;
            j -= 0.45F;
            g *= 0.9F;
            k -= 0.03F;
        }

        poseStack.popPose();
    }

    @Unique
    private static void addFireVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v) {
        buffer.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setUv1(0, 10).setLight(240).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
