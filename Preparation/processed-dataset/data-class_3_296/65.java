static void checkNoSQLData(Database database, OrderedHashSet set) {
    for (int i = 0; i < set.size(); i++) {
        HsqlName name = (HsqlName) set.get(i);
        if (name.type == SchemaObject.SPECIFIC_ROUTINE) {
            Routine routine = (Routine) database.schemaManager.getSchemaObject(name);
            if (routine.dataImpact == Routine.READS_SQL) {
                throw Error.error(ErrorCode.X_42608, Tokens.T_READS + " " + Tokens.T_SQL);
            } else if (routine.dataImpact == Routine.MODIFIES_SQL) {
                throw Error.error(ErrorCode.X_42608, Tokens.T_MODIFIES + " " + Tokens.T_SQL);
            } else if (name.type == SchemaObject.TABLE) {
                throw Error.error(ErrorCode.X_42608, Tokens.T_READS + " " + Tokens.T_SQL);
            }
        }
    }
}
