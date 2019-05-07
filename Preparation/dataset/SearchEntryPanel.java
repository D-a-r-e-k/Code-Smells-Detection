package net.suberic.pooka.gui.search;
import net.suberic.pooka.*;
import net.suberic.util.VariableBundle;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.mail.search.SearchTerm;

/**
 * This is a full panel for making search options.  This panel combines lots
 * of SearchEntryForms with some controls for adding and removing 
 * SearchEntryForms.  getSearchTerm() returns the composite SearchTerm
 * made from all of the SearchEntryForms.
 */
public class SearchEntryPanel extends JPanel {

    public static int FIRST = -1;
    public static int AND = 0;
    public static int OR = 1;

    public static String AND_LABEL = Pooka.getProperty("Search.button.and.label", "And");
    public static String OR_LABEL = Pooka.getProperty("Search.button.or.label", "Or");
    
    JPanel conditionPanel;
    JPanel entryPanel;
    JScrollPane entryScrollPane;
    JButton buttonOne, buttonTwo;
    Vector searchTerms = new Vector();
    SearchTermManager manager;

    class SearchEntryPair {
	SearchEntryForm form;
	SearchConnector connector;

	public SearchEntryPair(SearchEntryForm newForm, int newType) {
	    form = newForm;
	    connector = new SearchConnector(newType);
	}
    }

    class SearchConnector {
	
	JComboBox list;
	SearchConnector(int newType) {
	    String[] choices = new String[2];
	    choices[0] = AND_LABEL;
	    choices[1] = OR_LABEL;
	    list = new JComboBox(choices);
	    
	    if (newType < 2) {
		
		list.setSelectedIndex(newType);
	    } else
		list.setSelectedIndex(0);
	    
	}
	
	public int getType() {
	    return list.getSelectedIndex();
	}

	public JComboBox getCombo() {
	    return list;
	}

    }

    /**
     * Creates a new SearchEntryPanel.
     */
    public SearchEntryPanel(SearchTermManager newManager) {
	manager = newManager;
	populatePanel();
    }

    /**
     * Creates a new SearchEntryPanel populated by the SearchTerm defined
     * by the given Property.
     */
    public SearchEntryPanel(SearchTermManager newManager, String property) {
	this(newManager, property, Pooka.getResources());
    }

    /**
     * Creates a new SearchEntryPanel populated by the SearchTerm defined
     * by the given Property.
     */
    public SearchEntryPanel(SearchTermManager newManager, String property, VariableBundle bundle) {
	this(newManager);
	setSearchTerm(property, bundle);
    }

    
    /**
     * Populates the panel with all the appropriate widgets.  Called by
     * the constructor.
     */
    public void populatePanel() {
	this.setLayout(new java.awt.BorderLayout());
	entryPanel = new JPanel();
	entryPanel.setLayout(new BoxLayout(entryPanel,BoxLayout.Y_AXIS));

	addSearchEntryForm(FIRST);

	createConditionPanel();

	entryScrollPane = new JScrollPane(entryPanel);
	int defaultHeight = entryPanel.getPreferredSize().height;
	entryScrollPane.setPreferredSize(new java.awt.Dimension(entryPanel.getPreferredSize().width + 15, defaultHeight * 5));

	this.add(conditionPanel, java.awt.BorderLayout.SOUTH);
	this.add(entryScrollPane, java.awt.BorderLayout.CENTER);

    }

