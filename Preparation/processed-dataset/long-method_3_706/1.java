/**
	 * @throws Exception
	 * @throws IOException
	 * @throws CommandCancelledException
	 * @throws IMAPException
	 */
private void synchronizeHeaderlist() throws Exception, IOException, CommandCancelledException, IMAPException {
    // Check if the mailbox has changed 
    MailboxStatus status = getServer().getStatus(this);
    if (status.getMessages() == 0) {
        headerList.clear();
        syncMailboxInfo(status);
        return;
    }
    List localUids = new LinkedList(Arrays.asList(headerList.getUids()));
    // Sort the uid list 
    Collections.sort(localUids);
    int newMessages = 0;
    int largestLocalUid = localUids.size() > 0 ? ((Integer) localUids.get(localUids.size() - 1)).intValue() : -1;
    int largestLocalUidIndex = -1;
    int removedLocalUids = 0;
    int largestRemoteUid = (int) status.getUidNext() - 1;
    if (localUids.size() == status.getMessages() && largestRemoteUid == largestLocalUid) {
        // Seems to be no change! 
        syncMailboxInfo(status);
        return;
    }
    printStatusMessage(MailResourceLoader.getString("statusbar", "message", "sync_messages"));
    if (status.getMessages() > 0) {
        largestRemoteUid = getServer().fetchUid(new SequenceSet(SequenceEntry.STAR), this);
        if (largestRemoteUid == -1) {
            largestRemoteUid = getServer().fetchUid(new SequenceSet(status.getMessages()), this);
        }
        printStatusMessage(MailResourceLoader.getString("statusbar", "message", "sync_messages"));
        // Compute the number of new messages 
        if (localUids.size() > 0) {
            // Number of new messages = largestRemoteUid - largestLocalUid 
            largestLocalUid = ((Integer) localUids.get(localUids.size() - 1)).intValue();
            // Find the index of the largest local Uid 
            int position = localUids.size() - 1;
            while (largestLocalUidIndex == -1 && position >= localUids.size() - 10 && position > 0) {
                largestLocalUidIndex = getServer().getIndex((Integer) localUids.get(position--), this);
            }
            // Still not found -> do a binary search 
            if (largestLocalUidIndex == -1) {
                int a, b, c;
                int index;
                a = 0;
                b = position;
                while (b > a && b - a > 1) {
                    c = Math.round((b - a) * 0.5f) + a;
                    index = getServer().getIndex((Integer) localUids.get(c), this);
                    if (index == -1) {
                        b = c;
                    } else {
                        a = c;
                        largestLocalUidIndex = index;
                    }
                }
                removedLocalUids = localUids.size() - 1 - position;
            } else {
                // -2 because of the decrement in line 317 
                removedLocalUids = localUids.size() - 2 - position;
            }
            // Check if all local uids have been deleted 
            if (largestLocalUidIndex == -1) {
                newMessages = status.getMessages();
                // Set the index of the largest Uid to 0 
                // -> ensure it works with the fetch of new 
                // messages part below 
                largestLocalUidIndex = 0;
            } else {
                newMessages = status.getMessages() - largestLocalUidIndex;
            }
        } else {
            // all messages are new 
            newMessages = status.getMessages();
            // Set the index of the largest Uid to 0 
            // -> ensure it works with the fetch of new 
            // messages part below 
            largestLocalUidIndex = 0;
        }
        // Somehow there are new messages that 
        // have a lower index -> out of sync 
        if (localUids.size() - status.getMessages() - removedLocalUids + newMessages < 0) {
            LOG.severe("Folder " + getName() + " is out of sync -> recreating the cache!");
            headerList.clear();
            // all messages are new 
            newMessages = status.getMessages();
            // Set the index of the largest Uid to 0 
            // -> ensure it works with the fetch of new 
            // messages part below 
            largestLocalUidIndex = 0;
            largestLocalUid = -1;
            localUids.clear();
        }
        LOG.fine("Found " + newMessages + " new Messages");
        // If we have new messages add them to the headerlist 
        if (newMessages > 0) {
            printStatusMessage(MailResourceLoader.getString("statusbar", "message", "fetch_new_headers"));
            IMAPFlags[] newFlags = getServer().fetchFlagsListStartFrom(largestLocalUidIndex + 1, this);
            if (newFlags.length > 0) {
                List newUids = new ArrayList(newFlags.length);
                // Build a list of the new uids 
                for (int i = 0; i < newFlags.length; i++) {
                    // Check if the uids match as expected 
                    if (((Integer) newFlags[i].getUid()).intValue() <= largestLocalUid) {
                        LOG.severe("Assertion Failed : New UID is smaller");
                    }
                    // Update the list of new and local uids 
                    newUids.add(newFlags[i].getUid());
                    localUids.add(newFlags[i].getUid());
                }
                // Fetch the headers of the new messages ... 
                getServer().fetchHeaderList(headerList, newUids, this);
                // .. and set the flags 
                setFlags(newFlags);
                // fire message added updates 
                for (int i = 0; i < newFlags.length; i++) {
                    fireMessageAdded(newFlags[i]);
                }
                // Apply filter on new messages if enabled 
                IMAPRootFolder rootFolder = (IMAPRootFolder) getRootFolder();
                AccountItem accountItem = rootFolder.getAccountItem();
                ImapItem item = accountItem.getImapItem();
                boolean applyFilter = item.getBooleanWithDefault("automatically_apply_filter", false);
                // if "automatically apply filter" is selected & there are 
                // new 
                // messages 
                if (applyFilter) {
                    CommandProcessor.getInstance().addOp(new ApplyFilterCommand(new MailFolderCommandReference(this, newUids.toArray())));
                }
            }
        }
    }
    // Number of deleted messages is computed from exists on imap and local 
    // newMessages 
    int deletedMessages = localUids.size() - status.getMessages();
    LOG.fine("Found " + deletedMessages + " deleted Messages");
    // Find the messages that have been deleted 
    if (deletedMessages > 0) {
        int found = 0;
        // First deleted all local uids that 
        // are larger than the largest remote uid 
        while (localUids.size() > 0 && found != deletedMessages && ((Integer) localUids.get(localUids.size() - 1)).intValue() > largestRemoteUid) {
            Flags flags = headerList.remove(localUids.get(localUids.size() - 1)).getFlags();
            fireMessageRemoved(localUids.remove(localUids.size() - 1), flags);
            found++;
        }
        // Search in packs beginning from newest to oldest 
        // -> in most cases this should save us a lot of uid fetchings to 
        // find the deleted messages 
        // Pack size is min 10, max 200 else mailboxsize / 10 
        int packSize = Math.min(Math.max(deletedMessages, status.getMessages() / 10), 200);
        int upper = status.getMessages();
        int localPointer = localUids.size() - 1;
        // Fetch Pack outer loop 
        while (upper >= 1 && found != deletedMessages) {
            SequenceSet set = new SequenceSet();
            set.add(Math.max(upper - packSize + 1, 1), upper);
            // Fetch these uids and compare them to the 
            // local list 
            Integer[] actUids = getServer().fetchUids(set, this);
            // Compare inner loop 
            for (int i = actUids.length - 1; i >= 0 && found != deletedMessages; i--) {
                // Find missing uids loop 
                while (found != deletedMessages && localPointer >= 0 && !localUids.get(localPointer).equals(actUids[i])) {
                    // We found the uid of a deleted message 
                    // -> remove it from the headerlist 
                    headerList.remove(localUids.get(localPointer));
                    found++;
                    localPointer--;
                }
                // next position in the local uid list 
                localPointer--;
            }
            upper = upper - packSize;
        }
        // All the other local mails are deleted 
        while (found < deletedMessages && localPointer >= 0) {
            headerList.remove(localUids.get(localPointer--));
            found++;
        }
        if (found != deletedMessages) {
            LOG.severe("Assertion failed : found only " + found + " of " + deletedMessages);
        }
    }
    syncMailboxInfo(status);
}
