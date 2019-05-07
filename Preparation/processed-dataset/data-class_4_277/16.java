/** Determine the surface lighting at a point due to the photons in this map.
      @param pos      the position near which to locate photons
      @param spec     the surface properties at the point being evaluated
      @param normal   the surface normal at the point being evaluated
      @param viewDir  the direction from which the surface is being viewed
      @param front    true if the surface is being viewed from the front
      @param light    the total lighting contribution will be stored in this
      @param pmc      the PhotonMapContext from which this is being invoked
  */
public void getLight(Vec3 pos, TextureSpec spec, Vec3 normal, Vec3 viewDir, boolean front, RGBColor light, PhotonMapContext pmc) {
    light.setRGB(0.0f, 0.0f, 0.0f);
    if (photon.length == 0)
        return;
    PhotonList nearbyPhotons = pmc.nearbyPhotons;
    RGBColor tempColor = pmc.tempColor;
    RGBColor tempColor2 = pmc.tempColor2;
    Vec3 tempVec = pmc.tempVec;
    float startCutoff2 = (float) (Math.sqrt(pmc.lastCutoff2) + pos.distance(pmc.lastPos));
    startCutoff2 *= startCutoff2;
    if (startCutoff2 > cutoffDist2)
        startCutoff2 = cutoffDist2;
    nearbyPhotons.init(startCutoff2);
    findPhotons(pos, 0, pmc);
    pmc.lastPos.set(pos);
    pmc.lastCutoff2 = nearbyPhotons.cutoff2;
    if (nearbyPhotons.numFound == 0)
        return;
    float r2inv = 1.0f / nearbyPhotons.cutoff2;
    boolean hilight = false;
    if (spec.hilight.getMaxComponent() > rt.minRayIntensity) {
        hilight = true;
        tempColor2.setRGB(0.0f, 0.0f, 0.0f);
    }
    for (int i = 0; i < nearbyPhotons.numFound; i++) {
        Photon p = nearbyPhotons.photon[i];
        Vec3 dir = direction[p.direction & 0xFFFF];
        double dot = normal.dot(dir);
        if ((front && dot < -1.0e-10) || (!front && dot > 1.0e-10)) {
            tempColor.setERGB(p.ergb);
            float x = nearbyPhotons.dist2[i] * r2inv;
            if (filter == 2)
                tempColor.scale(x * (x - 2.0f) + 1.0f);
            else if (filter == 1)
                tempColor.scale(1.0f - x * x);
            light.add(tempColor);
            if (hilight) {
                tempVec.set(dir);
                tempVec.add(viewDir);
                tempVec.normalize();
                double viewDot = (front ? -tempVec.dot(normal) : tempVec.dot(normal));
                if (viewDot > 0.0) {
                    float scale = (float) FastMath.pow(viewDot, (int) ((1.0 - spec.roughness) * 128.0) + 1);
                    tempColor2.add(tempColor.getRed() * scale, tempColor.getGreen() * scale, tempColor.getBlue() * scale);
                }
            }
        }
    }
    light.multiply(spec.diffuse);
    if (hilight) {
        tempColor2.multiply(spec.hilight);
        light.add(tempColor2);
    }
    light.scale(lightScale / (Math.PI * nearbyPhotons.cutoff2));
}
