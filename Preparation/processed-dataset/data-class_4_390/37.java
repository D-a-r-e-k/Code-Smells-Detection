/**
     * Determine if this unit has an active stealth system.
     * <p/>
     * Sub-classes are encouraged to override this method.
     *
     * @return <code>true</code> if this unit has a stealth system that is currently active, <code>false</code> if there is no stealth system or if it is
     *         inactive.
     */
@Override
public boolean isStealthActive() {
    return (isStealthy || isMimetic || isSimpleCamo);
}
