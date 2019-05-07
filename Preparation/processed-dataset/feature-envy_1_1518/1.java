//}}}  
//{{{ HistoryButton constructor  
public HistoryButton(int type, HelpHistoryModel model) {
    super();
    arrow_button = new RolloverButton(GUIUtilities.loadIcon(jEdit.getProperty(type == BACK ? "helpviewer.back.icon" : "helpviewer.forward.icon")));
    arrow_button.setToolTipText(jEdit.getProperty(type == BACK ? "helpviewer.back.label" : "helpviewer.forward.label"));
    Box box = new Box(BoxLayout.X_AXIS);
    drop_button = new RolloverButton(GUIUtilities.loadIcon(jEdit.getProperty("dropdown-arrow.icon")));
    drop_button.addActionListener(new DropActionHandler());
    box.add(arrow_button);
    box.add(drop_button);
    this.setMaximumSize(new Dimension(drop_button.getPreferredSize().width + arrow_button.getPreferredSize().width + 5, arrow_button.getPreferredSize().height + 10));
    this.add(box);
    this.type = type;
    this.history = model;
}
