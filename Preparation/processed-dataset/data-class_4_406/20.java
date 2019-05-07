/**
	 * Removes the slide at the given index (0-based).
	 * <p>
	 * Shifts any subsequent slides to the left (subtracts one from their slide
	 * numbers).
	 * </p>
	 *
	 * @param index
	 *            the index of the slide to remove (0-based)
	 * @return the slide that was removed from the slide show.
	 */
public Slide removeSlide(int index) {
    int lastSlideIdx = _slides.length - 1;
    if (index < 0 || index > lastSlideIdx) {
        throw new IllegalArgumentException("Slide index (" + index + ") is out of range (0.." + lastSlideIdx + ")");
    }
    SlideListWithText slwt = _documentRecord.getSlideSlideListWithText();
    SlideAtomsSet[] sas = slwt.getSlideAtomsSets();
    Slide removedSlide = null;
    ArrayList<Record> records = new ArrayList<Record>();
    ArrayList<SlideAtomsSet> sa = new ArrayList<SlideAtomsSet>();
    ArrayList<Slide> sl = new ArrayList<Slide>();
    ArrayList<Notes> nt = new ArrayList<Notes>();
    for (Notes notes : getNotes()) nt.add(notes);
    for (int i = 0, num = 0; i < _slides.length; i++) {
        if (i != index) {
            sl.add(_slides[i]);
            sa.add(sas[i]);
            _slides[i].setSlideNumber(num++);
            records.add(sas[i].getSlidePersistAtom());
            records.addAll(Arrays.asList(sas[i].getSlideRecords()));
        } else {
            removedSlide = _slides[i];
            nt.remove(_slides[i].getNotesSheet());
        }
    }
    if (sa.size() == 0) {
        _documentRecord.removeSlideListWithText(slwt);
    } else {
        slwt.setSlideAtomsSets(sa.toArray(new SlideAtomsSet[sa.size()]));
        slwt.setChildRecord(records.toArray(new Record[records.size()]));
    }
    _slides = sl.toArray(new Slide[sl.size()]);
    // if the removed slide had notes - remove references to them too 
    if (removedSlide != null) {
        int notesId = removedSlide.getSlideRecord().getSlideAtom().getNotesID();
        if (notesId != 0) {
            SlideListWithText nslwt = _documentRecord.getNotesSlideListWithText();
            records = new ArrayList<Record>();
            ArrayList<SlideAtomsSet> na = new ArrayList<SlideAtomsSet>();
            for (SlideAtomsSet ns : nslwt.getSlideAtomsSets()) {
                if (ns.getSlidePersistAtom().getSlideIdentifier() != notesId) {
                    na.add(ns);
                    records.add(ns.getSlidePersistAtom());
                    if (ns.getSlideRecords() != null)
                        records.addAll(Arrays.asList(ns.getSlideRecords()));
                }
            }
            if (na.size() == 0) {
                _documentRecord.removeSlideListWithText(nslwt);
            } else {
                nslwt.setSlideAtomsSets(na.toArray(new SlideAtomsSet[na.size()]));
                nslwt.setChildRecord(records.toArray(new Record[records.size()]));
            }
        }
    }
    _notes = nt.toArray(new Notes[nt.size()]);
    return removedSlide;
}
