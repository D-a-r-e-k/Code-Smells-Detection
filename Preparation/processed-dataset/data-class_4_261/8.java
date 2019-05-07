//}}}  
//{{{ handleMessage() method  
public void handleMessage(EBMessage msg) {
    if (msg instanceof PropertiesChanged)
        propertiesChanged();
    else if (msg instanceof BufferUpdate) {
        BufferUpdate bmsg = (BufferUpdate) msg;
        if (bmsg.getWhat() == BufferUpdate.CREATED || bmsg.getWhat() == BufferUpdate.CLOSED)
            browserView.updateFileView();
    } else if (msg instanceof PluginUpdate) {
        PluginUpdate pmsg = (PluginUpdate) msg;
        if ((pmsg.getWhat() == PluginUpdate.LOADED || pmsg.getWhat() == PluginUpdate.UNLOADED) && plugins != null) {
            plugins.updatePopupMenu();
        }
    } else if (msg instanceof VFSUpdate) {
        maybeReloadDirectory(((VFSUpdate) msg).getPath());
    }
}
