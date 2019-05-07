// scanAttribute(XMLAttributesImpl):boolean 
/** Adds location augmentations to the specified attribute. */
protected void addLocationItem(XMLAttributes attributes, int index) {
    fEndLineNumber = fCurrentEntity.getLineNumber();
    fEndColumnNumber = fCurrentEntity.getColumnNumber();
    fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
    LocationItem locationItem = new LocationItem();
    locationItem.setValues(fBeginLineNumber, fBeginColumnNumber, fBeginCharacterOffset, fEndLineNumber, fEndColumnNumber, fEndCharacterOffset);
    Augmentations augs = attributes.getAugmentations(index);
    augs.putItem(AUGMENTATIONS, locationItem);
}
