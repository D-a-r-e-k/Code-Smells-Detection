/**
     * Regenerates the Struts validations for this field.
     */
public void regenerateValidations() {
    validations = new StrutsValidation(sqlTypeText.getText(), jdbcTypeComboBox.getSelectedItem().toString(), enforceRequiredValidation());
    if (validations.getDependsList() != null) {
        validationDependsText.setText(validations.getDependsList());
    } else {
        validationDependsText.setText("");
    }
    if (validations.getXml() != null) {
        validationXMLTextArea.setText(validations.getXml());
    } else {
        validationXMLTextArea.setText("");
    }
}
