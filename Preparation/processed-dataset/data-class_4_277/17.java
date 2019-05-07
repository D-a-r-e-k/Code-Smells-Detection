/** Determine the volume lighting at a point due to the photons in this map.
      @param pos      the position near which to locate photons
      @param spec     the material properties at the point being evaluated
      @param viewDir  the direction from which the material is being viewed
      @param light    the total lighting contribution will be stored in this
      @param pmc      the PhotonMapContext from which this is being invoked
  */
public void getVolumeLight(Vec3 pos, MaterialSpec spec, Vec3 viewDir, RGBColor light, PhotonMapContext pmc) {
    light.setRGB(0.0f, 0.0f, 0.0f);
    if (photon.length == 0)
        return;
    PhotonList nearbyPhotons = pmc.nearbyPhotons;
    RGBColor tempColor = pmc.tempColor;
    float startCutoff2 = pmc.lastCutoff2 + (float) pos.distance2(pmc.lastPos);
    if (startCutoff2 > cutoffDist2)
        startCutoff2 = cutoffDist2;
    nearbyPhotons.init(startCutoff2);
    findPhotons(pos, 0, pmc);
    pmc.lastPos.set(pos);
    pmc.lastCutoff2 = nearbyPhotons.cutoff2;
    if (nearbyPhotons.numFound == 0)
        return;
    double eccentricity = spec.eccentricity;
    double ec2 = eccentricity * eccentricity;
    for (int i = 0; i < nearbyPhotons.numFound; i++) {
        Photon p = nearbyPhotons.photon[i];
        tempColor.setERGB(p.ergb);
        if (eccentricity != 0.0) {
            Vec3 dir = direction[p.direction & 0xFFFF];
            double dot = dir.dot(viewDir);
            double fatt = (1.0 - ec2) / Math.pow(1.0 + ec2 + 2.0 * eccentricity * dot, 1.5);
            tempColor.scale(fatt);
        }
        light.add(tempColor);
    }
    light.scale(lightScale / ((4.0 / 3.0) * Math.PI * Math.pow(nearbyPhotons.cutoff2, 1.5)));
}
