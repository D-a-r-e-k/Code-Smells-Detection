/**
	 * Build up model level Slide and Notes objects, from the underlying
	 * records.
	 */
private void buildSlidesAndNotes() {
    // Ensure we really found a Document record earlier 
    // If we didn't, then the file is probably corrupt 
    if (_documentRecord == null) {
        throw new CorruptPowerPointFileException("The PowerPoint file didn't contain a Document Record in its PersistPtr blocks. It is probably corrupt.");
    }
    // Fetch the SlideListWithTexts in the most up-to-date Document Record 
    // 
    // As far as we understand it: 
    // * The first SlideListWithText will contain a SlideAtomsSet 
    // for each of the master slides 
    // * The second SlideListWithText will contain a SlideAtomsSet 
    // for each of the slides, in their current order 
    // These SlideAtomsSets will normally contain text 
    // * The third SlideListWithText (if present), will contain a 
    // SlideAtomsSet for each Notes 
    // These SlideAtomsSets will not normally contain text 
    // 
    // Having indentified the masters, slides and notes + their orders, 
    // we have to go and find their matching records 
    // We always use the latest versions of these records, and use the 
    // SlideAtom/NotesAtom to match them with the StyleAtomSet 
    SlideListWithText masterSLWT = _documentRecord.getMasterSlideListWithText();
    SlideListWithText slidesSLWT = _documentRecord.getSlideSlideListWithText();
    SlideListWithText notesSLWT = _documentRecord.getNotesSlideListWithText();
    // Find master slides 
    // These can be MainMaster records, but oddly they can also be 
    // Slides or Notes, and possibly even other odd stuff.... 
    // About the only thing you can say is that the master details are in 
    // the first SLWT. 
    SlideAtomsSet[] masterSets = new SlideAtomsSet[0];
    if (masterSLWT != null) {
        masterSets = masterSLWT.getSlideAtomsSets();
        ArrayList mmr = new ArrayList();
        ArrayList tmr = new ArrayList();
        for (int i = 0; i < masterSets.length; i++) {
            Record r = getCoreRecordForSAS(masterSets[i]);
            SlideAtomsSet sas = masterSets[i];
            int sheetNo = sas.getSlidePersistAtom().getSlideIdentifier();
            if (r instanceof org.apache.poi.hslf.record.Slide) {
                TitleMaster master = new TitleMaster((org.apache.poi.hslf.record.Slide) r, sheetNo);
                master.setSlideShow(this);
                tmr.add(master);
            } else if (r instanceof org.apache.poi.hslf.record.MainMaster) {
                SlideMaster master = new SlideMaster((org.apache.poi.hslf.record.MainMaster) r, sheetNo);
                master.setSlideShow(this);
                mmr.add(master);
            }
        }
        _masters = new SlideMaster[mmr.size()];
        mmr.toArray(_masters);
        _titleMasters = new TitleMaster[tmr.size()];
        tmr.toArray(_titleMasters);
    }
    // Having sorted out the masters, that leaves the notes and slides 
    // Start by finding the notes records to go with the entries in 
    // notesSLWT 
    org.apache.poi.hslf.record.Notes[] notesRecords;
    SlideAtomsSet[] notesSets = new SlideAtomsSet[0];
    Hashtable slideIdToNotes = new Hashtable();
    if (notesSLWT == null) {
        // None 
        notesRecords = new org.apache.poi.hslf.record.Notes[0];
    } else {
        // Match up the records and the SlideAtomSets 
        notesSets = notesSLWT.getSlideAtomsSets();
        ArrayList notesRecordsL = new ArrayList();
        for (int i = 0; i < notesSets.length; i++) {
            // Get the right core record 
            Record r = getCoreRecordForSAS(notesSets[i]);
            // Ensure it really is a notes record 
            if (r instanceof org.apache.poi.hslf.record.Notes) {
                org.apache.poi.hslf.record.Notes notesRecord = (org.apache.poi.hslf.record.Notes) r;
                notesRecordsL.add(notesRecord);
                // Record the match between slide id and these notes 
                SlidePersistAtom spa = notesSets[i].getSlidePersistAtom();
                Integer slideId = Integer.valueOf(spa.getSlideIdentifier());
                slideIdToNotes.put(slideId, Integer.valueOf(i));
            } else {
                logger.log(POILogger.ERROR, "A Notes SlideAtomSet at " + i + " said its record was at refID " + notesSets[i].getSlidePersistAtom().getRefID() + ", but that was actually a " + r);
            }
        }
        notesRecords = new org.apache.poi.hslf.record.Notes[notesRecordsL.size()];
        notesRecords = (org.apache.poi.hslf.record.Notes[]) notesRecordsL.toArray(notesRecords);
    }
    // Now, do the same thing for our slides 
    org.apache.poi.hslf.record.Slide[] slidesRecords;
    SlideAtomsSet[] slidesSets = new SlideAtomsSet[0];
    if (slidesSLWT == null) {
        // None 
        slidesRecords = new org.apache.poi.hslf.record.Slide[0];
    } else {
        // Match up the records and the SlideAtomSets 
        slidesSets = slidesSLWT.getSlideAtomsSets();
        slidesRecords = new org.apache.poi.hslf.record.Slide[slidesSets.length];
        for (int i = 0; i < slidesSets.length; i++) {
            // Get the right core record 
            Record r = getCoreRecordForSAS(slidesSets[i]);
            // Ensure it really is a slide record 
            if (r instanceof org.apache.poi.hslf.record.Slide) {
                slidesRecords[i] = (org.apache.poi.hslf.record.Slide) r;
            } else {
                logger.log(POILogger.ERROR, "A Slide SlideAtomSet at " + i + " said its record was at refID " + slidesSets[i].getSlidePersistAtom().getRefID() + ", but that was actually a " + r);
            }
        }
    }
    // Finally, generate model objects for everything 
    // Notes first 
    _notes = new Notes[notesRecords.length];
    for (int i = 0; i < _notes.length; i++) {
        _notes[i] = new Notes(notesRecords[i]);
        _notes[i].setSlideShow(this);
    }
    // Then slides 
    _slides = new Slide[slidesRecords.length];
    for (int i = 0; i < _slides.length; i++) {
        SlideAtomsSet sas = slidesSets[i];
        int slideIdentifier = sas.getSlidePersistAtom().getSlideIdentifier();
        // Do we have a notes for this? 
        Notes notes = null;
        // Slide.SlideAtom.notesId references the corresponding notes slide. 
        // 0 if slide has no notes. 
        int noteId = slidesRecords[i].getSlideAtom().getNotesID();
        if (noteId != 0) {
            Integer notesPos = (Integer) slideIdToNotes.get(Integer.valueOf(noteId));
            if (notesPos != null)
                notes = _notes[notesPos.intValue()];
            else
                logger.log(POILogger.ERROR, "Notes not found for noteId=" + noteId);
        }
        // Now, build our slide 
        _slides[i] = new Slide(slidesRecords[i], notes, sas, slideIdentifier, (i + 1));
        _slides[i].setSlideShow(this);
    }
}
