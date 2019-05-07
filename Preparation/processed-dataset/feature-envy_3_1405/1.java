public static Sound getSoundDefinition(InputStream mp3) throws IOException {
    MP3Frame frame = MP3Frame.readFrame(mp3);
    int samplesPerFrame = frame.getSamplesPerFrame();
    int sampleRate = frame.getSampleRate();
    boolean isStereo = frame.isStereo();
    int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
    if (sampleRate >= 44000)
        rate = SWFConstants.SOUND_FREQ_44KHZ;
    else if (sampleRate >= 22000)
        rate = SWFConstants.SOUND_FREQ_22KHZ;
    else if (sampleRate >= 11000)
        rate = SWFConstants.SOUND_FREQ_11KHZ;
    int sampleCount = 0;
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    while (true && frame != null) {
        //--Write DelaySeek of zero 
        bout.write(0);
        bout.write(0);
        while (frame != null) {
            sampleCount += frame.getSamplesPerFrame();
            frame.write(bout);
            frame = MP3Frame.readFrame(mp3);
        }
        bout.flush();
    }
    mp3.close();
    byte[] data = bout.toByteArray();
    return new Sound(SWFConstants.SOUND_FORMAT_MP3, rate, true, isStereo, sampleCount, data);
}
