void method0() { 
private static final Log log = LogFactory.getLog(JavaGroupsBroadcastingListener.class);
private static final String BUS_NAME = "OSCacheBus";
private static final String CHANNEL_PROPERTIES = "cache.cluster.properties";
private static final String MULTICAST_IP_PROPERTY = "cache.cluster.multicast.ip";
/**
    * The first half of the default channel properties. They default channel properties are:
    * <pre>
    * UDP(mcast_addr=*.*.*.*;mcast_port=45566;ip_ttl=32;\
    * mcast_send_buf_size=150000;mcast_recv_buf_size=80000):\
    * PING(timeout=2000;num_initial_members=3):\
    * MERGE2(min_interval=5000;max_interval=10000):\
    * FD_SOCK:VERIFY_SUSPECT(timeout=1500):\
    * pbcast.NAKACK(gc_lag=50;retransmit_timeout=300,600,1200,2400,4800;max_xmit_size=8192):\
    * UNICAST(timeout=300,600,1200,2400):\
    * pbcast.STABLE(desired_avg_gossip=20000):\
    * FRAG(frag_size=8096;down_thread=false;up_thread=false):\
    * pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;shun=false;print_local_addr=true)
    * </pre>
    *
    * Where <code>*.*.*.*</code> is the specified multicast IP, which defaults to <code>231.12.21.132</code>.
    */
private static final String DEFAULT_CHANNEL_PROPERTIES_PRE = "UDP(mcast_addr=";
/**
    * The second half of the default channel properties. They default channel properties are:
    * <pre>
    * UDP(mcast_addr=*.*.*.*;mcast_port=45566;ip_ttl=32;\
    * mcast_send_buf_size=150000;mcast_recv_buf_size=80000):\
    * PING(timeout=2000;num_initial_members=3):\
    * MERGE2(min_interval=5000;max_interval=10000):\
    * FD_SOCK:VERIFY_SUSPECT(timeout=1500):\
    * pbcast.NAKACK(gc_lag=50;retransmit_timeout=300,600,1200,2400,4800;max_xmit_size=8192):\
    * UNICAST(timeout=300,600,1200,2400):\
    * pbcast.STABLE(desired_avg_gossip=20000):\
    * FRAG(frag_size=8096;down_thread=false;up_thread=false):\
    * pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;shun=false;print_local_addr=true)
    * </pre>
    *
    * Where <code>*.*.*.*</code> is the specified multicast IP, which defaults to <code>231.12.21.132</code>.
    */
private static final String DEFAULT_CHANNEL_PROPERTIES_POST = ";mcast_port=45566;ip_ttl=32;mcast_send_buf_size=150000;mcast_recv_buf_size=80000):" + "PING(timeout=2000;num_initial_members=3):MERGE2(min_interval=5000;max_interval=10000):FD_SOCK:VERIFY_SUSPECT(timeout=1500):" + "pbcast.NAKACK(gc_lag=50;retransmit_timeout=300,600,1200,2400,4800;max_xmit_size=8192):UNICAST(timeout=300,600,1200,2400):pbcast.STABLE(desired_avg_gossip=20000):" + "FRAG(frag_size=8096;down_thread=false;up_thread=false):pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;shun=false;print_local_addr=true)";
private static final String DEFAULT_MULTICAST_IP = "231.12.21.132";
private NotificationBus bus;
}
