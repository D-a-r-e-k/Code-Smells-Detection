//}}}  
/* We do this stuff because the browser is not able to handle
	 * more than one request yet */
//{{{ startRequest() method  
private boolean startRequest() {
    if (requestRunning) {
        // dump stack trace for debugging purposes  
        Log.log(Log.DEBUG, this, new Throwable("For debugging purposes"));
        GUIUtilities.error(this, "browser-multiple-io", null);
        return false;
    } else {
        requestRunning = true;
        return true;
    }
}
