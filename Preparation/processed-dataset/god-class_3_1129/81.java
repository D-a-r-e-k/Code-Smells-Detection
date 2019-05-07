/**
     * Perform a sample, and return the results
     *
     * @return results of the sampling
     */
public SampleResult sample() {
    SampleResult res = null;
    try {
        res = sample(getUrl(), getMethod(), false, 0);
        res.setSampleLabel(getName());
        return res;
    } catch (Exception e) {
        return errorResult(e, new HTTPSampleResult());
    }
}
