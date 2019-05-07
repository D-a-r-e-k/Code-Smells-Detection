/**
     * Arrange for the given CandidateURI to be visited, if it is not
     * already scheduled/completed.
     *
     * @see org.archive.crawler.framework.Frontier#schedule(org.archive.crawler.datamodel.CandidateURI)
     */
public void schedule(CandidateURI caUri) {
    // Canonicalization may set forceFetch flag.  See 
    // #canonicalization(CandidateURI) javadoc for circumstance. 
    String canon = canonicalize(caUri);
    if (caUri.forceFetch()) {
        alreadyIncluded.addForce(canon, caUri);
    } else {
        alreadyIncluded.add(canon, caUri);
    }
}
