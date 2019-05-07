/******************************************************************************/
/**
 * Checks, if the algorithm could break it's calculation earlier, than 
 * performing until {@link #countRounds} is {@link #maxRounds}. This depends on
 * {@link #shouldEndPerAverage}. 
 * 
 */
private boolean isFrozen() {
    double sumOfTemp = 0.0;
    //sum of temperatures to get the average value 
    double globalTemp = 0.0;
    //average value of all temperatures 
    boolean isFrozen = true;
    //true while all temperatures <= minTemperature 
    for (int i = 0; i < applyCellList.size(); i++) {
        double temperature = getTemperature(i, applyCellList);
        sumOfTemp += temperature;
        isFrozen = isFrozen && (temperature <= minTemperature);
        if (!isFrozen && !shouldEndPerAverage)
            //speeds up a little 
            break;
    }
    if (shouldEndPerAverage) {
        globalTemp = sumOfTemp / applyCellList.size();
        return globalTemp < minTemperature;
    } else
        return isFrozen;
}
