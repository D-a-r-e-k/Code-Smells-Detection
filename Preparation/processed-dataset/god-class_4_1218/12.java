@Override
public void setSecondaryFacing(int sec_facing) {
    if (!isTurretLocked()) {
        super.setSecondaryFacing(sec_facing);
        if (!m_bHasNoTurret) {
            m_nTurretOffset = sec_facing - getFacing();
        }
    }
}
