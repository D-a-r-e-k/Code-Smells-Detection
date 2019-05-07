/**
     * Returns the name of the type of movement used. This is Infantry-specific.
     */
@Override
public String getMovementString(EntityMovementType mtype) {
    switch(mtype) {
        case MOVE_NONE:
            return "None";
        case MOVE_WALK:
        case MOVE_RUN:
            return "Walked";
        case MOVE_VTOL_WALK:
        case MOVE_VTOL_RUN:
            return "Flew";
        case MOVE_JUMP:
            return "Jumped";
        default:
            return "Unknown!";
    }
}
