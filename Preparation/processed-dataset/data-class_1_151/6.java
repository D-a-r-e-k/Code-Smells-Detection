/**
     * Read the next MP3 frame from the stream - return null if no more
     */
public static MP3Frame readFrame(InputStream in) throws IOException {
    MP3Frame frame = new MP3Frame();
    while (true) {
        int b = in.read();
        if (b < 0)
            return null;
        if (b == 0xFF) {
            b = in.read();
            if (b < 0)
                return null;
            if ((b & 0xE0) != 0xE0)
                continue;
            frame.mpegVersion = (b & 0x18) >> 3;
            frame.mpegLayer = (b & 0x06) >> 1;
            frame.isProtected = (b & 1) == 0;
            if (frame.mpegLayer != MPEG_Layer_3)
                continue;
            break;
        }
    }
    //skip the CRC 
    if (frame.isProtected) {
        in.read();
        in.read();
    }
    int b = in.read();
    if (b < 0)
        return null;
    frame.bit_rate = (b & 0xF0) >> 4;
    if (frame.mpegVersion == MPEG_Version_1)
        frame.bitRate = MPEG1BitRates[frame.bit_rate];
    else
        frame.bitRate = MPEG2BitRates[frame.bit_rate];
    frame.sample_rate = (b & 0x0C) >> 2;
    if (frame.mpegVersion == MPEG_Version_1)
        frame.sampleRate = MPEG10SampleRates[frame.sample_rate];
    else if (frame.mpegVersion == MPEG_Version_2)
        frame.sampleRate = MPEG20SampleRates[frame.sample_rate];
    else
        frame.sampleRate = MPEG25SampleRates[frame.sample_rate];
    frame.padded = (b & 2) != 0;
    b = in.read();
    if (b < 0)
        return null;
    frame.channelMode = (b & 0xC0) >> 6;
    frame.modeExtension = (b & 0x30) >> 4;
    frame.copyrighted = ((b & 0x80) >> 3) != 0;
    frame.original = ((b & 0x40) >> 2) != 0;
    frame.emphasis = b & 0x02;
    int size = (((frame.mpegVersion == MPEG_Version_1 ? 144 : 72) * frame.bitRate) / frame.sampleRate) + (frame.padded ? 1 : 0) - 4;
    byte[] data = new byte[size];
    int read = 0;
    int r;
    while ((r = in.read(data, read, size - read)) >= 0 && read < size) {
        read += r;
    }
    if (read != size)
        throw new IOException("Unexpected end of MP3 data");
    frame.data = data;
    //System.out.print( "." ); 
    return frame;
}
