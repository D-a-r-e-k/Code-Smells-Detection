/**
	 * This method sets the granularity period (in milliseconds). The default
	 * value is one second.
	 *
	 * @param period the granularity period value.
	 *
	 * @exception java.lang.IllegalArgumentException - The granularity period
	 * 				is less than or equal to zero.
	 */
public void setGranularityPeriod(long period) throws IllegalArgumentException {
    if (period <= 0)
        throw new IllegalArgumentException("Invalid Granularity Period!!!!" + " Granularity Period cannot be negative or Zero");
    else
        this.granularityPeriod = period;
}
