/**
	 * {@inheritDoc}
	 */
@Override
public void testIterationStart(LoopIterationEvent event) {
    if (!USE_CACHED_SSL_CONTEXT) {
        JsseSSLManager sslMgr = (JsseSSLManager) SSLManager.getInstance();
        sslMgr.resetContext();
        notifySSLContextWasReset();
    }
}
