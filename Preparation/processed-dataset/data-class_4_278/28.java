/** Propagate a ray through a material, and determine how much light is removed (due to
      absorption and outscattering) and added (due to emission and inscattering).
      <p>
      On exit, color[treeDepth] is set equal to the emitted color, and rayIntensity[treeDepth]
      is reduced by the appropriate factor to account for the absorbed light.

      @param r                 the ray being propagated
      @param node              the octree node containing the ray origin
      @param dist              the distance between the ray origin and the endpoint
      @param material          the MaterialMapping through which the ray is being propagated
      @param prevMaterial      the MaterialMapping the ray was passing through before entering material
      @param currentMatTrans   the transform to local coordinates for the current material
      @param prevMatTrans      the transform to local coordinates for the previous material
      @param emitted           on exit, this contains the light emitted from the material
      @param filter            on exit, this is multiplied by the attenuation factor
      @param treeDepth         the current ray tree depth
      @param totalDist         the distance traveled from the viewpoint
   */
protected void propagateRay(Ray r, OctreeNode node, double dist, MaterialMapping material, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans, RGBColor emitted, RGBColor filter, int treeDepth, double totalDist) {
    boolean scattering = material.isScattering();
    MaterialSpec matSpec = r.rt.matSpec;
    float re, ge, be, rs, gs, bs;
    float rf = filter.getRed(), gf = filter.getGreen(), bf = filter.getBlue();
    if (material instanceof UniformMaterialMapping && !scattering) {
        // The effects of the material can be computed exactly. 
        material.getMaterialSpec(r.origin, matSpec, 0.0, time);
        RGBColor trans = matSpec.transparency, blend = matSpec.color;
        float d = (float) dist;
        if (trans.getRed() == 1.0f)
            rs = 1.0f;
        else
            rs = (float) Math.pow(trans.getRed(), d);
        if (trans.getGreen() == 1.0f)
            gs = 1.0f;
        else
            gs = (float) Math.pow(trans.getGreen(), d);
        if (trans.getBlue() == 1.0f)
            bs = 1.0f;
        else
            bs = (float) Math.pow(trans.getBlue(), d);
        re = blend.getRed() * rf * (1.0f - rs);
        ge = blend.getGreen() * gf * (1.0f - gs);
        be = blend.getBlue() * bf * (1.0f - bs);
        rf *= rs;
        gf *= gs;
        bf *= bs;
    } else {
        // Integrate the material properties by stepping along the ray. 
        Vec3 v = r.rt.ray[treeDepth + 1].origin, origin = r.origin, direction = r.direction;
        double x = 0.0, newx, dx, distToScreen = theCamera.getDistToScreen(), step;
        double origx, origy, origz, dirx, diry, dirz;
        // Find the ray origin and direction in the object's local coordinates. 
        v.set(origin);
        currentMatTrans.transform(v);
        origx = v.x;
        origy = v.y;
        origz = v.z;
        v.set(direction);
        currentMatTrans.transformDirection(v);
        dirx = v.x;
        diry = v.y;
        dirz = v.z;
        // Do the integration. 
        re = ge = be = 0.0f;
        step = stepSize * material.getStepSize();
        do {
            // Find the new point along the ray. 
            dx = step * (1.5 * r.rt.random.nextDouble());
            if (adaptive && totalDist > distToScreen)
                dx *= totalDist / distToScreen;
            newx = x + dx;
            if (newx > dist) {
                dx = dist - x;
                x = dist;
            } else
                x = newx;
            totalDist += dx;
            v.set(origx + dirx * x, origy + diry * x, origz + dirz * x);
            // Find the material properties at that point. 
            material.getMaterialSpec(v, matSpec, dx, time);
            RGBColor trans = matSpec.transparency, blend = matSpec.color;
            // Update the total emission and transmission. 
            if (trans.getRed() == 1.0f)
                rs = 1.0f;
            else
                rs = (float) Math.pow(trans.getRed(), dx);
            if (trans.getGreen() == 1.0f)
                gs = 1.0f;
            else
                gs = (float) Math.pow(trans.getGreen(), dx);
            if (trans.getBlue() == 1.0f)
                bs = 1.0f;
            else
                bs = (float) Math.pow(trans.getBlue(), dx);
            re += blend.getRed() * rf * (1.0f - rs);
            ge += blend.getGreen() * gf * (1.0f - gs);
            be += blend.getBlue() * bf * (1.0f - bs);
            if (scattering) {
                RGBColor rayIntensity = r.rt.rayIntensity[treeDepth + 1];
                rayIntensity.setRGB(rf, gf, bf);
                rayIntensity.multiply(matSpec.scattering);
                if (rayIntensity.getRed() > minRayIntensity || rayIntensity.getGreen() > minRayIntensity || rayIntensity.getBlue() > minRayIntensity) {
                    if (scatterMode == SCATTER_SINGLE || scatterMode == SCATTER_BOTH) {
                        v.set(origin.x + direction.x * x, origin.y + direction.y * x, origin.z + direction.z * x);
                        while (node != null && !node.contains(v)) node = node.findNextNode(r);
                        if (node == null)
                            break;
                        getScatteredLight(r.rt, treeDepth + 1, node, matSpec.eccentricity, totalDist, material, prevMaterial, currentMatTrans, prevMatTrans);
                        RGBColor color = r.rt.color[treeDepth + 1];
                        re += color.getRed() * (1.0f - rs);
                        ge += color.getGreen() * (1.0f - gs);
                        be += color.getBlue() * (1.0f - bs);
                    }
                    if (r.rt.volumeMap != null) {
                        RGBColor color = r.rt.color[treeDepth + 1];
                        r.rt.volumeMap.getVolumeLight(v, matSpec, r.getDirection(), color);
                        color.multiply(rayIntensity);
                        re += color.getRed() * (1.0f - rs);
                        ge += color.getGreen() * (1.0f - gs);
                        be += color.getBlue() * (1.0f - bs);
                    }
                }
            }
            rf *= rs;
            gf *= gs;
            bf *= bs;
            if (rf < minRayIntensity && gf < minRayIntensity && bf < minRayIntensity) {
                // Everything beyond this point makes an insignificant contribution, so 
                // just stop now. 
                rf = gf = bf = 0.0f;
                break;
            }
        } while (x < dist);
    }
    // Set the output colors and return. 
    emitted.setRGB(re, ge, be);
    filter.setRGB(rf, gf, bf);
}
