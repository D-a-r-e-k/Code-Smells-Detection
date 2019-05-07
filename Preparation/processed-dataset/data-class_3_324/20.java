public void start() throws Exception {
    super.start();
    if (!frag_size_received) {
        log.warn("No fragmentation protocol was found. When flow control (e.g. FC or SFC) is used, we recommend " + "a fragmentation protocol, due to http://jira.jboss.com/jira/browse/JGRP-590");
    }
    running = true;
}
