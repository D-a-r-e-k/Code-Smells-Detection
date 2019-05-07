public void showMapControls(boolean value) {
    if (value && freeColClient.isInGame()) {
        if (mapControls == null) {
            try {
                String className = freeColClient.getClientOptions().getString(ClientOptions.MAP_CONTROLS);
                Class<?> controls = Class.forName("net.sf.freecol.client.gui.panel." + className);
                mapControls = (MapControls) controls.getConstructor(FreeColClient.class, GUI.class).newInstance(freeColClient, this);
            } catch (Exception e) {
                mapControls = new CornerMapControls(freeColClient, this);
            }
        }
        mapControls.update();
    }
    if (mapControls != null) {
        if (value) {
            if (!mapControls.isShowing()) {
                mapControls.addToComponent(canvas);
            }
            mapControls.update();
        } else {
            if (mapControls.isShowing()) {
                mapControls.removeFromComponent(canvas);
            }
        }
    }
}
