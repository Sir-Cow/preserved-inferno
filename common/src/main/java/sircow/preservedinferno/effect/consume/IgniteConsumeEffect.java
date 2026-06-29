package sircow.preservedinferno.effect.consume;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record IgniteConsumeEffect(float seconds)  implements ConsumeEffect {
    public static final MapCodec<IgniteConsumeEffect> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("seconds").forGetter(IgniteConsumeEffect::seconds)).apply(instance, IgniteConsumeEffect::new)
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, IgniteConsumeEffect> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, IgniteConsumeEffect::seconds, IgniteConsumeEffect::new);

    @Override
    public @NonNull Type<? extends ConsumeEffect> getType() {
        return ModConsumeEffects.IGNITE;
    }

    @Override
    public boolean apply(@NonNull Level level, @NonNull ItemStack stack, LivingEntity user) {
        user.igniteForSeconds(this.seconds);
        return true;
    }
}
