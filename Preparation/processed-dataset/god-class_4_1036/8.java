private static Audio getAudio(String path) {
    Audio audio = null;
    try {
        List list = AudioManager.findByPath(path);
        if (list != null && list.size() > 0) {
            audio = (Audio) list.get(0);
        }
    } catch (Exception ex) {
        Tools.logException(Podcasting.class, ex);
    }
    if (audio == null) {
        try {
            File file = new File(path);
            if (file.exists()) {
                audio = (Audio) MediaManager.getMedia(file.getCanonicalPath());
                AudioManager.createAudio(audio);
            }
        } catch (Exception ex) {
            Tools.logException(Podcasting.class, ex);
        }
    }
    return audio;
}
