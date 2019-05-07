/*
	 * ===============================================================
	 *  Addition Code
	 * ===============================================================
	 */
/**
	 * Create a blank <code>Slide</code>.
	 *
	 * @return the created <code>Slide</code>
	 */
public Slide createSlide() {
    SlideListWithText slist = null;
    // We need to add the records to the SLWT that deals 
    // with Slides. 
    // Add it, if it doesn't exist 
    slist = _documentRecord.getSlideSlideListWithText();
    if (slist == null) {
        // Need to add a new one 
        slist = new SlideListWithText();
        slist.setInstance(SlideListWithText.SLIDES);
        _documentRecord.addSlideListWithText(slist);
    }
    // Grab the SlidePersistAtom with the highest Slide Number. 
    // (Will stay as null if no SlidePersistAtom exists yet in 
    // the slide, or only master slide's ones do) 
    SlidePersistAtom prev = null;
    SlideAtomsSet[] sas = slist.getSlideAtomsSets();
    for (int j = 0; j < sas.length; j++) {
        SlidePersistAtom spa = sas[j].getSlidePersistAtom();
        if (spa.getSlideIdentifier() < 0) {
        } else {
            // Must be for a real slide 
            if (prev == null) {
                prev = spa;
            }
            if (prev.getSlideIdentifier() < spa.getSlideIdentifier()) {
                prev = spa;
            }
        }
    }
    // Set up a new SlidePersistAtom for this slide 
    SlidePersistAtom sp = new SlidePersistAtom();
    // First slideId is always 256 
    sp.setSlideIdentifier(prev == null ? 256 : (prev.getSlideIdentifier() + 1));
    // Add this new SlidePersistAtom to the SlideListWithText 
    slist.addSlidePersistAtom(sp);
    // Create a new Slide 
    Slide slide = new Slide(sp.getSlideIdentifier(), sp.getRefID(), _slides.length + 1);
    slide.setSlideShow(this);
    slide.onCreate();
    // Add in to the list of Slides 
    Slide[] s = new Slide[_slides.length + 1];
    System.arraycopy(_slides, 0, s, 0, _slides.length);
    s[_slides.length] = slide;
    _slides = s;
    logger.log(POILogger.INFO, "Added slide " + _slides.length + " with ref " + sp.getRefID() + " and identifier " + sp.getSlideIdentifier());
    // Add the core records for this new Slide to the record tree 
    org.apache.poi.hslf.record.Slide slideRecord = slide.getSlideRecord();
    int slideRecordPos = _hslfSlideShow.appendRootLevelRecord(slideRecord);
    _records = _hslfSlideShow.getRecords();
    // Add the new Slide into the PersistPtr stuff 
    int offset = 0;
    int slideOffset = 0;
    PersistPtrHolder ptr = null;
    UserEditAtom usr = null;
    for (int i = 0; i < _records.length; i++) {
        Record record = _records[i];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            record.writeOut(out);
        } catch (IOException e) {
            throw new HSLFException(e);
        }
        // Grab interesting records as they come past 
        if (_records[i].getRecordType() == RecordTypes.PersistPtrIncrementalBlock.typeID) {
            ptr = (PersistPtrHolder) _records[i];
        }
        if (_records[i].getRecordType() == RecordTypes.UserEditAtom.typeID) {
            usr = (UserEditAtom) _records[i];
        }
        if (i == slideRecordPos) {
            slideOffset = offset;
        }
        offset += out.size();
    }
    // persist ID is UserEditAtom.maxPersistWritten + 1 
    int psrId = usr.getMaxPersistWritten() + 1;
    sp.setRefID(psrId);
    slideRecord.setSheetId(psrId);
    // Last view is now of the slide 
    usr.setLastViewType((short) UserEditAtom.LAST_VIEW_SLIDE_VIEW);
    usr.setMaxPersistWritten(psrId);
    // increment the number of persit 
    // objects 
    // Add the new slide into the last PersistPtr 
    // (Also need to tell it where it is) 
    slideRecord.setLastOnDiskOffset(slideOffset);
    ptr.addSlideLookup(sp.getRefID(), slideOffset);
    logger.log(POILogger.INFO, "New slide ended up at " + slideOffset);
    slide.setMasterSheet(_masters[0]);
    // All done and added 
    return slide;
}
