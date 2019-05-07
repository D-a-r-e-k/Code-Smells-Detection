/**
     * see {@link Entity#getForwardArc()}
     */
@Override
public int getForwardArc() {
    if (game.getOptions().booleanOption("tacops_vehicle_arcs")) {
        return Compute.ARC_NOSE;
    }
    return super.getForwardArc();
}
