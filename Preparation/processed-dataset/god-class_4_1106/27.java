/** Trace a ray to a light source, and determine which objects it intersects.  If the ray
     is completely blocked, such that no light from the light source reaches the ray origin,
     return false.  Otherwise, return true, and reduce the intensity of color[treeDepth] to
     give the amount of light which reaches the ray origin.  Arguments are:

     @param r                  the ray to trace
     @param lt                 the Light to which r points
     @param treeDepth          the current ray tree depth
     @param node               the octree node containing the ray origin
     @param endNode            the node containing the Light, or null if the light is outside the octree
     @param distToLight        the distance from the ray origin to the light
     @param totalDist          the distance traveled from the viewpoint
     @param currentMaterial    the MaterialMapping at the ray's origin (may be null)
     @param prevMaterial       the MaterialMapping the ray was passing through before entering currentMaterial
     @param currentMatTrans    the transform to local coordinates for the current material
     @param prevMatTrans       the transform to local coordinates for the previous material */
protected boolean traceLightRay(Ray r, Light lt, int treeDepth, OctreeNode node, OctreeNode endNode, double distToLight, double totalDist, MaterialMapping currentMaterial, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans) {
    RGBColor lightColor = r.rt.color[treeDepth], transColor = r.rt.surfSpec[treeDepth].transparent;
    Vec3 intersectionPoint = r.rt.pos[maxRayDepth], trueNorm = r.rt.trueNormal[maxRayDepth];
    MaterialIntersection matChange[] = r.rt.matChange;
    int i, j, matCount = 0;
    do {
        RTObject obj[] = node.getObjects();
        for (i = obj.length - 1; i >= 0; i--) {
            SurfaceIntersection intersection = r.findIntersection(obj[i]);
            if (intersection != SurfaceIntersection.NO_INTERSECTION)
                for (j = 0; ; j++) {
                    intersection.intersectionPoint(j, intersectionPoint);
                    if (node.contains(intersectionPoint)) {
                        double dist = intersection.intersectionDist(j);
                        if (dist < distToLight) {
                            intersection.trueNormal(trueNorm);
                            double angle = -trueNorm.dot(r.getDirection());
                            intersection.intersectionTransparency(j, transColor, angle, (totalDist + dist) * smoothScale, time);
                            lightColor.multiply(transColor);
                            if (lightColor.getRed() < minRayIntensity && lightColor.getGreen() < minRayIntensity && lightColor.getBlue() < minRayIntensity)
                                return false;
                            MaterialMapping mat = obj[i].getMaterialMapping();
                            if (mat != null && mat.castsShadows()) {
                                if (matCount == matChange.length) {
                                    r.rt.increaseMaterialChangeLength();
                                    matChange = r.rt.matChange;
                                }
                                matChange[matCount].mat = mat;
                                matChange[matCount].toLocal = obj[i].toLocal();
                                matChange[matCount].dist = dist;
                                matChange[matCount].node = node;
                                matChange[matCount].entered = (angle > 0.0) ^ (j % 2 == 1);
                                matCount++;
                            }
                        }
                    }
                    if (j >= intersection.numIntersections() - 1)
                        break;
                }
        }
        if (node == endNode)
            break;
        node = node.findNextNode(r);
    } while (node != null);
    if (currentMaterial == null && matCount == 0)
        return true;
    // The ray passes through one or more Materials, so attenuate it accordingly. 
    sortMaterialList(matChange, matCount);
    if (matCount == matChange.length) {
        r.rt.increaseMaterialChangeLength();
        matChange = r.rt.matChange;
    }
    matChange[matCount++].dist = distToLight;
    double dist = 0.0;
    for (i = 0; ; i++) {
        if (currentMaterial != null && currentMaterial.castsShadows()) {
            propagateLightRay(r, node, dist, matChange[i].dist, currentMaterial, lightColor, currentMatTrans, totalDist);
            if (lightColor.getRed() < minRayIntensity && lightColor.getGreen() < minRayIntensity && lightColor.getBlue() < minRayIntensity)
                return false;
        }
        if (i == matCount - 1)
            break;
        double n1 = (currentMaterial == null ? 1.0 : currentMaterial.indexOfRefraction());
        if (matChange[i].entered) {
            if (matChange[i].mat != currentMaterial) {
                prevMaterial = currentMaterial;
                prevMatTrans = currentMatTrans;
                currentMaterial = matChange[i].mat;
                currentMatTrans = matChange[i].toLocal;
            }
        } else if (matChange[i].mat == currentMaterial) {
            currentMaterial = prevMaterial;
            currentMatTrans = prevMatTrans;
            prevMaterial = null;
        } else if (matChange[i].mat == prevMaterial)
            prevMaterial = null;
        if (caustics) {
            double n2 = (currentMaterial == null ? 1.0 : currentMaterial.indexOfRefraction());
            if (n1 != n2)
                return false;
        }
        node = matChange[i].node;
        dist = matChange[i].dist;
    }
    return true;
}
