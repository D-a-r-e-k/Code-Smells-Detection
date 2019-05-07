//}}} 
//{{{ getContents() 
public ListModel getContents() {
    ArrayList contents = getActivityLogContents();
    DefaultListModel contentsJListModel = new DefaultListModel();
    JTextArea newArea = new JTextArea(5, 30);
    for (Iterator it = contents.iterator(); it.hasNext(); ) {
        String line = (String) it.next();
        contentsJListModel.addElement(line);
    }
    return contentsJListModel;
}
