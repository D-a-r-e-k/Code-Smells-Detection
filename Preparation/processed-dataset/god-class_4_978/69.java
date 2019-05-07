/**
   * Attempts to load Jext in a single JVM instance only. If this instance of
   * Jext is the very first to be loaded, then a ServerSocket is opened. Otherwise,
   * this instance attemps to connect on to a specific socket port to tell other
   * Jext instance to create a new window. This avoids to load on JVM for each
   * launch of Jext. Opened port is securised so that no security hole is created.
   * @param args The arguments of the new Jext window
   */
public static void loadInSingleJVMInstance(String[] args) {
    try {
        File security = new File(SETTINGS_DIRECTORY + ".security");
        if (!security.exists())
            isServerEnabled = true;
        else {
            BufferedReader reader = new BufferedReader(new FileReader(security));
            isServerEnabled = new Boolean(reader.readLine()).booleanValue();
            reader.close();
        }
    } catch (IOException ioe) {
    }
    if (!isServerEnabled && !runInBg)
        return;
    File authorizationKey = new File(SETTINGS_DIRECTORY + ".auth-key");
    // if the authorization key exists, another Jext instance may 
    // be running 
    if (authorizationKey.exists()) {
        // we attempt to log onto the other instance of Jext(but only if we are not backgrounding; no 
        // more than one bg instance is started, and if we are bg we don't pass anything to the other instance. 
        try {
            BufferedReader reader = new BufferedReader(new FileReader(authorizationKey));
            int port = Integer.parseInt(reader.readLine());
            String key = reader.readLine();
            reader.close();
            Socket client = new Socket("127.0.0.1", JEXT_SERVER_PORT + port);
            if (!runInBg) {
                //now that we made sure that the other instance exists, if backgrounding we do 
                //nothing 
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                StringBuffer _args = new StringBuffer();
                if (goingToKill) {
                    _args.append("kill");
                } else {
                    _args.append("load_jext:");
                    for (int i = 0; i < args.length; i++) {
                        _args.append(args[i]);
                        if (i != args.length - 1)
                            _args.append('?');
                    }
                }
                _args.append(':').append(key);
                writer.write(_args.toString());
                writer.flush();
                writer.close();
            } else
                System.out.println("Jext is already running, either in background or foreground.");
            client.close();
            System.exit(5);
        } catch (Exception e) {
            // no other jext instance is running, we delete the auth. file 
            authorizationKey.delete();
            if (goingToKill) {
                System.err.println("No jext instance found!");
                System.exit(0);
            } else
                jextLoader = new JextLoader();
        }
    } else if (!goingToKill) {
        jextLoader = new JextLoader();
    } else {
        System.err.println("No jext instance found!");
        System.exit(0);
    }
}
