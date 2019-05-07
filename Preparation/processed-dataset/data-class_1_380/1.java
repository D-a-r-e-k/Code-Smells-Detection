/* (non-Javadoc)
   * @see org.apache.lucene.search.highlight.Fragmenter#start(java.lang.String, org.apache.lucene.analysis.TokenStream)
   */
@Override
public void start(String originalText, TokenStream stream) {
    offsetAtt = stream.addAttribute(OffsetAttribute.class);
    currentNumFrags = 1;
}
