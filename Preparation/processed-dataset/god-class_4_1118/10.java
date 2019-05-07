HsqlName[] getReferenceArray(HsqlName objectName, boolean cascade) {
    if (cascade) {
        OrderedHashSet names = new OrderedHashSet();
        database.schemaManager.getCascadingReferencingObjectNames(objectName, names);
        Iterator it = names.iterator();
        while (it.hasNext()) {
            HsqlName name = (HsqlName) it.next();
            if (name.type != SchemaObject.TABLE) {
                it.remove();
            }
        }
        names.add(objectName);
        HsqlName[] array = new HsqlName[names.size()];
        names.toArray(array);
        return array;
    } else {
        return new HsqlName[] { objectName };
    }
}
