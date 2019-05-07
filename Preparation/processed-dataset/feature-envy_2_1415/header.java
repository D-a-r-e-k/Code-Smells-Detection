void method0() { 
private JPanel labelPanel = new JPanel();
private JPanel modelPanel = new JPanel();
private JPanel actionPanel = new JPanel();
private JButton okButton = new JButton("OK");
private JButton cancelButton = new JButton("Cancel");
private JCheckBox useModelLabel = new JCheckBox("use model label");
private JTextField labelField = new JTextField();
private JTree chooserTree = new JTree();
public JPanel east = new JPanel(new GridBagLayout());
public JPanel west = new JPanel();
public JPanel north = new JPanel();
public JPanel south = new JPanel();
public JPanel center = new JPanel();
public static boolean editorInsideCell = true;
/**
	 * A temporary clone of the business object to work with before commiting
	 * the change (allows to undo).
	 */
private BusinessObjectWrapper2 newModel;
/**
	 * the old user object
	 */
private BusinessObjectWrapper2 oldModel;
/**
	 * a reference to the editor: here we only use it to force the end of the
	 * editing after the OK button or Cancel button is pressed
	 */
private static CellEditor cellEditor;
}
