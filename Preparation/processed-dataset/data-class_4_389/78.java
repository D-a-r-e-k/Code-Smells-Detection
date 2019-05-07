public void setOnFire(boolean inferno) {
    infernoFire |= inferno;
    burningLocations = (1 << locations()) - 1;
    extinguishLocation(LOC_BODY);
}
