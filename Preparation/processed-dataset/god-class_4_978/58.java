//For JextLoader when it's killing Jext 
/*friendly*/
static void closeToQuit(JextFrame frame, boolean isKillingServer) {
    if (isKillingServer)
        runInBg = false;
    //so when calling closeWindow(frame), which will happen, it will close completely Jext 
    //and stop plugins. 
    frame.closeToQuit();
}
