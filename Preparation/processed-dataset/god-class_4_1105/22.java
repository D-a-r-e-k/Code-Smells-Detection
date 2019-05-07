private float median(int index, int axis) {
    switch(axis) {
        case 0:
            return photon[index].x;
        case 1:
            return photon[index].y;
        default:
            return photon[index].z;
    }
}
