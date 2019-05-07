/**
     * Selects the pages to keep in the document. The pages are described as a
     * <CODE>List</CODE> of <CODE>Integer</CODE>. The page ordering can be changed but
     * no page repetitions are allowed. Note that it may be very slow in partial mode.
     * @param pagesToKeep the pages to keep in the document
     */
public void selectPages(List<Integer> pagesToKeep) {
    pageRefs.selectPages(pagesToKeep);
    removeUnusedObjects();
}
