private void expandRelatedAttributeUsesComponents(XSObjectList attrUses, Vector componentList, String namespace, Hashtable dependencies) {
    final int attrUseSize = (attrUses == null) ? 0 : attrUses.size();
    for (int i = 0; i < attrUseSize; i++) {
        expandRelatedAttributeUseComponents((XSAttributeUse) attrUses.item(i), componentList, namespace, dependencies);
    }
}
