/**
     * Close all opened InputStreams and Readers opened by this parser.
     */
public void closeReaders() {
    // close all readers  
    for (int i = fReaderStack.size() - 1; i >= 0; i--) {
        try {
            ((Reader) fReaderStack.pop()).close();
        } catch (IOException e) {
        }
    }
}
