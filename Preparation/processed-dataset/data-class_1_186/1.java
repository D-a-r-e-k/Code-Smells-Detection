/**
   * Creates a VariableBundle that uses a JDBC connection.
   */
public VariableBundle createVariableBundle(String fileName, VariableBundle defaults) {
    System.err.println("using jdbc.");
    //Thread.currentThread().dumpStack(); 
    //System.setProperty("JDBCPreferences.driverName", "com.mysql.jdbc.Driver"); 
    //System.setProperty("JDBCPreferences.url", "jdbc:mysql://localhost:3306/dbname"); 
    //System.setProperty("JDBCPreferences.user", ""); 
    //System.setProperty("JDBCPreferences.password", ""); 
    System.setProperty("java.util.prefs.PreferencesFactory", "net.suberic.util.prefs.JDBCPreferencesFactory");
    /*
    System.out.println("driver: " + System.getProperty("JDBCPreferences.driverName"));
    System.out.println("url: " + System.getProperty("JDBCPreferences.url"));
    System.out.println("user: " + System.getProperty("JDBCPreferences.user"));
    System.out.println("password: " + System.getProperty("JDBCPreferences.password"));
    */
    try {
        return new net.suberic.util.PreferencesVariableBundle(Class.forName("net.suberic.pooka.Pooka"), defaults);
    } catch (Exception e) {
        e.printStackTrace();
        return defaults;
    }
}
