package sircow.preservedinferno.fluid;

public enum CauldronFluid {
    EMPTY,
    HONEY,
    LAVA,
    MILK,
    SNOW,
    WATER;

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
