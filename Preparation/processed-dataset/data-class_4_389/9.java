@Override
public boolean canChangeSecondaryFacing() {
    return !m_bHasNoTurret && !isTurretLocked();
}
