//{{{ Private members 
//{{{ init() 
private void _init() {
    ((Container) getLayeredPane()).addContainerListener(new ContainerHandler());
    getContentPane().addContainerListener(new ContainerHandler());
    keyHandler = new KeyHandler();
    addKeyListener(keyHandler);
    addWindowListener(new WindowHandler());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
}
