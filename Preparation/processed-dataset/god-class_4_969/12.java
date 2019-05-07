/******************************************************************************/
/**
 * Runs the Algorithm until {@link #temperature} is lower than 
 * {@link #minTemperature}.
 */
private void run() {
    while (round <= maxRounds && isAllowedToRun()) performRound();
}
