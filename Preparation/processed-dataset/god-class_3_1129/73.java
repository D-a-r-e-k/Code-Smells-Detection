/**
     * Populates the provided HTTPSampleResult with details from the Exception.
     * Does not create a new instance, so should not be used directly to add a subsample.
     * 
     * @param e
     *            Exception representing the error.
     * @param res
     *            SampleResult to be modified
     * @return the modified sampling result containing details of the Exception.
     */
protected HTTPSampleResult errorResult(Throwable e, HTTPSampleResult res) {
    res.setSampleLabel("Error: " + res.getSampleLabel());
    res.setDataType(SampleResult.TEXT);
    ByteArrayOutputStream text = new ByteArrayOutputStream(200);
    e.printStackTrace(new PrintStream(text));
    res.setResponseData(text.toByteArray());
    res.setResponseCode(NON_HTTP_RESPONSE_CODE + ": " + e.getClass().getName());
    res.setResponseMessage(NON_HTTP_RESPONSE_MESSAGE + ": " + e.getMessage());
    res.setSuccessful(false);
    res.setMonitor(this.isMonitor());
    return res;
}
