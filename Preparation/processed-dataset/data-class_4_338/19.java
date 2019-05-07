/** Updates the first document where the current all-document search began (called in two places: either when the 
    * _findField is updated, or when the user changes documents.
    */
public void updateFirstDocInSearch() {
    _machine.setFirstDoc(_model.getActiveDocument());
}
