// comment(XMLString,Augmentations) 
private void consumeEarlyTextIfNeeded() {
    if (!lostText_.isEmpty()) {
        if (!fSeenBodyElement) {
            forceStartBody();
        }
        lostText_.refeed(this);
    }
}
