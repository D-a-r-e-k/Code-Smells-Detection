@Override
public void newRound(int roundNumber) {
    super.newRound(roundNumber);
    // check for crew stun  
    if (m_nStunnedTurns > 0) {
        m_nStunnedTurns--;
    }
    // reset turret facing, if not jammed  
    if (!m_bTurretLocked) {
        setSecondaryFacing(getFacing());
    }
}
