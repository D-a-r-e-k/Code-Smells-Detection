public boolean isPkClassIsAutogeneratable() {
    String primaryKeyClass = typeText.getText();
    return autogeneratablePrimaryKeyClasses.contains(primaryKeyClass);
}
