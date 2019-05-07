/* (non-Javadoc)
   * @see org.apache.lucene.search.highlight.Fragmenter#isNewFragment()
   */
@Override
public boolean isNewFragment() {
    boolean isNewFrag = offsetAtt.endOffset() >= (fragmentSize * currentNumFrags);
    if (isNewFrag) {
        currentNumFrags++;
    }
    return isNewFrag;
}
