/**
     * Returns the abbreviation of the type of movement used. This is
     * Infantry-specific.
     */
@Override
public String getMovementAbbr(EntityMovementType mtype) {
    switch(mtype) {
        case MOVE_NONE:
            return "N";
        case MOVE_WALK:
            return "W";
        case MOVE_RUN:
            return "R";
        case MOVE_JUMP:
            return "J";
        case MOVE_VTOL_WALK:
        case MOVE_VTOL_RUN:
            return "F";
        default:
            return "?";
    }
}
