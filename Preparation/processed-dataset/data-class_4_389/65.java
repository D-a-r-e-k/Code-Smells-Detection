@Override
public boolean canCharge() {
    // Tanks can charge, except Hovers when the option is set, and WIGEs  
    return super.canCharge() && !(game.getOptions().booleanOption("no_hover_charge") && (EntityMovementMode.HOVER == getMovementMode())) && !(EntityMovementMode.WIGE == getMovementMode()) && !(getStunnedTurns() > 0);
}
