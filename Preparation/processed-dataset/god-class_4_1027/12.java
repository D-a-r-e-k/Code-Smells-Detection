public static String getTrackerString(Tracker tracker) {
    StringBuffer buffer = new StringBuffer();
    if (tracker != null) {
        Iterator iterator = tracker.getList().iterator();
        while (iterator.hasNext()) {
            try {
                Item nameFile = (Item) iterator.next();
                File file = (File) nameFile.getValue();
                Audio audio = getAudio(file.getCanonicalPath());
                if (buffer.length() == 0)
                    buffer.append(audio.getId());
                else {
                    buffer.append(SEPARATOR);
                    buffer.append(audio.getId());
                }
            } catch (Exception ex) {
            }
        }
    }
    return buffer.toString();
}
