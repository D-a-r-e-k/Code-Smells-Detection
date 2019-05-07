/** getContentHandler returns "our SAX builder" -- the thing that
   * someone else should send SAX events to in order to extend this
   * DTM model.
   *
   * @return null if this model doesn't respond to SAX events,
   * "this" if the DTM object has a built-in SAX ContentHandler,
   * the IncrmentalSAXSource if we're bound to one and should receive
   * the SAX stream via it for incremental build purposes...
   * */
public org.xml.sax.ContentHandler getContentHandler() {
    return null;
}
