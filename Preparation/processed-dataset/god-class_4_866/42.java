// skipNewlines(int):int 
// infoset utility methods 
/** Returns an augmentations object with a location item added. */
protected final Augmentations locationAugs() {
    HTMLAugmentations augs = null;
    if (fAugmentations) {
        fLocationItem.setValues(fBeginLineNumber, fBeginColumnNumber, fBeginCharacterOffset, fEndLineNumber, fEndColumnNumber, fEndCharacterOffset);
        augs = fInfosetAugs;
        augs.removeAllItems();
        augs.putItem(AUGMENTATIONS, fLocationItem);
    }
    return augs;
}
