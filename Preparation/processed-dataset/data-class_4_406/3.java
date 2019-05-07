/**
	 * For a given SlideAtomsSet, return the core record, based on the refID
	 * from the SlidePersistAtom
	 */
private Record getCoreRecordForSAS(SlideAtomsSet sas) {
    SlidePersistAtom spa = sas.getSlidePersistAtom();
    int refID = spa.getRefID();
    return getCoreRecordForRefID(refID);
}
