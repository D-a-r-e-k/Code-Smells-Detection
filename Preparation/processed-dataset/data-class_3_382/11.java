private void write(Directory directory) throws IOException {
    String segmentsFileName = getNextSegmentFileName();
    // Always advance the generation on write: 
    if (generation == -1) {
        generation = 1;
    } else {
        generation++;
    }
    ChecksumIndexOutput segnOutput = null;
    boolean success = false;
    final Set<String> upgradedSIFiles = new HashSet<String>();
    try {
        segnOutput = new ChecksumIndexOutput(directory.createOutput(segmentsFileName, IOContext.DEFAULT));
        CodecUtil.writeHeader(segnOutput, "segments", VERSION_40);
        segnOutput.writeLong(version);
        segnOutput.writeInt(counter);
        // write counter 
        segnOutput.writeInt(size());
        // write infos 
        for (SegmentInfoPerCommit siPerCommit : this) {
            SegmentInfo si = siPerCommit.info;
            segnOutput.writeString(si.name);
            segnOutput.writeString(si.getCodec().getName());
            segnOutput.writeLong(siPerCommit.getDelGen());
            segnOutput.writeInt(siPerCommit.getDelCount());
            assert si.dir == directory;
            assert siPerCommit.getDelCount() <= si.getDocCount();
            // If this segment is pre-4.x, perform a one-time 
            // "ugprade" to write the .si file for it: 
            String version = si.getVersion();
            if (version == null || StringHelper.getVersionComparator().compare(version, "4.0") < 0) {
                if (!segmentWasUpgraded(directory, si)) {
                    String markerFileName = IndexFileNames.segmentFileName(si.name, "upgraded", Lucene3xSegmentInfoFormat.UPGRADED_SI_EXTENSION);
                    si.addFile(markerFileName);
                    final String segmentFileName = write3xInfo(directory, si, IOContext.DEFAULT);
                    upgradedSIFiles.add(segmentFileName);
                    directory.sync(Collections.singletonList(segmentFileName));
                    // Write separate marker file indicating upgrade 
                    // is completed.  This way, if there is a JVM 
                    // kill/crash, OS crash, power loss, etc. while 
                    // writing the upgraded file, the marker file 
                    // will be missing: 
                    si.addFile(markerFileName);
                    IndexOutput out = directory.createOutput(markerFileName, IOContext.DEFAULT);
                    try {
                        CodecUtil.writeHeader(out, SEGMENT_INFO_UPGRADE_CODEC, SEGMENT_INFO_UPGRADE_VERSION);
                    } finally {
                        out.close();
                    }
                    upgradedSIFiles.add(markerFileName);
                    directory.sync(Collections.singletonList(markerFileName));
                }
            }
        }
        segnOutput.writeStringStringMap(userData);
        pendingSegnOutput = segnOutput;
        success = true;
    } finally {
        if (!success) {
            // We hit an exception above; try to close the file 
            // but suppress any exception: 
            IOUtils.closeWhileHandlingException(segnOutput);
            for (String fileName : upgradedSIFiles) {
                try {
                    directory.deleteFile(fileName);
                } catch (Throwable t) {
                }
            }
            try {
                // Try not to leave a truncated segments_N file in 
                // the index: 
                directory.deleteFile(segmentsFileName);
            } catch (Throwable t) {
            }
        }
    }
}
