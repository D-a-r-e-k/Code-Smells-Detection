/** Find the photons nearest to a given point.
      @param pos      the position near which to locate photons
      @param index    the point in the map from which to start searching
      @param pmc      the PhotonMapContext from which this is being invoked
  */
private void findPhotons(Vec3 pos, int index, PhotonMapContext pmc) {
    Photon p = photon[index];
    float dx = p.x - (float) pos.x, dy = p.y - (float) pos.y, dz = p.z - (float) pos.z;
    float dist2 = dx * dx + dy * dy + dz * dz;
    float delta;
    switch(p.axis) {
        case 0:
            delta = dx;
            break;
        case 1:
            delta = dy;
            break;
        default:
            delta = dz;
    }
    if (delta > 0.0f) {
        int child = (index << 1) + 1;
        if (child < photon.length) {
            findPhotons(pos, child, pmc);
            delta *= delta;
            child++;
            if (child < photon.length && delta < pmc.nearbyPhotons.cutoff2)
                findPhotons(pos, child, pmc);
        }
    } else {
        int child = (index << 1) + 2;
        if (child < photon.length)
            findPhotons(pos, child, pmc);
        delta *= delta;
        child--;
        if (child < photon.length && delta < pmc.nearbyPhotons.cutoff2)
            findPhotons(pos, child, pmc);
    }
    if (dist2 < pmc.nearbyPhotons.cutoff2)
        pmc.nearbyPhotons.addPhoton(p, dist2);
}
