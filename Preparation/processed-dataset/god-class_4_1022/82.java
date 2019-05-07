/**
   * Gets all of the header strings for the given search term.
   */
private List getHeaders(SearchTerm term) {
    List returnValue = new LinkedList();
    if (term instanceof HeaderTerm) {
        String headerName = ((HeaderTerm) term).getHeaderName();
        returnValue.add(headerName);
    } else if (term instanceof AndTerm) {
        SearchTerm[] terms = ((AndTerm) term).getTerms();
        for (int i = 0; i < terms.length; i++) {
            returnValue.addAll(getHeaders(terms[i]));
        }
    } else if (term instanceof OrTerm) {
        SearchTerm[] terms = ((OrTerm) term).getTerms();
        for (int i = 0; i < terms.length; i++) {
            returnValue.addAll(getHeaders(terms[i]));
        }
    } else if (term instanceof NotTerm) {
        SearchTerm otherTerm = ((NotTerm) term).getTerm();
        returnValue.addAll(getHeaders(otherTerm));
    } else if (term instanceof FromTerm || term instanceof FromStringTerm) {
        returnValue.add("From");
    } else if (term instanceof RecipientTerm || term instanceof RecipientStringTerm) {
        Message.RecipientType type;
        if (term instanceof RecipientTerm)
            type = ((RecipientTerm) term).getRecipientType();
        else
            type = ((RecipientStringTerm) term).getRecipientType();
        if (type == Message.RecipientType.TO)
            returnValue.add("To");
        else if (type == Message.RecipientType.CC)
            returnValue.add("Cc");
        else if (type == Message.RecipientType.BCC)
            returnValue.add("Bcc");
    }
    return returnValue;
}
