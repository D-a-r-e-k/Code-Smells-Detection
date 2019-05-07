// Calculate the normal vector at a point on the subdivided mesh. 
private Vec3 calcNormal(Vec3 point[], int u, int v, int u1, int u2, int v1, int v2, int udim) {
    double len1, len2;
    Vec3 vec1, vec2, norm;
    // Calculate the tangent vector along the u direction. 
    vec1 = point[u1 + udim * v].minus(point[u2 + udim * v]);
    len1 = vec1.length2();
    if (len1 == 0.0) {
        vec1 = point[u1 + udim * v1].minus(point[u2 + udim * v1]);
        len1 = vec1.length2();
    }
    if (len1 == 0.0) {
        vec1 = point[u1 + udim * v2].minus(point[u2 + udim * v2]);
        len1 = vec1.length2();
    }
    // Calculate the tangent vector along the v direction. 
    vec2 = point[u + udim * v1].minus(point[u + udim * v2]);
    len2 = vec2.length2();
    if (len2 == 0.0) {
        vec2 = point[u1 + udim * v1].minus(point[u1 + udim * v2]);
        len2 = vec2.length2();
    }
    if (len2 == 0.0) {
        vec2 = point[u2 + udim * v1].minus(point[u2 + udim * v2]);
        len2 = vec2.length2();
    }
    // Take the cross product to get the normal. 
    if (len1 == 0.0 || len2 == 0.0)
        return new Vec3();
    // This will only happen for *very* strange surfaces. 
    norm = vec1.cross(vec2);
    norm.normalize();
    return norm;
}
