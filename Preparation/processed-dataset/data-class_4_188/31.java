/**
   * Searches for messages in this folder which match the given
   * SearchTerm.
   *
   * Basically wraps the call to Folder.search(), and then wraps the
   * returned Message objects as MessageInfos.
   */
public MessageInfo[] search(javax.mail.search.SearchTerm term) throws MessagingException, OperationCancelledException {
    if (isConnected()) {
        return super.search(term);
    } else {
        return getCache().search(term);
    }
}
