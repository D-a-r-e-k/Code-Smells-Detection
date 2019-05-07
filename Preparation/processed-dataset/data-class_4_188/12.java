/**
   * Fetches the information for the given messages using the given
   * FetchProfile.
   */
/* original version
     public void fetch(MessageInfo[] messages, FetchProfile profile) throws MessagingException {
     // if we're connected, go ahead and fetch these.

     if (getLogger().isLoggable(Level.FINE)) {
     if (messages == null)
     getLogger().log(Level.FINE, "cachingFolderInfo:  fetching with null messages.");
     else
     getLogger().log(Level.FINE, "cachingFolderInfo:  fetching " + messages.length + " messages.");
     }

     int cacheStatus = -1;
     boolean doFlags = profile.contains(FetchProfile.Item.FLAGS);
     boolean doHeaders = (profile.contains(FetchProfile.Item.ENVELOPE) || profile.contains(FetchProfile.Item.CONTENT_INFO));

     if (doFlags && doHeaders) {
     cacheStatus = SimpleFileCache.FLAGS_AND_HEADERS;
     } else if (doFlags) {
     cacheStatus = SimpleFileCache.FLAGS;
     } else if (doHeaders) {
     cacheStatus = SimpleFileCache.HEADERS;
     }

     if (isConnected()) {

     if (getLogger().isLoggable(Level.FINE)) {
     getLogger().log(Level.FINE, "CachingFolderInfo:  running fetch against folder.");
     }
     super.fetch(messages, profile);

     if (cacheStatus != -1) {
     for (int i = 0; i < messages.length; i++) {
     Message m = messages[i].getRealMessage();
     if (m != null) {
     long uid = getUID(m);
     getCache().cacheMessage((MimeMessage)m, uid, cache.getUIDValidity(), cacheStatus);
     }
     }
     }
     } else {
     // if we're not connected, then go ahead and preload the cache for
     // these.
     for (int i = 0; i < messages.length; i++) {
     Message current = messages[i].getMessage();
     if (current != null && current instanceof UIDMimeMessage) {
     long uid = ((UIDMimeMessage) current).getUID();
     if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.FLAGS) {
     getCache().getFlags(uid, cache.getUIDValidity());
     }

     if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.HEADERS) {
     getCache().getHeaders(uid, cache.getUIDValidity());
     }
     }

     messages[i].setFetched(true);
     }
     }

     }
  */
// new version 
public void fetch(MessageInfo[] messages, FetchProfile profile) throws MessagingException {
    // if we're connected, go ahead and fetch these. 
    if (getLogger().isLoggable(Level.FINE)) {
        if (messages == null)
            getLogger().log(Level.FINE, "cachingFolderInfo:  fetching with null messages.");
        else
            getLogger().log(Level.FINE, "cachingFolderInfo:  fetching " + messages.length + " messages.");
    }
    int cacheStatus = -1;
    boolean doFlags = profile.contains(FetchProfile.Item.FLAGS);
    String[] headers = profile.getHeaderNames();
    boolean doHeaders = (profile.contains(FetchProfile.Item.ENVELOPE) || profile.contains(FetchProfile.Item.CONTENT_INFO) || profile.contains(com.sun.mail.imap.IMAPFolder.FetchProfileItem.HEADERS) || (headers != null && headers.length > 0));
    if (doFlags && doHeaders) {
        cacheStatus = SimpleFileCache.FLAGS_AND_HEADERS;
    } else if (doFlags) {
        cacheStatus = SimpleFileCache.FLAGS;
    } else if (doHeaders) {
        cacheStatus = SimpleFileCache.HEADERS;
    }
    if (isConnected()) {
        if (getLogger().isLoggable(Level.FINE)) {
            getLogger().log(Level.FINE, "CachingFolderInfo:  connected.  checking for already-cached messages.");
        }
        if (doHeaders) {
            // assume that headers don't change. 
            FetchProfile fp = new FetchProfile();
            if (doFlags) {
                fp.add(FetchProfile.Item.FLAGS);
            }
            java.util.LinkedList flagsOnly = new java.util.LinkedList();
            java.util.LinkedList headersAndFlags = new java.util.LinkedList();
            for (int i = 0; i < messages.length; i++) {
                Message current = messages[i].getMessage();
                if (current != null && current instanceof UIDMimeMessage) {
                    long uid = ((UIDMimeMessage) current).getUID();
                    if (getCache().getCacheStatus(((UIDMimeMessage) current).getUID()) >= MessageCache.HEADERS) {
                        flagsOnly.add(messages[i]);
                    } else {
                        headersAndFlags.add(messages[i]);
                    }
                }
            }
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, "CachingFolderInfo:  running fetch against " + headersAndFlags.size() + " full messages, plus " + flagsOnly.size() + " flags-only messages");
            }
            MessageInfo[] headersAndFlagsArray = (MessageInfo[]) headersAndFlags.toArray(new MessageInfo[0]);
            MessageInfo[] flagsOnlyArray = (MessageInfo[]) flagsOnly.toArray(new MessageInfo[0]);
            super.fetch(headersAndFlagsArray, profile);
            super.fetch(flagsOnlyArray, fp);
            if (cacheStatus != -1) {
                for (int i = 0; i < headersAndFlagsArray.length; i++) {
                    Message m = headersAndFlagsArray[i].getRealMessage();
                    if (m != null) {
                        long uid = getUID(m);
                        getCache().cacheMessage((MimeMessage) m, uid, cache.getUIDValidity(), cacheStatus);
                    }
                }
                for (int i = 0; i < flagsOnlyArray.length; i++) {
                    Message m = flagsOnlyArray[i].getRealMessage();
                    if (m != null) {
                        long uid = getUID(m);
                        getCache().cacheMessage((MimeMessage) m, uid, cache.getUIDValidity(), MessageCache.FLAGS);
                    }
                }
            }
        } else {
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, "CachingFolderInfo:  running fetch against folder.");
            }
            super.fetch(messages, profile);
            if (cacheStatus != -1) {
                for (int i = 0; i < messages.length; i++) {
                    Message m = messages[i].getRealMessage();
                    if (m != null) {
                        long uid = getUID(m);
                        getCache().cacheMessage((MimeMessage) m, uid, cache.getUIDValidity(), cacheStatus);
                    }
                }
            }
        }
    } else {
        // if we're not connected, then go ahead and preload the cache for 
        // these. 
        for (int i = 0; i < messages.length; i++) {
            Message current = messages[i].getMessage();
            if (current != null && current instanceof UIDMimeMessage) {
                long uid = ((UIDMimeMessage) current).getUID();
                if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.FLAGS) {
                    getCache().getFlags(uid, cache.getUIDValidity());
                }
                if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.HEADERS) {
                    getCache().getHeaders(uid, cache.getUIDValidity());
                }
            }
            messages[i].setFetched(true);
        }
    }
}
