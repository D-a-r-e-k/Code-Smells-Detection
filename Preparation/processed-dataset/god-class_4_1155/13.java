public void displaySplashScreen(final String splashFilename) {
    splash = null;
    if (splashFilename != null) {
        try {
            Image im = Toolkit.getDefaultToolkit().getImage(splashFilename);
            splash = new JWindow();
            splash.getContentPane().add(new JLabel(new ImageIcon(im)));
            splash.pack();
            Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
            splash.setLocation(center.x - splash.getWidth() / 2, center.y - splash.getHeight() / 2);
            splash.setVisible(true);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Splash fail", e);
            splash = null;
        }
    }
}
