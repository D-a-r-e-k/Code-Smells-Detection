/**
     * Selects the pages to keep in the document. The pages are described as
     * ranges. The page ordering can be changed but
     * no page repetitions are allowed. Note that it may be very slow in partial mode.
     * @param ranges the comma separated ranges as described in {@link SequenceList}
     */
public void selectPages(String ranges) {
    selectPages(SequenceList.expand(ranges, getNumberOfPages()));
}
