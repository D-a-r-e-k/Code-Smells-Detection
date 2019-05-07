/**
	 * Notifies the tagBalancingListener (if any) of an ignored start element
	 */
private void notifyDiscardedStartElement(final QName elem, final XMLAttributes attrs, final Augmentations augs) {
    if (tagBalancingListener != null)
        tagBalancingListener.ignoredStartElement(elem, attrs, augs);
}
