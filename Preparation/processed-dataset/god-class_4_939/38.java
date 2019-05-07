/***********************************************************************************************
*  ACTIONSTORE -METHODS (for checking and storeded users...
************************************************************************************************/
public void storeUser(Vector<Object> v, int action, String message, long millis, String storedBy) {
    for (Enumeration<?> e = v.elements(); e.hasMoreElements(); ) {
        User u = (User) e.nextElement();
        this.storeUser(action, u, message, millis, storedBy);
    }
}
