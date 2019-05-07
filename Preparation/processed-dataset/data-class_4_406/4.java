/**
	 * For a given refID (the internal, 0 based numbering scheme), return the
	 * core record
	 *
	 * @param refID
	 *            the refID
	 */
private Record getCoreRecordForRefID(int refID) {
    Integer coreRecordId = (Integer) _sheetIdToCoreRecordsLookup.get(Integer.valueOf(refID));
    if (coreRecordId != null) {
        Record r = _mostRecentCoreRecords[coreRecordId.intValue()];
        return r;
    }
    logger.log(POILogger.ERROR, "We tried to look up a reference to a core record, but there was no core ID for reference ID " + refID);
    return null;
}
