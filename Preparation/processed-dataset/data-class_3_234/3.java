/**
     * Set (or reset after configuration change) the cost policy in effect.
     * 
     * @throws FatalConfigurationException
     */
private void initCostPolicy() throws FatalConfigurationException {
    try {
        costAssignmentPolicy = (CostAssignmentPolicy) Class.forName((String) getUncheckedAttribute(null, ATTR_COST_POLICY)).newInstance();
    } catch (Exception e) {
        e.printStackTrace();
        throw new FatalConfigurationException(e.getMessage());
    }
}
