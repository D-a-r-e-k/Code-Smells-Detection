//}}} 
//{{{ getOpenBuffer() 
/**
     * Gets the DocumentBuffer for this file if the file is open already. Returns
     * null if the file is not open.
     * @param file The file that is open in jsXe
     * @return the DocumentBuffer for the given file or null if the file not open.
     */
public static DocumentBuffer getOpenBuffer(File file) {
    boolean caseInsensitiveFilesystem = (File.separatorChar == '\\' || File.separatorChar == ':');
    for (int i = 0; i < m_buffers.size(); i++) {
        try {
            DocumentBuffer buffer = (DocumentBuffer) m_buffers.get(i);
            if (buffer.equalsOnDisk(file)) {
                return buffer;
            }
        } catch (IOException ioe) {
            exiterror(null, ioe, 1);
        }
    }
    return null;
}
