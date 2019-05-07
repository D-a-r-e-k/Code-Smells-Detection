/**
     * Plays some sound. Parameter == null stops playing a sound.
     *
     * @param sound The sound resource to play or <b>null</b>
     */
public void playSound(String sound) {
    if (canPlaySound()) {
        if (sound == null) {
            soundPlayer.stop();
        } else {
            File file = ResourceManager.getAudio(sound);
            if (file != null) {
                soundPlayer.playOnce(file);
            }
            logger.finest(((file == null) ? "Could not load" : "Playing") + " sound: " + sound);
        }
    }
}
