Augmentations getEmptyAugs(Augmentations augs) {
    if (augs == null) {
        augs = fAugmentations;
        augs.removeAllItems();
    }
    augs.putItem(Constants.ELEMENT_PSVI, fCurrentPSVI);
    fCurrentPSVI.reset();
    return augs;
}
