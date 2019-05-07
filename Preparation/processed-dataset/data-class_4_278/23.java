/** Determine what material is present at a particular point in the scene.

      @param rt      contains information for the thread currently being executed
      @param pos     the point at which to determine the material
      @param node    the first octree node which the ray intersects
      @return the object with a material which the point is inside, or null if it is not inside any material.
   */
protected RTObject getMaterialAtPoint(RaytracerContext rt, Vec3 pos, OctreeNode node) {
    // Many points can be excluded immediately. 
    if (materialBounds == null || !materialBounds.contains(pos))
        return null;
    // Create a ray pointing away from the point. 
    Ray r = rt.ray[maxRayDepth];
    r.origin.set(pos);
    double len2 = pos.length2();
    if (len2 > 1e-5) {
        r.direction.set(pos);
        r.direction.scale(1.0 / Math.sqrt(len2));
    } else
        r.direction.set(0.0, 0.0, 1.0);
    r.newID();
    // Trace the ray and watch for it to exit a material. 
    int matCount = 0;
    MaterialIntersection matChange[] = r.rt.matChange;
    Vec3 trueNorm = r.rt.trueNormal[0];
    RTObject first, next = null;
    while (true) {
        if (next == null) {
            node = traceRay(r, node);
            if (node == null)
                return null;
            first = rt.intersect.first;
            next = rt.intersect.second;
        } else {
            first = next;
            next = null;
        }
        SurfaceIntersection intersection = rt.lastRayResult[first.index];
        MaterialMapping mat = first.getMaterialMapping();
        if (mat != null) {
            intersection.trueNormal(trueNorm);
            double angle = -trueNorm.dot(r.getDirection());
            boolean entered = (angle > 0.0);
            if (entered) {
                if (matCount == matChange.length) {
                    rt.increaseMaterialChangeLength();
                    matChange = rt.matChange;
                }
                matChange[matCount++].mat = mat;
            } else if (matCount > 0 && matChange[matCount - 1].mat == mat)
                matCount--;
            else
                return first;
        }
        if (next == null) {
            intersection.intersectionPoint(0, r.getOrigin());
            r.newID();
        }
    }
}
