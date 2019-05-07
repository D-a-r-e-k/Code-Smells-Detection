boolean partialFormFlattening(String name) {
    getAcroFields();
    if (acroFields.getXfa().isXfaPresent())
        throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("partial.form.flattening.is.not.supported.with.xfa.forms"));
    if (!acroFields.getFields().containsKey(name))
        return false;
    partialFlattening.add(name);
    return true;
}
