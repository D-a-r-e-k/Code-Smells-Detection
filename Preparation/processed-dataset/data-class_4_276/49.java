/** Modify this object based on a pose keyframe. */
public void applyPoseKeyframe(Keyframe k) {
    SplineMeshKeyframe key = (SplineMeshKeyframe) k;
    for (int i = 0; i < vertex.length; i++) vertex[i].r.set(key.vertPos[i]);
    System.arraycopy(key.usmoothness, 0, usmoothness, 0, usmoothness.length);
    System.arraycopy(key.vsmoothness, 0, vsmoothness, 0, vsmoothness.length);
    if (texParam != null && texParam.length > 0)
        for (int i = 0; i < texParam.length; i++) paramValue[i] = key.paramValue[i].duplicate();
    skeleton.copy(key.skeleton);
    cachedMesh = null;
    cachedWire = null;
    findBounds();
}
