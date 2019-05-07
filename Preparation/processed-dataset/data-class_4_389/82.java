public void extinguishLocation(int location) {
    int flag = ~(1 << location);
    burningLocations &= flag;
}
