/** Clip a triangle to the region in front of the z clipping plane. */
private Vec3[] clipTriangle(Vec3 v1, Vec3 v2, Vec3 v3, float z1, float z2, float z3, float newz[], double newu[], double newv[], RasterContext context) {
    double clip = context.camera.getClipDistance();
    boolean c1 = z1 < clip, c2 = z2 < clip, c3 = z3 < clip;
    Vec3 u1, u2, u3, u4;
    int clipCount = 0;
    if (c1)
        clipCount++;
    if (c2)
        clipCount++;
    if (c3)
        clipCount++;
    if (clipCount == 2) {
        // Two vertices need to be clipped. 
        if (!c1) {
            u1 = v1;
            newz[0] = z1;
            newu[0] = 1.0;
            newv[0] = 0.0;
            double f2 = (z1 - clip) / (z1 - z2), f1 = 1.0 - f2;
            u2 = new Vec3(f1 * v1.x + f2 * v2.x, f1 * v1.y + f2 * v2.y, f1 * v1.z + f2 * v2.z);
            newz[1] = (float) (f1 * z1 + f2 * z2);
            newu[1] = f1;
            newv[1] = f2;
            f2 = (z1 - clip) / (z1 - z3);
            f1 = 1.0 - f2;
            u3 = new Vec3(f1 * v1.x + f2 * v3.x, f1 * v1.y + f2 * v3.y, f1 * v1.z + f2 * v3.z);
            newz[2] = (float) (f1 * z1 + f2 * z3);
            newu[2] = f1;
            newv[2] = 0.0;
        } else if (!c2) {
            u2 = v2;
            newz[1] = z2;
            newu[1] = 0.0;
            newv[1] = 1.0;
            double f2 = (z2 - clip) / (z2 - z3), f1 = 1.0 - f2;
            u3 = new Vec3(f1 * v2.x + f2 * v3.x, f1 * v2.y + f2 * v3.y, f1 * v2.z + f2 * v3.z);
            newz[2] = (float) (f1 * z2 + f2 * z3);
            newu[2] = 0.0;
            newv[2] = f1;
            f2 = (z2 - clip) / (z2 - z1);
            f1 = 1.0 - f2;
            u1 = new Vec3(f1 * v2.x + f2 * v1.x, f1 * v2.y + f2 * v1.y, f1 * v2.z + f2 * v1.z);
            newz[0] = (float) (f1 * z2 + f2 * z1);
            newu[0] = f2;
            newv[0] = f1;
        } else {
            u3 = v3;
            newz[2] = z3;
            newu[2] = 0.0;
            newv[2] = 0.0;
            double f2 = (z3 - clip) / (z3 - z1), f1 = 1.0 - f2;
            u1 = new Vec3(f1 * v3.x + f2 * v1.x, f1 * v3.y + f2 * v1.y, f1 * v3.z + f2 * v1.z);
            newz[0] = (float) (f1 * z3 + f2 * z1);
            newu[0] = f2;
            newv[0] = 0.0;
            f2 = (z3 - clip) / (z3 - z2);
            f1 = 1.0 - f2;
            u2 = new Vec3(f1 * v3.x + f2 * v2.x, f1 * v3.y + f2 * v2.y, f1 * v3.z + f2 * v2.z);
            newz[1] = (float) (f1 * z3 + f2 * z2);
            newu[1] = 0.0;
            newv[1] = f2;
        }
        return new Vec3[] { u1, u2, u3 };
    }
    // Only one vertex needs to be clipped, resulting in a quad. 
    if (c1) {
        u1 = v2;
        newz[0] = z2;
        newu[0] = 0.0;
        newv[0] = 1.0;
        u2 = v3;
        newz[1] = z3;
        newu[1] = 0.0;
        newv[1] = 0.0;
        double f1 = (z2 - clip) / (z2 - z1), f2 = 1.0 - f1;
        u3 = new Vec3(f1 * v1.x + f2 * v2.x, f1 * v1.y + f2 * v2.y, f1 * v1.z + f2 * v2.z);
        newz[2] = (float) (f1 * z1 + f2 * z2);
        newu[2] = f1;
        newv[2] = f2;
        f1 = (z3 - clip) / (z3 - z1);
        f2 = 1.0 - f1;
        u4 = new Vec3(f1 * v1.x + f2 * v3.x, f1 * v1.y + f2 * v3.y, f1 * v1.z + f2 * v3.z);
        newz[3] = (float) (f1 * z1 + f2 * z3);
        newu[3] = f1;
        newv[3] = 0.0;
    } else if (c2) {
        u1 = v3;
        newz[0] = z3;
        newu[0] = 0.0;
        newv[0] = 0.0;
        u2 = v1;
        newz[1] = z1;
        newu[1] = 1.0;
        newv[1] = 0.0;
        double f1 = (z3 - clip) / (z3 - z2), f2 = 1.0 - f1;
        u3 = new Vec3(f1 * v2.x + f2 * v3.x, f1 * v2.y + f2 * v3.y, f1 * v2.z + f2 * v3.z);
        newz[2] = (float) (f1 * z2 + f2 * z3);
        newu[2] = 0.0;
        newv[2] = f1;
        f1 = (z1 - clip) / (z1 - z2);
        f2 = 1.0 - f1;
        u4 = new Vec3(f1 * v2.x + f2 * v1.x, f1 * v2.y + f2 * v1.y, f1 * v2.z + f2 * v1.z);
        newz[3] = (float) (f1 * z2 + f2 * z1);
        newu[3] = f2;
        newv[3] = f1;
    } else {
        u1 = v1;
        newz[0] = z1;
        newu[0] = 1.0;
        newv[0] = 0.0;
        u2 = v2;
        newz[1] = z2;
        newu[1] = 0.0;
        newv[1] = 1.0;
        double f1 = (z1 - clip) / (z1 - z3), f2 = 1.0 - f1;
        u3 = new Vec3(f1 * v3.x + f2 * v1.x, f1 * v3.y + f2 * v1.y, f1 * v3.z + f2 * v1.z);
        newz[2] = (float) (f1 * z3 + f2 * z1);
        newu[2] = f2;
        newv[2] = 0.0;
        f1 = (z2 - clip) / (z2 - z3);
        f2 = 1.0 - f1;
        u4 = new Vec3(f1 * v3.x + f2 * v2.x, f1 * v3.y + f2 * v2.y, f1 * v3.z + f2 * v2.z);
        newz[3] = (float) (f1 * z3 + f2 * z2);
        newu[3] = 0.0;
        newv[3] = f2;
    }
    return new Vec3[] { u1, u2, u3, u4 };
}
