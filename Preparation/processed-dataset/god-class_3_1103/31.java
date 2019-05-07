/** Modify this object based on a pose keyframe. */
public void applyPoseKeyframe(Keyframe k) {
    TubeKeyframe key = (TubeKeyframe) k;
    for (int i = 0; i < vertex.length; i++) {
        vertex[i].r.set(key.vertPos[i]);
        smoothness[i] = key.vertSmoothness[i];
        thickness[i] = key.vertThickness[i];
    }
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
