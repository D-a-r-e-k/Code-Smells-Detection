/** Return a Keyframe which describes the current pose of this object. */
public Keyframe getPoseKeyframe() {
    return new TubeKeyframe(this);
}
