@Override
public int getMaxElevationDown() {
    // WIGEs can go down as far as they want  
    // 50 is a pretty arbitrary max amount, but that's also the  
    // highest elevation for VTOLs, so I'll just use that  
    if ((getElevation() > 0) && (getMovementMode() == EntityMovementMode.WIGE)) {
        return 50;
    }
    return super.getMaxElevationDown();
}
