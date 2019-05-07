/**
	 * Return Header / Footer settings for notes
	 *
	 * @return Header / Footer settings for notes
	 */
public HeadersFooters getNotesHeadersFooters() {
    // detect if this ppt was saved in Office2007 
    String tag = getSlidesMasters()[0].getProgrammableTag();
    boolean ppt2007 = "___PPT12".equals(tag);
    HeadersFootersContainer hdd = null;
    Record[] ch = _documentRecord.getChildRecords();
    for (int i = 0; i < ch.length; i++) {
        if (ch[i] instanceof HeadersFootersContainer && ((HeadersFootersContainer) ch[i]).getOptions() == HeadersFootersContainer.NotesHeadersFootersContainer) {
            hdd = (HeadersFootersContainer) ch[i];
            break;
        }
    }
    boolean newRecord = false;
    if (hdd == null) {
        hdd = new HeadersFootersContainer(HeadersFootersContainer.NotesHeadersFootersContainer);
        newRecord = true;
    }
    if (ppt2007 && _notes.length > 0) {
        return new HeadersFooters(hdd, _notes[0], newRecord, ppt2007);
    }
    return new HeadersFooters(hdd, this, newRecord, ppt2007);
}
