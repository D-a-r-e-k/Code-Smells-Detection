/******************************************************************************/
/**
 * Calculates the maximal number of rounds, by flattening the actual 
 * {@link #temperature} with the temperature scaling factor 
 * {@link #tempScaleFactor}
 * 
 * @param actualTemperature The Temperature of the actual Graph
 * @return The number of Rounds that have to be performed until 
 * {@link #temperature} falls under {@link #minTemperature}.
 */
private int getMaxRoundsByTemperature(double actualTemperature) {
    return (int) Math.ceil(Math.log(minTemperature / actualTemperature) / Math.log(tempScaleFactor));
}
