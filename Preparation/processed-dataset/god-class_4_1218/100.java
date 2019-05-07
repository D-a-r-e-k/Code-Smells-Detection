/**
     * see {@link Entity#getRearArc()}
     */
@Override
public int getRearArc() {
    if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
        return Compute.ARC_AFT;
    }
    return super.getRearArc();
}
