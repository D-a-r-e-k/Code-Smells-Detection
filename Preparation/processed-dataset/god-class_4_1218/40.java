@Override
public void applyDamage() {
    m_bImmobile |= m_bImmobileHit;
    super.applyDamage();
}
