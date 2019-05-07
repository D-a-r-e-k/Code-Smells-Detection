final void rollbackCommit(Directory dir) {
    if (pendingSegnOutput != null) {
        // Suppress so we keep throwing the original exception 
        // in our caller 
        IOUtils.closeWhileHandlingException(pendingSegnOutput);
        pendingSegnOutput = null;
        // Must carefully compute fileName from "generation" 
        // since lastGeneration isn't incremented: 
        final String segmentFileName = IndexFileNames.fileNameFromGeneration(IndexFileNames.SEGMENTS, "", generation);
        // Suppress so we keep throwing the original exception 
        // in our caller 
        IOUtils.deleteFilesIgnoringExceptions(dir, segmentFileName);
    }
}
