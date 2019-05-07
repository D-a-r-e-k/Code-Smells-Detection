/** Get an array of normal vectors.  This calculates a single normal for each vertex,
     ignoring smoothness values. */
public Vec3[] getNormals() {
    Vec3 point[] = new Vec3[vertex.length], norm[] = new Vec3[vertex.length];
    int u1, u2, v1, v2, i, j;
    for (i = 0; i < vertex.length; i++) point[i] = vertex[i].r;
    for (i = 0; i < usize; i++) for (j = 0; j < vsize; j++) {
        u1 = i - 1;
        if (u1 == -1) {
            if (uclosed)
                u1 = usize - 1;
            else
                u1 = 0;
        }
        u2 = i + 1;
        if (u2 == usize) {
            if (uclosed)
                u2 = 0;
            else
                u2 = i;
        }
        v1 = j - 1;
        if (v1 == -1) {
            if (vclosed)
                v1 = vsize - 1;
            else
                v1 = 0;
        }
        v2 = j + 1;
        if (v2 == vsize) {
            if (vclosed)
                v2 = 0;
            else
                v2 = j;
        }
        norm[i + usize * j] = calcNormal(point, i, j, u1, u2, v1, v2, usize);
    }
    return norm;
}
