public MessageDialog(Frame parent, String sender, String msg) {
    super(parent, "Msg from " + sender);
    Button ok = new Button("OK");
    setLayout(new BorderLayout());
    setBackground(Color.white);
    ok.setFont(default_font);
    text.setFont(default_font);
    text.setEditable(false);
    text.setText(msg);
    ok.addActionListener(this);
    add("Center", text);
    add("South", ok);
    setSize(300, 150);
    Point my_loc = parent.getLocation();
    my_loc.x += 50;
    my_loc.y += 150;
    setLocation(my_loc);
    Toolkit.getDefaultToolkit().beep();
    show();
}
