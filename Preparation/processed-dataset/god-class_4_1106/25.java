/** Find the direct lighting contribution to the surface color.  The surface properties for the given point
      should be in surfSpec[treeDepth], and the resulting color is returned in color[treeDepth].

      @param rt                 contains information for the thread currently being executed
      @param pos                the point for which light is being calculated
      @param normal             the local surface normal
      @param front              true if the surface is being viewed from the front
      @param viewDir            the direction from which the surface is being viewed
      @param treeDepth          the current ray tree depth
      @param node               the octree node containing pos
      @param rayNumber          the number of the ray within the pixel (for distribution ray tracing)
      @param totalDist          the distance traveled from the viewpoint
      @param currentMaterial    the MaterialMapping at the point (may be null)
      @param prevMaterial       the MaterialMapping the ray was passing through before entering currentMaterial
      @param currentMatTrans    the transform to local coordinates for the current material
      @param prevMatTrans       the transform to local coordinates for the previous material
      @param diffuse            true if this ray has been diffusely reflected since leaving the eye
  */
protected void getDirectLight(RaytracerContext rt, Vec3 pos, Vec3 normal, boolean front, Vec3 viewDir, int treeDepth, OctreeNode node, int rayNumber, double totalDist, MaterialMapping currentMaterial, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans, boolean diffuse) {
    int i;
    RGBColor lightColor = rt.color[treeDepth + 1], finalColor = rt.color[treeDepth];
    TextureSpec spec = rt.surfSpec[treeDepth];
    Vec3 dir;
    Ray r = rt.ray[treeDepth + 1];
    double sign, distToLight, dot;
    boolean hilight;
    Light lt;
    // Start with the ambient and emissive contributions. 
    finalColor.copy(ambColor);
    finalColor.multiply(spec.diffuse);
    finalColor.add(spec.emissive);
    // If this ray was sampling ambient occlusion, we can stop now. 
    if (giMode == GI_AMBIENT_OCCLUSION && diffuse)
        return;
    // If appropriate, get the light from photon maps. 
    if (giMode == GI_HYBRID && diffuse) {
        rt.globalMap.getLight(pos, spec, normal, viewDir, front, lightColor);
        finalColor.add(lightColor);
        return;
    }
    if (giMode == GI_PHOTON) {
        rt.globalMap.getLight(pos, spec, normal, viewDir, front, lightColor);
        finalColor.add(lightColor);
    }
    if (caustics) {
        rt.causticsMap.getLight(pos, spec, normal, viewDir, front, lightColor);
        finalColor.add(lightColor);
    }
    // Now loop over the list of lights. 
    dir = r.getDirection();
    sign = front ? 1.0 : -1.0;
    hilight = (spec.hilight.getRed() != 0.0 || spec.hilight.getGreen() != 0.0 || spec.hilight.getBlue() != 0.0);
    for (i = light.length - 1; i >= 0; i--) {
        lt = light[i].getLight();
        distToLight = light[i].findRayToLight(pos, r, rayNumber + treeDepth + 1);
        r.newID();
        // Now scan through the list of objects, and see if the light is blocked. 
        if (lt.getType() == Light.TYPE_AMBIENT)
            dot = 1.0;
        else
            dot = sign * dir.dot(normal);
        if (dot > 0.0) {
            lt.getLight(lightColor, light[i].getCoords().toLocal().times(pos));
            if (lightColor.getRed() * (spec.diffuse.getRed() * dot + spec.hilight.getRed()) < minRayIntensity && lightColor.getGreen() * (spec.diffuse.getGreen() * dot + spec.hilight.getGreen()) < minRayIntensity && lightColor.getBlue() * (spec.diffuse.getBlue() * dot + spec.hilight.getBlue()) < minRayIntensity)
                continue;
            if (lt.getType() == Light.TYPE_AMBIENT || lt.getType() == Light.TYPE_SHADOWLESS || traceLightRay(r, lt, treeDepth + 1, node, lightNode[i], distToLight, totalDist, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans)) {
                RGBColor tempColor = rt.tempColor;
                tempColor.copy(lightColor);
                tempColor.multiply(spec.diffuse);
                tempColor.scale(dot);
                finalColor.add(tempColor);
                if (hilight) {
                    dir.subtract(viewDir);
                    dir.normalize();
                    dot = sign * dir.dot(normal);
                    if (dot > 0.0) {
                        tempColor.copy(lightColor);
                        tempColor.multiply(spec.hilight);
                        tempColor.scale(FastMath.pow(dot, (int) ((1.0 - spec.roughness) * 128.0) + 1));
                        finalColor.add(tempColor);
                    }
                }
            }
        }
    }
}
