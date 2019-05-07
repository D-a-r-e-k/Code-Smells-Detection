// check the command line arguments 
private static String[] parseOptions(String[] args) {
    // Trap bg flag 
    int argLn = args.length;
    ArrayList newArgs = new ArrayList(argLn);
    //First, it checks defaults: if the user actived -showbg by default, read this setting. 
    try {
        File showbg = new File(SETTINGS_DIRECTORY + ".showBg");
        if (!showbg.exists())
            keepInMemory = false;
        else {
            BufferedReader reader = new BufferedReader(new FileReader(showbg));
            keepInMemory = new Boolean(reader.readLine()).booleanValue();
            reader.close();
        }
    } catch (IOException ioe) {
    }
    defaultKeepInMemory = runInBg = keepInMemory;
    //Then, let's read options. 
    for (int i = 0; i < argLn; i++) {
        //Whenever it encounter an option it resets all contrary ones. 
        if ("-bg".equals(args[i])) {
            runInBg = true;
            keepInMemory = false;
            goingToKill = false;
        } else if ("-kill".equals(args[i])) {
            goingToKill = true;
            keepInMemory = false;
            runInBg = false;
        } else if ("-showbg".equals(args[i])) {
            runInBg = true;
            keepInMemory = true;
            goingToKill = false;
        } else if ("-debug".equals(args[i]))
            DEBUG = true;
        else
            newArgs.add(args[i]);
    }
    return (String[]) newArgs.toArray(new String[0]);
}
