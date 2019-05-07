/**
	 * Use the PersistPtrHolder entries to figure out what is the "most recent"
	 * version of all the core records (Document, Notes, Slide etc), and save a
	 * record of them. Do this by walking from the oldest PersistPtr to the
	 * newest, overwriting any references found along the way with newer ones
	 */
private void findMostRecentCoreRecords() {
    // To start with, find the most recent in the byte offset domain 
    Hashtable mostRecentByBytes = new Hashtable();
    for (int i = 0; i < _records.length; i++) {
        if (_records[i] instanceof PersistPtrHolder) {
            PersistPtrHolder pph = (PersistPtrHolder) _records[i];
            // If we've already seen any of the "slide" IDs for this 
            // PersistPtr, remove their old positions 
            int[] ids = pph.getKnownSlideIDs();
            for (int j = 0; j < ids.length; j++) {
                Integer id = Integer.valueOf(ids[j]);
                if (mostRecentByBytes.containsKey(id)) {
                    mostRecentByBytes.remove(id);
                }
            }
            // Now, update the byte level locations with their latest values 
            Hashtable thisSetOfLocations = pph.getSlideLocationsLookup();
            for (int j = 0; j < ids.length; j++) {
                Integer id = Integer.valueOf(ids[j]);
                mostRecentByBytes.put(id, thisSetOfLocations.get(id));
            }
        }
    }
    // We now know how many unique special records we have, so init 
    // the array 
    _mostRecentCoreRecords = new Record[mostRecentByBytes.size()];
    // We'll also want to be able to turn the slide IDs into a position 
    // in this array 
    _sheetIdToCoreRecordsLookup = new Hashtable();
    int[] allIDs = new int[_mostRecentCoreRecords.length];
    Enumeration ids = mostRecentByBytes.keys();
    for (int i = 0; i < allIDs.length; i++) {
        Integer id = (Integer) ids.nextElement();
        allIDs[i] = id.intValue();
    }
    Arrays.sort(allIDs);
    for (int i = 0; i < allIDs.length; i++) {
        _sheetIdToCoreRecordsLookup.put(Integer.valueOf(allIDs[i]), Integer.valueOf(i));
    }
    // Now convert the byte offsets back into record offsets 
    for (int i = 0; i < _records.length; i++) {
        if (_records[i] instanceof PositionDependentRecord) {
            PositionDependentRecord pdr = (PositionDependentRecord) _records[i];
            Integer recordAt = Integer.valueOf(pdr.getLastOnDiskOffset());
            // Is it one we care about? 
            for (int j = 0; j < allIDs.length; j++) {
                Integer thisID = Integer.valueOf(allIDs[j]);
                Integer thatRecordAt = (Integer) mostRecentByBytes.get(thisID);
                if (thatRecordAt.equals(recordAt)) {
                    // Bingo. Now, where do we store it? 
                    Integer storeAtI = (Integer) _sheetIdToCoreRecordsLookup.get(thisID);
                    int storeAt = storeAtI.intValue();
                    // Tell it its Sheet ID, if it cares 
                    if (pdr instanceof PositionDependentRecordContainer) {
                        PositionDependentRecordContainer pdrc = (PositionDependentRecordContainer) _records[i];
                        pdrc.setSheetId(thisID.intValue());
                    }
                    // Finally, save the record 
                    _mostRecentCoreRecords[storeAt] = _records[i];
                }
            }
        }
    }
    // Now look for the interesting records in there 
    for (int i = 0; i < _mostRecentCoreRecords.length; i++) {
        // Check there really is a record at this number 
        if (_mostRecentCoreRecords[i] != null) {
            // Find the Document, and interesting things in it 
            if (_mostRecentCoreRecords[i].getRecordType() == RecordTypes.Document.typeID) {
                _documentRecord = (Document) _mostRecentCoreRecords[i];
                _fonts = _documentRecord.getEnvironment().getFontCollection();
            }
        } else {
        }
    }
}
