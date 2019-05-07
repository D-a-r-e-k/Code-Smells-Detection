/**
     * Calls the heading listeners.
     *
     * @param param A Heading object.
     */
protected void callHeadingListenerChain(Heading param) {
    List list = m_headingListenerChain;
    for (Iterator i = list.iterator(); i.hasNext(); ) {
        HeadingListener h = (HeadingListener) i.next();
        h.headingAdded(m_context, param);
    }
}
