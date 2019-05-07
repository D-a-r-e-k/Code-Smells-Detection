/** Determine whether this mesh is completely closed. */
public boolean isClosed() {
    if (!vclosed) {
        Vec3 v1 = vertex[0].r, v2 = vertex[usize * (vsize - 1)].r;
        for (int i = 1; i < usize; i++) if (v1.distance2(vertex[i].r) > 1e-24 || v2.distance2(vertex[i + usize * (vsize - 1)].r) > 1e-24)
            return false;
    }
    if (!uclosed) {
        Vec3 v1 = vertex[0].r, v2 = vertex[usize - 1].r;
        for (int i = 1; i < vsize; i++) if (v1.distance2(vertex[i * usize].r) > 1e-24 || v2.distance2(vertex[i * usize + usize - 1].r) > 1e-24)
            return false;
    }
    return true;
}
