final void finishCommit(Directory dir) throws IOException {
    if (pendingSegnOutput == null) {
        throw new IllegalStateException("prepareCommit was not called");
    }
    boolean success = false;
    try {
        pendingSegnOutput.finishCommit();
        success = true;
    } finally {
        if (!success) {
            // Closes pendingSegnOutput & deletes partial segments_N: 
            rollbackCommit(dir);
        } else {
            success = false;
            try {
                pendingSegnOutput.close();
                success = true;
            } finally {
                if (!success) {
                    // Closes pendingSegnOutput & deletes partial segments_N: 
                    rollbackCommit(dir);
                } else {
                    pendingSegnOutput = null;
                }
            }
        }
    }
    // NOTE: if we crash here, we have left a segments_N 
    // file in the directory in a possibly corrupt state (if 
    // some bytes made it to stable storage and others 
    // didn't).  But, the segments_N file includes checksum 
    // at the end, which should catch this case.  So when a 
    // reader tries to read it, it will throw a 
    // CorruptIndexException, which should cause the retry 
    // logic in SegmentInfos to kick in and load the last 
    // good (previous) segments_N-1 file. 
    final String fileName = IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS, "", generation);
    success = false;
    try {
        dir.sync(Collections.singleton(fileName));
        success = true;
    } finally {
        if (!success) {
            try {
                dir.deleteFile(fileName);
            } catch (Throwable t) {
            }
        }
    }
    lastGeneration = generation;
    try {
        IndexOutput genOutput = dir.createOutput(IndexFileNames.SEGMENTS_GEN, IOContext.READONCE);
        try {
            genOutput.writeInt(FORMAT_SEGMENTS_GEN_CURRENT);
            genOutput.writeLong(generation);
            genOutput.writeLong(generation);
        } finally {
            genOutput.close();
            dir.sync(Collections.singleton(IndexFileNames.SEGMENTS_GEN));
        }
    } catch (Throwable t) {
        // It's OK if we fail to write this file since it's 
        // used only as one of the retry fallbacks. 
        try {
            dir.deleteFile(IndexFileNames.SEGMENTS_GEN);
        } catch (Throwable t2) {
        }
    }
}
