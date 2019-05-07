/** Find the light being scattered by a point in scattering material.
      The surface properties for the given point should be in surfSpec[treeDepth-1], and
      the resulting color is returned in color[treeDepth-1].

      @param rt                contains information for the thread currently being executed
      @param treeDepth         the current ray tree depth
      @param node              the octree node containing the point
      @param eccentricity      the eccentricity of the material
      @param totalDist         the distance traveled from the viewpoint
      @param currentMaterial   the MaterialMapping through which the ray is being propagated
      @param prevMaterial      the MaterialMapping the ray was passing through before entering material
      @param currentMatTrans   the transform to local coordinates for the current material
      @param prevMatTrans      the transform to local coordinates for the previous material
   */
protected void getScatteredLight(RaytracerContext rt, int treeDepth, OctreeNode node, double eccentricity, double totalDist, MaterialMapping currentMaterial, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans) {
    int i;
    RGBColor filter = rt.rayIntensity[treeDepth], lightColor = rt.color[treeDepth];
    Ray r = rt.ray[treeDepth];
    Vec3 dir, pos = r.origin, viewDir = rt.ray[treeDepth - 1].direction;
    double distToLight, fatt, dot;
    double ec2 = eccentricity * eccentricity;
    Light lt;
    rt.tempColor2.setRGB(0.0f, 0.0f, 0.0f);
    dir = r.getDirection();
    for (i = light.length - 1; i >= 0; i--) {
        lt = light[i].getLight();
        distToLight = light[i].findRayToLight(pos, r, -1);
        r.newID();
        // Now scan through the list of objects, and see if the light is blocked. 
        lt.getLight(lightColor, light[i].getCoords().toLocal().times(pos));
        lightColor.multiply(filter);
        if (eccentricity != 0.0 && lt.getType() != Light.TYPE_AMBIENT) {
            dot = dir.dot(viewDir);
            fatt = (1.0 - ec2) / Math.pow(1.0 + ec2 - 2.0 * eccentricity * dot, 1.5);
            lightColor.scale(fatt);
        }
        if (lightColor.getRed() < minRayIntensity && lightColor.getGreen() < minRayIntensity && lightColor.getBlue() < minRayIntensity)
            continue;
        if (lt.getType() == Light.TYPE_AMBIENT || lt.getType() == Light.TYPE_SHADOWLESS || traceLightRay(r, lt, treeDepth, node, lightNode[i], distToLight, totalDist, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans))
            rt.tempColor2.add(lightColor);
    }
    rt.color[treeDepth].copy(rt.tempColor2);
}
