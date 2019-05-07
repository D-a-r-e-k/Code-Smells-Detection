/**
     *  Figures out if a link is an off-site link.  This recognizes
     *  the most common protocols by checking how it starts.
     *
     *  @param link The link to check.
     *  @return true, if this is a link outside of this wiki.
     *  @since 2.4
     */
public static boolean isExternalLink(String link) {
    int idx = Arrays.binarySearch(EXTERNAL_LINKS, link, c_startingComparator);
    // 
    //  We need to check here once again; otherwise we might 
    //  get a match for something like "h". 
    // 
    if (idx >= 0 && link.startsWith(EXTERNAL_LINKS[idx]))
        return true;
    return false;
}
