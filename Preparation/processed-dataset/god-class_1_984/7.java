public void write(OutputStream out) throws IOException {
    out.write(0xff);
    int b = 0xE1;
    b |= this.mpegVersion << 3;
    b |= this.mpegLayer << 1;
    out.write(b);
    b = bit_rate << 4;
    b |= sample_rate << 2;
    if (padded)
        b |= 2;
    out.write(b);
    b = channelMode << 6;
    b |= modeExtension << 4;
    b |= emphasis;
    out.write(b);
    out.write(data);
}
