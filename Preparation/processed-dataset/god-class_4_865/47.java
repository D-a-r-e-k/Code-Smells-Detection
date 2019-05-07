/**
	 * Notifies the tagBalancingListener (if any) of an ignored end element
	 */
private void notifyDiscardedEndElement(final QName element, final Augmentations augs) {
    if (tagBalancingListener != null)
        tagBalancingListener.ignoredEndElement(element, augs);
}
