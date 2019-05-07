public int getSamplesPerFrame() {
    if (mpegVersion == MPEG_Version_1)
        return FRAME_SAMPLES_MPEG_1;
    else
        return FRAME_SAMPLES_MPEG_2;
}
