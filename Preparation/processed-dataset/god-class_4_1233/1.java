/**
	 * Find the records that are parent-aware, and tell them who their parent is
	 */
private void handleParentAwareRecords(Record baseRecord) {
    // Only need to do something if this is a container record 
    if (baseRecord instanceof RecordContainer) {
        RecordContainer br = (RecordContainer) baseRecord;
        Record[] childRecords = br.getChildRecords();
        // Loop over child records, looking for interesting ones 
        for (int i = 0; i < childRecords.length; i++) {
            Record record = childRecords[i];
            // Tell parent aware records of their parent 
            if (record instanceof ParentAwareRecord) {
                ((ParentAwareRecord) record).setParentRecord(br);
            }
            // Walk on down for the case of container records 
            if (record instanceof RecordContainer) {
                handleParentAwareRecords(record);
            }
        }
    }
}
