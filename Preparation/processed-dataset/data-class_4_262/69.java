//}}}  
//{{{ close() method  
void close() {
    setFlag(CLOSED, true);
    if (autosaveFile != null)
        autosaveFile.delete();
    // notify clients with -wait  
    if (waitSocket != null) {
        try {
            waitSocket.getOutputStream().write('\0');
            waitSocket.getOutputStream().flush();
            waitSocket.getInputStream().close();
            waitSocket.getOutputStream().close();
            waitSocket.close();
        } catch (IOException io) {
        }
    }
}
