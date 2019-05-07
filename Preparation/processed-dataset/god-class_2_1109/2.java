/**
     *  Gets the remoteInfo attribute of the HttpSPMFileSystem object
     *
     *@param  cb  Callback to call when done
     */
public void getRemoteInfo(Runnable cb) {
    if (!initialized) {
        super.initialize();
        unknownHost = false;
        if (!isDownloading) {
            callbacks = new Vector();
            callbacks.add(cb);
            isDownloading = true;
            statusDialog = new HttpStatusDialog();
            (new Thread() {

                public void run() {
                    scanPlugins();
                    if (!unknownHost)
                        scanToolScripts();
                    if (!unknownHost)
                        scanObjectScripts();
                    if (!unknownHost)
                        scanStartupScripts();
                    isDownloading = false;
                    initialized = true;
                    for (int i = 0; i < callbacks.size(); ++i) ((Runnable) callbacks.elementAt(i)).run();
                    statusDialog.dispose();
                    statusDialog = null;
                }
            }).start();
        } else
            callbacks.add(cb);
    } else
        cb.run();
}
