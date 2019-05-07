/**
     * Returns the name of the type of movement used. This is tank-specific.
     */
@Override
public String getMovementString(EntityMovementType mtype) {
    switch(mtype) {
        case MOVE_SKID:
            return "Skidded";
        case MOVE_NONE:
            return "None";
        case MOVE_WALK:
            return "Cruised";
        case MOVE_RUN:
            return "Flanked";
        case MOVE_JUMP:
            return "Jumped";
        default:
            return "Unknown!";
    }
}
