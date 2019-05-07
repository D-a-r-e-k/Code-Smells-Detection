public String toString() {
    String version = null;
    if (mpegVersion == MPEG_Version_1)
        version = "1";
    else if (mpegVersion == MPEG_Version_2)
        version = "2";
    else if (mpegVersion == MPEG_Version_2_5)
        version = "2.5";
    return "MP3 Frame: " + " version=" + version + " bit-rate=" + bitRate + " sample-rate=" + sampleRate + " stereo=" + (channelMode != CHANNEL_MODE_MONO);
}
