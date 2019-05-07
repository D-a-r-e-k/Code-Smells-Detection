@Override
public boolean isImmobile() {
    if (game.getOptions().booleanOption("no_immobile_vehicles")) {
        return super.isImmobile();
    }
    return super.isImmobile() || m_bImmobile;
}
