package sircow.preservedinferno.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringInductorRailBlock extends InductorRailBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringInductorRailBlock> CODEC = RecordCodecBuilder.mapCodec((properties) -> properties.group(WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringInductorRailBlock::getAge), propertiesCodec()).apply(properties, WeatheringInductorRailBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    @Override
    public @NotNull MapCodec<WeatheringInductorRailBlock> codec() {
        return CODEC;
    }

    public WeatheringInductorRailBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource randomSource) {
        this.changeOverTime(state, level, pos, randomSource);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }
}
