void method0() { 
private static final Logger log = LoggingManager.getLoggerForClass();
/** retry count to be used (default 1); 0 = disable retries */
private static final int RETRY_COUNT = JMeterUtils.getPropDefault("httpclient3.retrycount", 1);
private static final String HTTP_AUTHENTICATION_PREEMPTIVE = "http.authentication.preemptive";
// $NON-NLS-1$ 
private static final boolean canSetPreEmptive;
// OK to set pre-emptive auth? 
private static final ThreadLocal<Map<HostConfiguration, HttpClient>> httpClients = new ThreadLocal<Map<HostConfiguration, HttpClient>>() {

    @Override
    protected Map<HostConfiguration, HttpClient> initialValue() {
        return new HashMap<HostConfiguration, HttpClient>();
    }
};
// Needs to be accessible by HTTPSampler2 
volatile HttpClient savedClient;
}
