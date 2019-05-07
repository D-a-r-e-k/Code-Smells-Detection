//}}}  
//{{{ maybeReloadDirectory() method  
private void maybeReloadDirectory(String dir) {
    if (MiscUtilities.isURL(dir) && MiscUtilities.getProtocolOfURL(dir).equals(FavoritesVFS.PROTOCOL)) {
        if (favorites != null)
            favorites.popup = null;
    }
    // this is a dirty hack and it relies on the fact  
    // that updates for parents are sent before updates  
    // for the changed nodes themselves (if this was not  
    // the case, the browser wouldn't be updated properly  
    // on delete, etc).  
    //  
    // to avoid causing '> 1 request' errors, don't reload  
    // directory if request already active  
    if (maybeReloadRequestRunning) {
        //Log.log(Log.WARNING,this,"VFS update: request already in progress");  
        return;
    }
    // save a file -> sends vfs update. if a VFS file dialog box  
    // is shown from the same event frame as the save, the  
    // VFSUpdate will be delivered before the directory is loaded,  
    // and before the path is set.  
    if (path != null) {
        try {
            maybeReloadRequestRunning = true;
            browserView.maybeReloadDirectory(dir);
        } finally {
            VFSManager.runInAWTThread(new Runnable() {

                public void run() {
                    maybeReloadRequestRunning = false;
                }
            });
        }
    }
}
