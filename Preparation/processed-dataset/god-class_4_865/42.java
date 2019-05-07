// emptyAttributes():XMLAttributes 
/** Returns an augmentations object with a synthesized item added. */
protected final Augmentations synthesizedAugs() {
    HTMLAugmentations augs = null;
    if (fAugmentations) {
        augs = fInfosetAugs;
        augs.removeAllItems();
        augs.putItem(AUGMENTATIONS, SYNTHESIZED_ITEM);
    }
    return augs;
}
