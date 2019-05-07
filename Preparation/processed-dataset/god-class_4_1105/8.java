/** Propagate a ray through a material, determine how much light is removed (due to
      absorption and outscattering) and stored photons in the volume photon map.
      <p>
      On exit, rayIntensity[treeDepth] is reduced by the appropriate factor to account
      for the absorbed light.

      @param r                 the ray being propagated
      @param node              the octree node containing the ray origin
      @param dist              the distance between the ray origin and the endpoint
      @param material          the MaterialMapping through which the ray is being propagated
      @param prevMaterial      the MaterialMapping the ray was passing through before entering material
      @param currentMatTrans   the transform to local coordinates for the current material
      @param prevMatTrans      the transform to local coordinates for the previous material
      @param color             on exit, this is multiplied by the attenuation factor
      @param treeDepth         the current ray tree depth
      @param totalDist         the distance traveled from the viewpoint
      @param caustic            true if this ray has been specularly reflected or refracted since leaving the eye
      @param scattered         true if this ray has already been scattered by the material
   */
void propagateRay(Ray r, OctreeNode node, RTObject first, double dist, MaterialMapping material, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans, RGBColor color, int treeDepth, double totalDist, boolean caustic, boolean scattered) {
    MaterialSpec matSpec = r.rt.matSpec;
    // Integrate the material properties by stepping along the ray. 
    Vec3 v = r.rt.ray[treeDepth + 1].origin, origin = r.origin, direction = r.direction;
    double x = 0.0, newx, dx, distToScreen = rt.theCamera.getDistToScreen(), step;
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
    step = rt.stepSize * material.getStepSize();
    do {
        // Find the new point along the ray. 
        dx = step * (1.5 * r.rt.random.nextDouble());
        if (this.rt.adaptive && totalDist > distToScreen)
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
        material.getMaterialSpec(v, matSpec, dx, this.rt.time);
        RGBColor trans = matSpec.transparency;
        RGBColor scat = matSpec.scattering;
        // Update the total emission and transmission. 
        float rt, gt, bt;
        if (trans.getRed() == 1.0f)
            rt = 1.0f;
        else
            rt = (float) Math.pow(trans.getRed(), dx);
        if (trans.getGreen() == 1.0f)
            gt = 1.0f;
        else
            gt = (float) Math.pow(trans.getGreen(), dx);
        if (trans.getBlue() == 1.0f)
            bt = 1.0f;
        else
            bt = (float) Math.pow(trans.getBlue(), dx);
        float averageTrans = (rt + gt + bt) / 3.0f;
        if (random.nextFloat() < averageTrans) {
            // The photon does not interact with the medium here. 
            color.multiply(rt / averageTrans, gt / averageTrans, bt / averageTrans);
            continue;
        }
        float scatProb = (scat.getRed() + scat.getGreen() + scat.getBlue()) / 3.0f;
        if (scatProb > 0.98f)
            scatProb = 0.98f;
        // Otherwise photons just bounce around forever and never get stored. 
        if (random.nextFloat() < scatProb && treeDepth < this.rt.maxRayDepth - 1) {
            // The photon is scattered. 
            if (treeDepth < this.rt.maxRayDepth - 1) {
                RGBColor rayIntensity = r.rt.rayIntensity[treeDepth + 1];
                rayIntensity.copy(color);
                rayIntensity.multiply(matSpec.scattering);
                rayIntensity.scale(1.0f / scatProb);
                if (rayIntensity.getMaxComponent() > this.rt.minRayIntensity) {
                    // Send out a scattered ray. 
                    v.set(origin.x + direction.x * x, origin.y + direction.y * x, origin.z + direction.z * x);
                    while (node != null && !node.contains(v)) {
                        OctreeNode nextNode = node.findNextNode(r);
                        node = nextNode;
                    }
                    if (node == null)
                        break;
                    double g = matSpec.eccentricity;
                    Vec3 newdir = r.rt.ray[treeDepth + 1].getDirection();
                    if (g > 0.01 || g < -0.01) {
                        // Importance sample the phase function. 
                        double theta = Math.acos((1.0 + g * g - Math.pow((1 - g * g) / (1 - g + 2 * g * random.nextDouble()), 2.0)) / Math.abs(2 * g));
                        double phi = 2 * Math.PI * random.nextDouble();
                        newdir.set(Math.sin(theta) * Math.cos(phi), Math.sin(theta) * Math.sin(phi), Math.cos(theta));
                        Mat4 m = Mat4.objectTransform(new Vec3(), direction, Math.abs(direction.y) > 0.9 ? Vec3.vx() : Vec3.vy());
                        m.transformDirection(newdir);
                    } else {
                        // Pick a uniformly distributed random direction. 
                        newdir.set(0.0, 0.0, 0.0);
                        randomizePoint(newdir, 1.0);
                        newdir.normalize();
                    }
                    tracePhoton(r.rt.ray[treeDepth + 1], rayIntensity, treeDepth + 1, node, first, material, prevMaterial, currentMatTrans, prevMatTrans, totalDist, true, caustic);
                }
            }
            color.setRGB(0.0, 0.0, 0.0);
            break;
        }
        // The photon is absorbed. 
        color.scale(1.0 / (1.0 - scatProb));
        if ((includeDirect || scattered) && color.getMaxComponent() > this.rt.minRayIntensity)
            addPhoton(v, direction, color);
        color.setRGB(0.0, 0.0, 0.0);
        break;
    } while (x < dist);
}
