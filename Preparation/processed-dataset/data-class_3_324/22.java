private void handleConfigEvent(Map<String, Object> map) {
    if (map != null) {
        Integer frag_size = (Integer) map.get("frag_size");
        if (frag_size != null) {
            if (frag_size > max_credits) {
                log.warn("The fragmentation size of the fragmentation protocol is " + frag_size + ", which is greater than the max credits. While this is not incorrect, " + "it may lead to long blockings. Frag size should be less than max_credits " + "(http://jira.jboss.com/jira/browse/JGRP-590)");
            }
            frag_size_received = true;
        }
    }
}
