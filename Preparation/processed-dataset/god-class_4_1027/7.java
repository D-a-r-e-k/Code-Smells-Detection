public boolean handleKeyPress(int code, long rawcode) {
    switch(code) {
        case KEY_THUMBSUP:
        case KEY_NUM1:
            if (mCurrentTrackerContext != null) {
                play("thumbsup.snd");
                if (!isDemoMode()) {
                    PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(DefaultApplication.TRACKER);
                    StringBuffer buffer = new StringBuffer();
                    if (persistentValue != null) {
                        buffer.append(persistentValue.getValue());
                    }
                    File file = new File(mCurrentTrackerContext);
                    if (file.exists()) {
                        if (file.isDirectory()) {
                            FileSystemContainer fileSystemContainer = new FileSystemContainer(mCurrentTrackerContext, true);
                            Tracker tracker = new Tracker(fileSystemContainer.getItems(FileFilters.audioDirectoryFilter), 0);
                            //mTracker.setRandom(musicPlayerConfiguration.isRandomPlayFolders()); 
                            String trackerString = getTrackerString(tracker);
                            if (buffer.length() == 0)
                                buffer.append(trackerString);
                            else {
                                buffer.append(SEPARATOR);
                                buffer.append(trackerString);
                            }
                        } else {
                            try {
                                Audio audio = getAudio(file.getCanonicalPath());
                                if (buffer.length() == 0)
                                    buffer.append(audio.getId());
                                else {
                                    buffer.append(SEPARATOR);
                                    buffer.append(audio.getId());
                                }
                            } catch (Exception ex) {
                                Tools.logException(DefaultApplication.class, ex);
                            }
                        }
                    } else {
                        // Organizer 
                        List files = new ArrayList();
                        try {
                            Transaction tx = null;
                            Session session = HibernateUtil.openSession();
                            try {
                                tx = session.beginTransaction();
                                Query query = session.createQuery(mCurrentTrackerContext).setCacheable(true);
                                Audio audio = null;
                                ScrollableResults items = query.scroll();
                                if (items.first()) {
                                    items.beforeFirst();
                                    while (items.next()) {
                                        audio = (Audio) items.get(0);
                                        files.add(new FileItem(audio.getTitle(), new File(audio.getPath())));
                                    }
                                }
                                tx.commit();
                            } catch (HibernateException he) {
                                if (tx != null)
                                    tx.rollback();
                                throw he;
                            } finally {
                                HibernateUtil.closeSession();
                            }
                        } catch (Exception ex) {
                            Tools.logException(DefaultApplication.class, ex);
                        }
                        Tracker tracker = new Tracker(files, 0);
                        //mTracker.setRandom(musicPlayerConfiguration.isRandomPlayFolders()); 
                        String trackerString = getTrackerString(tracker);
                        if (buffer.length() == 0)
                            buffer.append(trackerString);
                        else {
                            buffer.append(SEPARATOR);
                            buffer.append(trackerString);
                        }
                    }
                    PersistentValueManager.savePersistentValue(TRACKER, buffer.toString());
                }
            }
            return true;
        case KEY_LEFT:
            play("pageup.snd");
            flush();
            setActive(false);
            // TODO Make default just pop 
            return true;
        case KEY_PAUSE:
            //setSoundPlayed(true); 
            play(null);
            getPlayer().pauseTrack();
            return true;
        case KEY_PLAY:
            getPlayer().playTrack();
            return true;
        case KEY_CHANNELUP:
            play("select.snd");
            flush();
            getPlayer().getPrevPos();
            getPlayer().startTrack();
            return true;
        case KEY_CHANNELDOWN:
            play("select.snd");
            flush();
            getPlayer().getNextPos();
            getPlayer().startTrack();
            return true;
        case KEY_SLOW:
            getPlayer().stopTrack();
            break;
        case KEY_FORWARD:
            getPlayer().fastForwardTrack();
            break;
        case KEY_REVERSE:
            getPlayer().rewindTrack();
            break;
        case KEY_ADVANCE:
            play("right.snd");
            flush();
            getPlayer().jumpTrack();
            break;
        case KEY_CLEAR:
            setActive(false);
            break;
    }
    return super.handleKeyPress(code, rawcode);
}