    /**
     * This creates the conditionPanel.  This is always the bottom panel,
     * and contains the and / or buttons.  Pressing one of these buttons
     * will create a new SearchEntryForm.
     */
    private void createConditionPanel() {
	JPanel jp = new JPanel();
	jp.setLayout(new FlowLayout());
	
	buttonOne = new JButton(Pooka.getProperty("Search.button.and.label", "And"));
	buttonOne.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    addSearchEntryForm(AND);
		}
	    });

	buttonTwo = new JButton(Pooka.getProperty("Search.button.or.label", "Or"));
	buttonTwo.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    addSearchEntryForm(OR);
		}
	    });

	jp.add(buttonOne);
	jp.add(buttonTwo);

	conditionPanel=jp;
    }

    public void addSearchEntryForm(int type) {
	if (type == FIRST) {
	    SearchEntryForm sef = new SearchEntryForm(manager);
	    SearchConnector sc = new SearchConnector(AND);
	    //java.awt.Component blankPanel = Box.createRigidArea(sc.getCombo().getPreferredSize());
	    //blankPanel.setSize(sc.getCombo().getSize());
	    //blankPanel.setPreferredSize(sc.getCombo().getPreferredSize());
	    Box fullPanel = new Box(BoxLayout.X_AXIS);
	    searchTerms.add(new SearchEntryPair(sef, AND));
	    //fullPanel.add(blankPanel);
	    fullPanel.add(sef.getPanel());
	    entryPanel.add(fullPanel);
	} else {
	    SearchEntryForm sef = new SearchEntryForm(manager);
	    SearchEntryPair pair = new SearchEntryPair(sef, type);
	    searchTerms.add(pair);

	    JPanel newSearchPanel = new JPanel();
	    newSearchPanel.add(pair.connector.getCombo());
	    newSearchPanel.add(sef.getPanel());
	    entryPanel.add(newSearchPanel);
	    //entryPanel.add(pair.connector.getCombo());
	    //entryPanel.add(sef.getPanel());

	    entryPanel.revalidate();
	    Runnable runMe = new Runnable() {
		    public void run() {
			JScrollBar vsb = entryScrollPane.getVerticalScrollBar();
			vsb.setValue(vsb.getMaximum());
			//entryPanel.repaint();
		    }
		};

	    SwingUtilities.invokeLater(runMe);
	}

    }

    /**
     * Returns the SearchTerm specified by this SearchEntryPanel.
     */
    public SearchTerm getSearchTerm() throws java.text.ParseException {
	if (Pooka.isDebug())
	    System.out.println("calling SearchEntryPanel.getSearchTerm()");
	if (searchTerms.size() > 0) {
	    if (Pooka.isDebug())
		System.out.println("SearchEntryPanel:  searchTerms.size() > 0.");
	    SearchEntryPair pair = (SearchEntryPair) searchTerms.elementAt(0);
	    SearchTerm term = pair.form.generateSearchTerm();
	    if (Pooka.isDebug())
		System.out.println("SearchEntryPanel:  setting term to " + term);
	    for (int i = 1; i < searchTerms.size(); i++) {
		SearchEntryPair newPair = (SearchEntryPair) searchTerms.elementAt(i);
		SearchTerm newTerm = newPair.form.generateSearchTerm();
		if (newPair.connector.getType() == AND) {
		    term = new javax.mail.search.AndTerm(term, newTerm);
		} else if (newPair.connector.getType() == OR) {
		    term = new javax.mail.search.OrTerm(term, newTerm);
		}
	    }

	    return term;
	} else
	    return null;
    }

    /**
     * This sets the currently depicted SearchTerm to the one defined by
     * the rootProperty in the given VariableBundle.
     */
    public void setSearchTerm(String rootProperty, VariableBundle bundle) {
	searchTerms = new Vector();
	entryPanel = new JPanel();
	entryPanel.setLayout(new BoxLayout(entryPanel,BoxLayout.Y_AXIS));
	addSearchTermProperty(rootProperty, bundle, FIRST);
	entryScrollPane.setViewportView(entryPanel);
	entryPanel.revalidate();
    }

    /**
     * Adds a SearchTerm property.  This actually goes through and divides
     * up single vs. compound search terms.  addSignleSearchTerm() actually
     * adds the editor.
     */
    private void addSearchTermProperty(String rootProperty, VariableBundle bundle, int type) {
	String termType = bundle.getProperty(rootProperty + ".type", "simple");
	if (termType.equalsIgnoreCase("compound")) {
	    Vector subProps = bundle.getPropertyAsVector(rootProperty + ".subTerms", "");
	    int subType = AND;
	    String operation = bundle.getProperty(rootProperty + ".operation", "and");
	    if (operation.equalsIgnoreCase("and"))
		subType = AND;
	    else
		subType = OR;

	    for (int i = 0; i < subProps.size(); i++) {
		String nextTerm = (String) subProps.elementAt(i);
		if (i == 0)
		    addSearchTermProperty(nextTerm, bundle, type);
		else
		    addSearchTermProperty(nextTerm, bundle, subType);
	    }
	} else {
	    addSingleSearchTerm(rootProperty, bundle, type);
	}
    }
    
    /**
     * Adds a single search term.
     */
    private void addSingleSearchTerm(String rootProperty, VariableBundle bundle, int type) {
	if (type == FIRST) {
	    SearchEntryForm sef = new SearchEntryForm(manager, rootProperty, bundle);
	    SearchConnector sc = new SearchConnector(AND);
	    Box fullPanel = new Box(BoxLayout.X_AXIS);
	    searchTerms.add(new SearchEntryPair(sef, AND));
	    fullPanel.add(sef.getPanel());
	    entryPanel.add(fullPanel);
	} else {
	    SearchEntryForm sef = new SearchEntryForm(manager, rootProperty, bundle);
	    SearchEntryPair pair = new SearchEntryPair(sef, type);
	    searchTerms.add(pair);
	    
	    JPanel newSearchPanel = new JPanel();
	    newSearchPanel.add(pair.connector.getCombo());
	    newSearchPanel.add(sef.getPanel());
	    entryPanel.add(newSearchPanel);
	}
    }

    /**
     * This returns the defined SearchTerm as a set of Properties, with the
     * given rootProperty as the root.
     */
    public java.util.Properties generateSearchTermProperties(String rootProperty) {
	java.util.Properties returnValue = new java.util.Properties();

	Vector v = new Vector (searchTerms);
	addToProperties(v, rootProperty, returnValue);
	return returnValue;
    }

    /**
     * Adds the given properties to the list.
     */
    private void addToProperties(SearchEntryPair pair, String rootProperty, java.util.Properties props) {
	Properties tmpProperties = pair.form.generateSearchTermProperties(rootProperty);
	Enumeration keys = tmpProperties.keys();
	while (keys.hasMoreElements()) {
	    String current = (String) keys.nextElement();
	    props.setProperty(current, tmpProperties.getProperty(current));
	}
    }

    /**
     * Adds the searchTerms given by the pairList to the properties list, with
     * rootProperty as the root property.
     */
    private void addToProperties(Vector pairList, String rootProperty, Properties props) {
	if (pairList.size() == 1) {
	    addToProperties((SearchEntryPair) pairList.remove(0), rootProperty, props);
	} else {
	    addToProperties((SearchEntryPair) pairList.remove(0), rootProperty + ".term1", props);
	    int type = ((SearchEntryPair) pairList.elementAt(0)).connector.getType();
	    addToProperties(pairList, rootProperty + ".term2", props);
	    props.setProperty(rootProperty + ".type", "compound");
	    props.setProperty(rootProperty + ".subTerms", rootProperty + ".term1:" + rootProperty + ".term2");
	    if (type == AND) {
		props.setProperty(rootProperty + ".operation", "and");
	    } else {
		props.setProperty(rootProperty + ".operation", "or");
	    }
	}
    }

    /**
     * Sets whether or not this panel is enabled.
     */
    public void setEnabled(boolean newValue) {
	for (int i = 0; i < searchTerms.size(); i++) {
	    SearchEntryPair currentPair = (SearchEntryPair) searchTerms.elementAt(i);
	    currentPair.form.setEnabled(newValue);
	    currentPair.connector.getCombo().setEnabled(newValue);
	}
	buttonOne.setEnabled(newValue);
	buttonTwo.setEnabled(newValue);

    }
}
