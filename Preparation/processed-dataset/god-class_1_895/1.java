public DoubleEntry toDoubleEntry() {
    DoubleEntry de = new DoubleEntry(this);
    de.getOther().setAmount(getOther().getAmount());
    de.getOther().setStatus(getOther().getStatus());
    return de;
}
