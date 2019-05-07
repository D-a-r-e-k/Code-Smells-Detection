@Override
public void setFacing(int facing) {
    super.setFacing(facing);
    if (isTurretLocked()) {
        int nTurretFacing = (facing + m_nTurretOffset + 6) % 6;
        super.setSecondaryFacing(nTurretFacing);
    }
}
