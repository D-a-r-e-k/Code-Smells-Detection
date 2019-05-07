/** @see HarvestCoordinator#checkForBandwidthTransition(). */
public synchronized void checkForBandwidthTransition() {
    long currBW = getCurrentGlobalMaxBandwidth();
    if (log.isDebugEnabled()) {
        log.debug("Checking bandwidth. prev = " + previousMaxGlobalBandwidth + " curr = " + currBW);
    }
    if (currBW != previousMaxGlobalBandwidth) {
        if (log.isInfoEnabled()) {
            log.info("Found bandwidth transition from " + previousMaxGlobalBandwidth + " to " + currBW + " re-calulating bandwidth settings.");
        }
        sendBandWidthRestrictions();
    }
    previousMaxGlobalBandwidth = currBW;
}
