AcroFields getAcroFields() {
    if (acroFields == null) {
        acroFields = new AcroFields(reader, this);
    }
    return acroFields;
}
