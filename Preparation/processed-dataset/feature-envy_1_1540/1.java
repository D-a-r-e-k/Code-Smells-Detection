/* Modify the position of the object. */
public void apply(double time) {
    PointInfo point = new PointInfo();
    OutputModule output[] = proc.getOutputModules();
    ArrayKeyframe params = (ArrayKeyframe) tc.evaluate(time, smoothingMethod);
    point.t = time;
    if (params != null)
        point.param = params.val;
    double weight = theWeight.getWeight(time);
    if (worldCoords)
        info.addDistortion(new CustomDistortion(proc, procVersion, point, weight, info.getCoords().fromLocal(), info.getCoords().toLocal()));
    else
        info.addDistortion(new CustomDistortion(proc, procVersion, point, weight, null, null));
}
