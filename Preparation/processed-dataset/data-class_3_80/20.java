//}}} 
//{{{ getDocumentBuffers() 
/**
     * Gets an array of the open Buffers.
     * @return An array of DocumentBuffers that jsXe currently has open.
     */
public static DocumentBuffer[] getDocumentBuffers() {
    DocumentBuffer[] buffers = new DocumentBuffer[m_buffers.size()];
    for (int i = 0; i < m_buffers.size(); i++) {
        buffers[i] = (DocumentBuffer) m_buffers.get(i);
    }
    return buffers;
}
