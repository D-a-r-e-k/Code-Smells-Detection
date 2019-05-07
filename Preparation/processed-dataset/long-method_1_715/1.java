public CustomDistortionTrack(ObjectInfo info) {
    super("Deform");
    this.info = info;
    proc = new Procedure(new OutputModule[] { new OutputModule("X", "X", 0.0, null, IOPort.NUMBER), new OutputModule("Y", "Y", 0.0, null, IOPort.NUMBER), new OutputModule("Z", "Z", 0.0, null, IOPort.NUMBER) });
    parameter = new TextureParameter[0];
    tc = new Timecourse(new Keyframe[0], new double[0], new Smoothness[0]);
    smoothingMethod = Timecourse.INTERPOLATING;
    worldCoords = false;
    theWeight = new WeightTrack(this);
}
