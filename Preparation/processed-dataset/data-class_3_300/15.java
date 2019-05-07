/** {@inheritDoc} */
@Override
public boolean interrupt() {
    HttpClient client = savedClient;
    if (client != null) {
        savedClient = null;
        // TODO - not sure this is the best method 
        final HttpConnectionManager httpConnectionManager = client.getHttpConnectionManager();
        if (httpConnectionManager instanceof SimpleHttpConnectionManager) {
            // Should be true 
            ((SimpleHttpConnectionManager) httpConnectionManager).shutdown();
        }
    }
    return client != null;
}
