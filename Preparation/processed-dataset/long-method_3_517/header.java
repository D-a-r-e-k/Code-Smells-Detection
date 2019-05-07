void method0() { 
public static final short INVALID = -1;
public static final short IDLE = 0;
public static final short ACTIVE = 1;
private volatile PreparedStatement select = null, update = null, updateLastlogin = null, insert = null;
private volatile String selStrg = null, insStrg = null, updStrg = null, updLastloginStrg = null;
private DbProperties dbp;
private volatile boolean isValid = true;
ConnectionPool pool;
Connection con = null;
int id;
volatile int sCnt = 0;
long validUntil;
volatile boolean isActive = false, hasBeenUsed = false, cleanedUp = false;
ResultSet rs;
}
