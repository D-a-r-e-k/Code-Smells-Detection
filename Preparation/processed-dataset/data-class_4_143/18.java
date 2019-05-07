/**
   * Opens a new window, but eventually does not show it.
   * @param args The command line arguments
   * @param toShow When true the frame is shown
   */
//Note: until code doesn't need it, better leaving it only for the package. 
/*friendly*/
static JextFrame newWindow(String args[], boolean toShow) {
    synchronized (instances) {
        JextFrame window;
        if (toShow && builtTextArea != null) {
            if (args != null)
                for (int i = 0; i < args.length; i++) builtTextArea.open(args[i]);
            builtTextArea.setVisible(true);
            window = builtTextArea;
            builtTextArea = null;
        } else {
            window = new JextFrame(args, toShow);
            //if (toShow) 
            instances.add(window);
        }
        return window;
    }
}
