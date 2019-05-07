/**
   * Changes the font selection in the combo box and returns the
   * size actually selected.
   * @return -1 if unable to select an appropriate font
   */
protected int changeFontSizeCombo(JComboBox box, int requestedSize) {
    int len = box.getItemCount();
    int currentValue;
    Object currentObject;
    Object selectedObject = box.getItemAt(0);
    int selectedValue = Integer.parseInt(String.valueOf(selectedObject));
    for (int i = 0; i < len; i++) {
        currentObject = box.getItemAt(i);
        currentValue = Integer.parseInt(String.valueOf(currentObject));
        if (selectedValue < currentValue && currentValue <= requestedSize) {
            selectedValue = currentValue;
            selectedObject = currentObject;
        }
    }
    box.setSelectedItem(selectedObject);
    return selectedValue;
}
