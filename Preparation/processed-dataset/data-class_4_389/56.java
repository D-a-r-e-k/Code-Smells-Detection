/**
     * Tanks don't have MASC
     */
@Override
public int getRunMPwithoutMASC(boolean gravity, boolean ignoreheat) {
    return getRunMP(gravity, ignoreheat);
}
