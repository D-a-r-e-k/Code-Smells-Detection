/* Make this track identical to another one. */
public void copy(Track tr) {
    CustomDistortionTrack t = (CustomDistortionTrack) tr;
    name = t.name;
    enabled = t.enabled;
    quantized = t.quantized;
    proc.copy(t.proc);
    worldCoords = t.worldCoords;
    smoothingMethod = t.smoothingMethod;
    tc = t.tc.duplicate(info);
    theWeight = (WeightTrack) t.theWeight.duplicate(this);
}
