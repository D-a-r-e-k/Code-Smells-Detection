public boolean isLocationBurning(int location) {
    int flag = (1 << location);
    return (burningLocations & flag) == flag;
}
