/** If necessary, reorder the points in the mesh so that, when converted to a triangle mesh
     for rendering, the normals will be properly oriented. */
public void makeRightSideOut() {
    Vec3 norm[] = getNormals(), result = new Vec3();
    for (int i = 0; i < norm.length; i++) {
        result.x += vertex[i].r.x * norm[i].x;
        result.y += vertex[i].r.y * norm[i].y;
        result.z += vertex[i].r.z * norm[i].z;
    }
    if (result.x + result.y + result.z < 0.0)
        reverseOrientation();
}
