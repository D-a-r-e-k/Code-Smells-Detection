/**
     * Tanks have all sorts of prohibited terrain.
     */
@Override
public boolean isHexProhibited(IHex hex) {
    if (hex.containsTerrain(Terrains.IMPASSABLE)) {
        return true;
    }
    if (hex.containsTerrain(Terrains.SPACE) && doomedInSpace()) {
        return true;
    }
    switch(movementMode) {
        case TRACKED:
            return (hex.terrainLevel(Terrains.WOODS) > 1) || ((hex.terrainLevel(Terrains.WATER) > 0) && !hex.containsTerrain(Terrains.ICE)) || hex.containsTerrain(Terrains.JUNGLE) || (hex.terrainLevel(Terrains.MAGMA) > 1) || (hex.terrainLevel(Terrains.ROUGH) > 1);
        case WHEELED:
            return hex.containsTerrain(Terrains.WOODS) || hex.containsTerrain(Terrains.ROUGH) || ((hex.terrainLevel(Terrains.WATER) > 0) && !hex.containsTerrain(Terrains.ICE)) || hex.containsTerrain(Terrains.RUBBLE) || hex.containsTerrain(Terrains.MAGMA) || hex.containsTerrain(Terrains.JUNGLE) || (hex.terrainLevel(Terrains.SNOW) > 1) || (hex.terrainLevel(Terrains.GEYSER) == 2);
        case HOVER:
            return hex.containsTerrain(Terrains.WOODS) || hex.containsTerrain(Terrains.JUNGLE) || (hex.terrainLevel(Terrains.MAGMA) > 1) || (hex.terrainLevel(Terrains.ROUGH) > 1);
        case NAVAL:
        case HYDROFOIL:
            return (hex.terrainLevel(Terrains.WATER) <= 0) || hex.containsTerrain(Terrains.ICE);
        case SUBMARINE:
            return (hex.terrainLevel(Terrains.WATER) <= 0);
        case WIGE:
            return (hex.containsTerrain(Terrains.WOODS) || hex.containsTerrain(Terrains.BUILDING));
        default:
            return false;
    }
}
