static void addAllSQL(OrderedHashSet resolved, OrderedHashSet unresolved, HsqlArrayList list, Iterator it, OrderedHashSet newResolved) {
    while (it.hasNext()) {
        SchemaObject object = (SchemaObject) it.next();
        OrderedHashSet references = object.getReferences();
        boolean isResolved = true;
        for (int j = 0; j < references.size(); j++) {
            HsqlName name = (HsqlName) references.get(j);
            if (SqlInvariants.isSchemaNameSystem(name)) {
                continue;
            }
            switch(name.type) {
                case SchemaObject.TABLE:
                    if (!resolved.contains(name)) {
                        isResolved = false;
                    }
                    break;
                case SchemaObject.COLUMN:
                    {
                        if (object.getType() == SchemaObject.TABLE) {
                            int index = ((Table) object).findColumn(name.name);
                            ColumnSchema column = ((Table) object).getColumn(index);
                            if (!isChildObjectResolved(column, resolved)) {
                                isResolved = false;
                            }
                            break;
                        }
                        if (!resolved.contains(name.parent)) {
                            isResolved = false;
                        }
                        break;
                    }
                case SchemaObject.CONSTRAINT:
                    {
                        if (name.parent == object.getName()) {
                            Constraint constraint = ((Table) object).getConstraint(name.name);
                            if (constraint.getConstraintType() == SchemaObject.ConstraintTypes.CHECK) {
                                if (!isChildObjectResolved(constraint, resolved)) {
                                    isResolved = false;
                                }
                            }
                        }
                        // only UNIQUE constraint referenced by FK in table 
                        break;
                    }
                case SchemaObject.CHARSET:
                    if (name.schema == null) {
                        continue;
                    }
                case SchemaObject.TYPE:
                case SchemaObject.DOMAIN:
                case SchemaObject.FUNCTION:
                case SchemaObject.PROCEDURE:
                case SchemaObject.SPECIFIC_ROUTINE:
                    if (!resolved.contains(name)) {
                        isResolved = false;
                    }
                default:
            }
        }
        if (!isResolved) {
            unresolved.add(object);
            continue;
        }
        HsqlName name;
        if (object.getType() == SchemaObject.FUNCTION) {
            name = ((Routine) object).getSpecificName();
        } else {
            name = object.getName();
        }
        resolved.add(name);
        if (newResolved != null) {
            newResolved.add(object);
        }
        if (object.getType() == SchemaObject.TABLE) {
            list.addAll(((Table) object).getSQL(resolved, unresolved));
        } else {
            list.add(object.getSQL());
        }
    }
}
