/**
	 * Return Header / Footer settings for slides
	 *
	 * @return Header / Footer settings for slides
	 */
public HeadersFooters getSlideHeadersFooters() {
    // detect if this ppt was saved in Office2007 
    String tag = getSlidesMasters()[0].getProgrammableTag();
    boolean ppt2007 = "___PPT12".equals(tag);
    HeadersFootersContainer hdd = null;
    Record[] ch = _documentRecord.getChildRecords();
    for (int i = 0; i < ch.length; i++) {
        if (ch[i] instanceof HeadersFootersContainer && ((HeadersFootersContainer) ch[i]).getOptions() == HeadersFootersContainer.SlideHeadersFootersContainer) {
            hdd = (HeadersFootersContainer) ch[i];
            break;
        }
    }
    boolean newRecord = false;
    if (hdd == null) {
        hdd = new HeadersFootersContainer(HeadersFootersContainer.SlideHeadersFootersContainer);
        newRecord = true;
    }
    return new HeadersFooters(hdd, this, newRecord, ppt2007);
}
