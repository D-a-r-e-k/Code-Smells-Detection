/* Create a duplicate of this track. */
public Track duplicate(Object obj) {
    CustomDistortionTrack t = new CustomDistortionTrack((ObjectInfo) obj);
    t.name = name;
    t.enabled = enabled;
    t.quantized = quantized;
    t.proc.copy(proc);
    t.worldCoords = worldCoords;
    t.smoothingMethod = smoothingMethod;
    t.tc = tc.duplicate((ObjectInfo) obj);
    t.theWeight = (WeightTrack) theWeight.duplicate(t);
    return t;
}
