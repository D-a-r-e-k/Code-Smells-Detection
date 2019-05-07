public String getPaymentDescription() {
    return getIndex() + ":" + getCurrency().name() + ":" + getFrequency().getTenorDescriptor();
}
