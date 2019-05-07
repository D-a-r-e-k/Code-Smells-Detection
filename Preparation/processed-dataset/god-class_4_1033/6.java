private Video getVideo(File file) {
    Video video = null;
    try {
        List list = VideoManager.findByPath(file.getCanonicalPath());
        if (list != null && list.size() > 0) {
            return (Video) list.get(0);
        } else {
            String path = file.getAbsolutePath();
            path = path.substring(0, 1).toLowerCase() + path.substring(1);
            list = VideoManager.findByPath(path);
            if (list != null && list.size() > 0) {
                return (Video) list.get(0);
            }
        }
    } catch (Exception ex) {
        log.error("Video retrieve failed", ex);
    }
    return (Video) MediaManager.getMedia(file.getAbsolutePath());
}
