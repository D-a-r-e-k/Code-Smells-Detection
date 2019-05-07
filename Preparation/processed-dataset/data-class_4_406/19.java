/*
	 * ===============================================================
	 * Re-ordering Code
	 * ===============================================================
	 */
/**
	 * Re-orders a slide, to a new position.
	 *
	 * @param oldSlideNumber
	 *            The old slide number (1 based)
	 * @param newSlideNumber
	 *            The new slide number (1 based)
	 */
public void reorderSlide(int oldSlideNumber, int newSlideNumber) {
    // Ensure these numbers are valid 
    if (oldSlideNumber < 1 || newSlideNumber < 1) {
        throw new IllegalArgumentException("Old and new slide numbers must be greater than 0");
    }
    if (oldSlideNumber > _slides.length || newSlideNumber > _slides.length) {
        throw new IllegalArgumentException("Old and new slide numbers must not exceed the number of slides (" + _slides.length + ")");
    }
    // The order of slides is defined by the order of slide atom sets in the 
    // SlideListWithText container. 
    SlideListWithText slwt = _documentRecord.getSlideSlideListWithText();
    SlideAtomsSet[] sas = slwt.getSlideAtomsSets();
    SlideAtomsSet tmp = sas[oldSlideNumber - 1];
    sas[oldSlideNumber - 1] = sas[newSlideNumber - 1];
    sas[newSlideNumber - 1] = tmp;
    ArrayList lst = new ArrayList();
    for (int i = 0; i < sas.length; i++) {
        lst.add(sas[i].getSlidePersistAtom());
        Record[] r = sas[i].getSlideRecords();
        for (int j = 0; j < r.length; j++) {
            lst.add(r[j]);
        }
        _slides[i].setSlideNumber(i + 1);
    }
    Record[] r = (Record[]) lst.toArray(new Record[lst.size()]);
    slwt.setChildRecord(r);
}
