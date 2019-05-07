private boolean enforceRequiredValidation() {
    //primary key fields are always 'required' (can't be null), but in the webapp we only validate this  
    //if the pk field isn't auto-generated.  
    return getHasAutoGenPrimaryKey() ? false : requiredCheckBox.isSelected();
}
