package sircow.preservedinferno.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringInductorRailBlock extends InductorRailBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringInductorRailBlock> CODEC = RecordCodecBuilder.mapCodec((p_368450_) -> p_368450_.group(WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringInductorRailBlock::getAge), propertiesCodec()).apply(p_368450_, WeatheringInductorRailBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    @Override
    public MapCodec<WeatheringInductorRailBlock> codec() {
        return CODEC;
    }

    public WeatheringInductorRailBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        this.changeOverTime(state, level, pos, randomSource);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
