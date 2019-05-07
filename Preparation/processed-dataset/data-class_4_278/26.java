/** Trace a ray, and determine the first object it intersects.  If it is immediately followed
     by a second object, both are returned.  To avoid creating excess objects, the results
     are returned in the global RayIntersection object.  node is the first octree node which
     the ray intersects.  If an intersection is found, the octree node containing the
     intersection point is returned.  Otherwise, the return value is null. */
protected OctreeNode traceRay(Ray r, OctreeNode node) {
    RTObject first = null, second = null, obj[];
    double dist, firstDist = Double.MAX_VALUE, secondDist = Double.MAX_VALUE;
    Vec3 intersectionPoint = r.rt.pos[maxRayDepth];
    int i;
    while (first == null) {
        obj = node.getObjects();
        for (i = obj.length - 1; i >= 0; i--) {
            SurfaceIntersection intersection = r.findIntersection(obj[i]);
            if (intersection != SurfaceIntersection.NO_INTERSECTION) {
                intersection.intersectionPoint(0, intersectionPoint);
                if (node.contains(intersectionPoint)) {
                    dist = intersection.intersectionDist(0);
                    if (dist < firstDist) {
                        secondDist = firstDist;
                        second = first;
                        firstDist = dist;
                        first = obj[i];
                    } else if (dist < secondDist) {
                        secondDist = dist;
                        second = obj[i];
                    }
                }
            }
        }
        if (first == null) {
            node = node.findNextNode(r);
            if (node == null)
                return null;
        }
    }
    RayIntersection intersect = r.rt.intersect;
    intersect.first = first;
    intersect.dist = firstDist;
    if (secondDist - firstDist < TOL)
        intersect.second = second;
    else
        intersect.second = null;
    return node;
}
