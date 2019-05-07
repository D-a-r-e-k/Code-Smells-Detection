/**
     * Returns the name of the type of movement used. This is tank-specific.
     */
@Override
public String getMovementAbbr(EntityMovementType mtype) {
    switch(mtype) {
        case MOVE_SKID:
            return "S";
        case MOVE_NONE:
            return "N";
        case MOVE_WALK:
            return "C";
        case MOVE_RUN:
            return "F";
        case MOVE_JUMP:
            return "J";
        default:
            return "?";
    }
}
