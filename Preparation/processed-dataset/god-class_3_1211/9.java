/**
   * Read a particular segmentFileName.  Note that this may
   * throw an IOException if a commit is in process.
   *
   * @param directory -- directory containing the segments file
   * @param segmentFileName -- segment file to load
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
public final void read(Directory directory, String segmentFileName) throws IOException {
    boolean success = false;
    // Clear any previous segments: 
    this.clear();
    generation = generationFromSegmentsFileName(segmentFileName);
    lastGeneration = generation;
    ChecksumIndexInput input = new ChecksumIndexInput(directory.openInput(segmentFileName, IOContext.READ));
    try {
        final int format = input.readInt();
        if (format == CodecUtil.CODEC_MAGIC) {
            // 4.0+ 
            CodecUtil.checkHeaderNoMagic(input, "segments", VERSION_40, VERSION_40);
            version = input.readLong();
            counter = input.readInt();
            int numSegments = input.readInt();
            if (numSegments < 0) {
                throw new CorruptIndexException("invalid segment count: " + numSegments + " (resource: " + input + ")");
            }
            for (int seg = 0; seg < numSegments; seg++) {
                String segName = input.readString();
                Codec codec = Codec.forName(input.readString());
                //System.out.println("SIS.read seg=" + seg + " codec=" + codec); 
                SegmentInfo info = codec.segmentInfoFormat().getSegmentInfoReader().read(directory, segName, IOContext.READ);
                info.setCodec(codec);
                long delGen = input.readLong();
                int delCount = input.readInt();
                if (delCount < 0 || delCount > info.getDocCount()) {
                    throw new CorruptIndexException("invalid deletion count: " + delCount + " (resource: " + input + ")");
                }
                add(new SegmentInfoPerCommit(info, delCount, delGen));
            }
            userData = input.readStringStringMap();
        } else {
            Lucene3xSegmentInfoReader.readLegacyInfos(this, directory, input, format);
            Codec codec = Codec.forName("Lucene3x");
            for (SegmentInfoPerCommit info : this) {
                info.info.setCodec(codec);
            }
        }
        final long checksumNow = input.getChecksum();
        final long checksumThen = input.readLong();
        if (checksumNow != checksumThen) {
            throw new CorruptIndexException("checksum mismatch in segments file (resource: " + input + ")");
        }
        success = true;
    } finally {
        if (!success) {
            // Clear any segment infos we had loaded so we 
            // have a clean slate on retry: 
            this.clear();
            IOUtils.closeWhileHandlingException(input);
        } else {
            input.close();
        }
    }
}
