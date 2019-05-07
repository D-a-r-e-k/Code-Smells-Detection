private static Audio getAudio(String path) {
    Audio audio = null;
    try {
        List list = AudioManager.findByPath(path);
        if (list != null && list.size() > 0) {
            audio = (Audio) list.get(0);
        }
    } catch (Exception ex) {
        Tools.logException(DefaultApplication.class, ex);
    }
    if (audio == null) {
        try {
            audio = (Audio) MediaManager.getMedia(path);
            if (audio != null) {
                AudioManager.createAudio(audio);
            }
        } catch (Exception ex) {
            Tools.logException(DefaultApplication.class, ex);
        }
    }
    return audio;
}
