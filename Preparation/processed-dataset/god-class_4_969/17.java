/******************************************************************************/
/**
 * Calculates a break condition for {@link #performRound()} if uphill moves
 * are allowed. This is computed by a formular from Bolzman:<p>
 * <blockquote><blockquote><code>
 * random < e^(oldEnergy-newEnergy)
 * </code></blockquote></blockquote>
 * @param oldEnergy The Energy before the Energy has increased, so it's the 
 * lower one, of the two values.
 * @param newEnergy The Energy after the Energy has increased, so it's the
 * higher one, of the two values
 * @return sometimes <code><b>true</b></code> when the random number is
 * smaler than <code>e^(oldEnergy-newEnergy)</code>
 */
private boolean getBolzmanBreak(double oldEnergy, double newEnergy) {
    return Math.random() < Math.pow(Math.E, (oldEnergy - newEnergy) / temperature);
}
