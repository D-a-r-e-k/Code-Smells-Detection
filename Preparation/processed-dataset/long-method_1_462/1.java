public void replaceCopiedColsByReferences(ColumnInfo[] colInfoRefs, boolean retainImportData) {
    for (int i = 0; i < colInfoRefs.length; i++) {
        for (int j = 0; j < _columnInfos.length; j++) {
            if (colInfoRefs[i].getName().equals(_columnInfos[j].getName())) {
                if (retainImportData) {
                    colInfoRefs[i].setImportData(_columnInfos[j].getImportedTableName(), _columnInfos[j].getImportedColumnName(), _columnInfos[j].getConstraintName(), _columnInfos[j].isNonDbConstraint());
                }
                _columnInfos[j] = colInfoRefs[i];
                break;
            }
        }
    }
}
