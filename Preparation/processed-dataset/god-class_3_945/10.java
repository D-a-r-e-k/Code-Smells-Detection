public String getValidationDepends() {
    if (validationDependsText.getText() == null) {
        return "";
    } else {
        return validationDependsText.getText();
    }
}
