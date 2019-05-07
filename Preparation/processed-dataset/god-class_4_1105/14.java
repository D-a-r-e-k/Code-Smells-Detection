/** Get the position of a photon along an axis. */
private float axisPosition(int index, int axis) {
    switch(axis) {
        case 0:
            return workspace[index].x;
        case 1:
            return workspace[index].y;
        default:
            return workspace[index].z;
    }
}
