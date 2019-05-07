/*   Copyright (C) 2003 Finalist IT Group
 *
 *   This file is part of JAG - the Java J2EE Application Generator
 *
 *   JAG is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *   JAG is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   You should have received a copy of the GNU General Public License
 *   along with JAG; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.finalist.jaggenerator;

import com.finalist.jag.JApplicationGen;
import com.finalist.jag.uml.Jag2UMLGenerator;
import com.finalist.jag.uml.UML2JagGenerator;
import com.finalist.jag.util.TemplateString;
import com.finalist.jaggenerator.modules.*;
import com.finalist.jaggenerator.template.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.lf5.LogLevel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Main class for the JAG GUI.
 *
 * @author Hillebrand Gelderblom, Rudie Ekkelenkamp, Michael O'Connor - Finalist IT Group
 * @version $Revision: 1.73 $, $Date: 2006/05/24 15:47:03 $
 */
public class JagGenerator extends JFrame {

    static Log log = LogFactory.getLog(JagGenerator.class);
    private ConsoleLogger logger;
    private boolean offlineMode = false;

    private DefaultTreeModel treeModel = null;
    public Root root = null;
    private File file = null;
    private File outputDir = null;
    private static File applicationFileDir = new File(".");
    public static JagGenerator jagGenerator;
    private static GenericJdbcManager conManager;
    private static final int SPLIT_PANE_WIDTH = 400;
    private static boolean relationsEnabled = true;
    private static Thread runningThread;
    private static final Icon CANCEL_ICON = new ImageIcon("../images/cancel.png");
    private static final Icon RUN_ICON = new ImageIcon("../images/execute.png");
    private static final String RUN_ACTION = "run";
    private static final String STOP_ACTION = "stop";
    private static final HashMap entities = new HashMap();
    private static final HashMap entitiesByTableName = new HashMap();
    private static final String XMI_SUFFIX = ".xmi";


    private static final HashMap FILECHOOSER_START_DIR = new HashMap();
    private static final String FILECHOOSER_UMLEXPORT = "UML export";
    private static final String FILECHOOSER_UMLIMPORT = "UML import";
    public static final String FILECHOOSER_APPFILE_OPEN = "AppFile open";
    private static final String FILECHOOSER_APPFILE_SAVE = "AppFile save";

    public final static String TEMPLATE_USE_RELATIONS = "useRelations";
    public final static String TEMPLATE_USE_MOCK = "useMock";
    public final static String TEMPLATE_USE_JAVA5 = "useJava5";
    public final static String TEMPLATE_USE_WEB_SERVICE = "useWebService";
    public final static String TEMPLATE_USE_SECURITY = "useSecurity";

    public final static String TEMPLATE_WEB_TIER = "webTier";
    public final static String TEMPLATE_WEB_TIER_STRUTS1_2 = "Struts 1.2";
    public final static String TEMPLATE_WEB_TIER_TAPESTRY4 = "Tapestry 4";
    public final static String TEMPLATE_WEB_TIER_SWING = "Swing";

    public final static String TEMPLATE_BUSINESS_TIER = "businessTier";
    public final static String TEMPLATE_BUSINESS_TIER_EJB2 = "EJB 2.0";
    public final static String TEMPLATE_BUSINESS_TIER_EJB3 = "EJB 3.0";
    public final static String TEMPLATE_BUSINESS_TIER_HIBERNATE2 = "Hibernate 2";
    public final static String TEMPLATE_BUSINESS_TIER_HIBERNATE3 = "Hibernate 3";
    public final static String TEMPLATE_BUSINESS_TIER_MOCK = "Mock";

    public final static String TEMPLATE_SERVICE_TIER = "serviceTier";
    public final static String TEMPLATE_SERVICE_TIER_SERVICE_LOCATOR = "ServiceLocator";
    public final static String TEMPLATE_SERVICE_TIER_SPRING = "Spring";

    public final static String TEMPLATE_APPLICATION_SERVER = "appserver";
    public final static String TEMPLATE_APPLICATION_SERVER_JBOSS_4_X = "JBoss 4.x";
    public final static String TEMPLATE_APPLICATION_SERVER_JBOSS_3_2_2_7 = "JBoss 3.2.2-7";
    public final static String TEMPLATE_APPLICATION_SERVER_JBOSS_3_2_0_1 = "JBoss 3.2.0-1";
    public final static String TEMPLATE_APPLICATION_SERVER_JBOSS_3_0 = "JBoss 3.0";
    public final static String TEMPLATE_APPLICATION_SERVER_TOMCAT_5 = "Tomcat 5";
    public final static String TEMPLATE_APPLICATION_SERVER_SUN_ONE_7 = "Sun ONE Application Server 7";
    public final static String TEMPLATE_APPLICATION_SERVER_WEBLOGIC_8_1 = "BEA WebLogic 8.1";
    public final static String TEMPLATE_APPLICATION_SERVER_WEBLOGIC_EJBGEN_8_1 = "BEA WebLogic 8.1 (Workshop EJBGen)";
    public final static String TEMPLATE_APPLICATION_SERVER_IBM_WEBSPERE = "IBM WebSphere";
    public final static String TEMPLATE_APPLICATION_SERVER_ORACLE = "Oracle Application Server";


    /**
     * Runs the JaGGenerator.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Initialize commons logging..
        log.info("Starting jag...");
        jagGenerator = new JagGenerator();
        jagGenerator.setVisible(true);
    }


   /**
    * Get the current logger.
    * @return the logger.
    */
    public ConsoleLogger getLogger() {
       return logger;
    }


   /**
    * Set the console logger.
    * @param logger
    */
    public void setLogger(ConsoleLogger logger) {
       this.logger = logger;
    }
    /**
     * Creates new form jagGenerator
     */
    public JagGenerator() {
        // Read user-prefs
        Settings.read();

        // Setup main Window
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        // Initialize the Netbeans form components.
        initComponents();
        // Initilize our custom components that aren't managed by the form editor.
        initCustomComponents();
        logger = new ConsoleLogger(console);
        consoleScrollPane.setViewportView(console);
       
        logger.log("Java Application Generator - console output:\n");
        initDesktop();
        fileNameLabel.setToolTipText("No application file selected.");
        root = new Root();
        treeModel = new DefaultTreeModel(root);
        treeModel.addTreeModelListener(new JagTreeModelListener());
        tree.setCellRenderer(new JagTreeCellRenderer());
        tree.setModel(treeModel);
        tree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) root.getFirstChild()).getPath()));

        // Set divider-locations to the place they were when user closed program
        splitPane.setDividerLocation(Settings.getVerticalDividerPosition());
        desktopConsoleSplitPane.setDividerLocation(Settings.getHorizontalDividerPosition());
        // Give the Window the last known position and size again.
        setBounds(Settings.getUserWindowBounds(this));
        // If user was previously working in a maximized Window, return to maximized state
        if (Settings.isMaximized()) {
            setExtendedState(MAXIMIZED_BOTH);
        }
    }


    /**
     * Gets the connection manager, the means by which the database is accessed.  If no connection has
     * yet been set up, a dialogue will be displayed to the user with the DB settings and following this,
     * a connection is attempted.
     *
     * @return Value of property conManager.
     */
    public static GenericJdbcManager getConManager() {
        if (conManager == null) {
            JDialog dialog = new ConnectDialog(jagGenerator);
            dialog.setVisible(true); //necessary as of kestrel
        }
        if (conManager != null) {
            jagGenerator.disconnectMenuItem.setEnabled(true);
        }
        return conManager;
    }

    /**
     * Checks whether the database has been connected yet.
     *
     * @return <code>true</code> if connected.
     */
    public static boolean isDatabaseConnected() {
        return conManager != null;
    }

    /**
     * Check if JAG is running in offline mode (no database connection).
     */
    public boolean isOfflineMode() {
        return offlineMode;
    }

    /**
     * Set JAG in offline mode. This allows for adding Entties without a databse connection.
     */
    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    /**
     * Enables the presentation layer to specify that a given field within a given entity is a foreign key field.
     *
     * @param tableName the name of the table whose entity contains the field we're interested in.
     * @param fieldName the foreign key field.
     */
    public static void setForeignKeyInField(String tableName, String fieldName) {
        TreeModel model = jagGenerator.tree.getModel();
        for (int i = 0; i < model.getChildCount(model.getRoot()); i++) {
            Object kid = model.getChild(model.getRoot(), i);
            if (kid instanceof Entity) {
                Entity entity = (Entity) kid;
                if (entity.getLocalTableName().equals(tableName)) {
                    for (int j = 0; j < entity.getChildCount(); j++) {
                        Object kid2 = entity.getChildAt(j);
                        if (kid2 instanceof Field) {
                            Field field = (Field) kid2;
                            if (field.getName().toString().equals(fieldName)) {
                                field.setForeignKey(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isRelationsEnabled() {
        return relationsEnabled;
    }

    public static void logToConsole(Object o) {
        logToConsole(o, LogLevel.INFO);
    }

    public static void stateChanged(boolean updateTree) {
        setFileNeedsSavingIndicator(true);
        if (updateTree) {
            jagGenerator.tree.updateUI();
        }
    }

    public static void finishedGeneration() {
        jagGenerator.executeButton.setIcon(RUN_ICON);
        jagGenerator.executeButton.setActionCommand(RUN_ACTION);
    }

    public static Template getTemplate() {
        Enumeration children = jagGenerator.root.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (child instanceof Config) {
                return ((Config) child).getTemplate();
            }
        }
        return null;
    }

    public static List getObjectsFromTree(Class clazz) {
        ArrayList list = new ArrayList();
        Enumeration children = jagGenerator.root.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (child != null && child.getClass().equals(clazz)) {
                list.add(child);
            }
        }
        return list;
    }

    public static void addEntity(String refName, Entity entity) {
        entities.put(refName, entity);
        entitiesByTableName.put(entity.getTableName(), entity);
    }

    public static Entity getEntityByRefName(String refName) {
        return (Entity) entities.get(refName);
    }

    public static Entity getEntityByTableName(String tableName) {
        return (Entity) entitiesByTableName.get(tableName);
    }

    /**
     * When the table name associated with an entity has been updated, calling this method
     * updates the cache.
     *
     * @param entityName
     * @param newTableName
     */
    public static void entityHasupdatedTableName(String entityName, String newTableName) {
        synchronized (entitiesByTableName) {
            Iterator i = entitiesByTableName.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                Entity entity = (Entity) entry.getValue();
                if (entity.getName().equals(entityName)) {
                    entitiesByTableName.remove(entry.getKey());
                    entitiesByTableName.put(newTableName, entity);
                }
            }
        }
    }

    /**
     * Makes sure that the SQL types of the fields within this application are compatible with the chosen DB.
     */
    public static void normaliseSQLTypesWithChosenDatabase() {
        //I don't think this functionality is really necessary.

//      if (jagGenerator.root.datasource.getMapping().toString().equals(Datasource.MYSQL)) {
//         replaceSQLTypeInAllFields("NUMBER(\\([0-9\\s]+,[0-9\\s]+\\))", "FLOAT$1"); //NUMBER with TWO comma-seperated args
//         replaceSQLTypeInAllFields("NUMBER", "INT"); //all other NUMBER
//         replaceSQLTypeInAllFields("VARCHAR2", "VARCHAR");
//      }
//
//      if (jagGenerator.root.datasource.getMapping().toString().equals(Datasource.ORACLE8)) {
//         replaceSQLTypeInAllFields("INT", "NUMBER");
//      }
    }

    /**
     * A record is kept of the last-accessed directory for every FileChooser, this method gets that record.
     *
     * @param filechooserKey A unique key.
     * @return File
     */
    public static File getFileChooserStartDir(String filechooserKey) {
        File dir = (File) FILECHOOSER_START_DIR.get(filechooserKey);
        return dir == null ? applicationFileDir : dir;
    }

    /**
     * A record is kept of the last-accessed directory for every FileChooser, this method sets that record.
     *
     * @param filechooserKey A unique key.
     * @param dir            The new directory.
     */
    public static void setFileChooserStartDir(String filechooserKey, File dir) {
        FILECHOOSER_START_DIR.put(filechooserKey, dir);
    }

    private static void saveGuiSettings() {
        // Store divier locations
        Settings.setVerticalDividerPosition(jagGenerator.splitPane.getDividerLocation());
        Settings.setHorizontalDividerPosition(jagGenerator.desktopConsoleSplitPane.getDividerLocation());
        // Store window's maximized state and/or it's size and location
        int extendedState = jagGenerator.getExtendedState();
        boolean isMaximized = ((extendedState & MAXIMIZED_BOTH) == MAXIMIZED_BOTH);
        if (!isMaximized) {
            // The current Window-dimensions are only valid if not maximized
            Settings.setUserWindowBounds(jagGenerator.getBounds());
        }
        Settings.setMaximized(isMaximized);
    }

    /**
     * Causes JAG to die.
     *
     * @param error if not <code>null</code>, forces an error dialogue before death.
     */
    public static void kickTheBucket(String error) {
        if (error == null) {
            // Store GUI-settings in user-preferences
            saveGuiSettings();
            Settings.write();
            // Make sure the current database-settings are stored as well
            ConfigManager.getInstance().save();
            //todo: prompt to save application file!
            //todo: Make sure prompt allows you to check "don't show this again"
            System.exit(0);
        } else {
            //something went horribly wrong..
            JOptionPane.showMessageDialog(jagGenerator,
                    error, "JAG - Fatal error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Setter for property conManager.
     *
     * @param conManager New value of property conManager.
     */
    public void setConManager(GenericJdbcManager conManager) {
        JagGenerator.conManager = conManager;
    }


    private void initDesktop() {
        desktopPane.setLayout(new GridBagLayout());
        this.setTitle("Java Application Generator - Finalist IT Group");
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        desktopPane.add(splitPane, gridBagConstraints);
        pack();
    }

    private void newRelationMenuItemActionPerformed() {
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
        if (!(selected instanceof Entity)) {
            //see if the parent is an Entity..
            TreePath selectedPath = tree.getSelectionPath();
            selected = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();
            if (!(selected instanceof Entity)) {
                JOptionPane.showMessageDialog(this,
                        "A relation can only be added to an entity bean.  Please first select an entity in the application tree.", "Can't add relation!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Entity selectedEntity = (Entity) selected;

        DefaultMutableTreeNode newNode = new Relation(selectedEntity);
        selectedEntity.addRelation((Relation) newNode);
        tree.setSelectionPath(new TreePath(newNode.getPath()));
        tree.updateUI();
    }


    /**
     * Updates objects concerned with 'local' side CMR relations, namely:
     * 1: for every foreign key Field object within an entity that takes part in a relation, set its Relation object.
     * 2: for every Relation object, set the Relation's local-side foreign key Field object.
     */
    private void updateLocalSideRelations() {
        Iterator entities = root.getEntityEjbs().iterator();
        while (entities.hasNext()) {
            Entity entity = (Entity) entities.next();
            //for every entity..
            for (int i = 0; i < entity.getChildCount(); i++) {
                TreeNode child = entity.getChildAt(i);
                //for every relation in that entity..
                if (child instanceof Relation) {
                    Relation relation = (Relation) child;
                    String fkFieldName = relation.getFieldName().toString();
                    for (int j = 0; j < entity.getChildCount(); j++) {
                        TreeNode child2 = entity.getChildAt(j);
                        //iterate through all this entity's Fields..
                        if (child2 instanceof Field) {
                            Field field = (Field) child2;
                            //until we find the Field that matches the relation's fkFieldName
                            if (field.getName().equals(fkFieldName)) {
                                field.setRelation(relation);
                                field.setForeignKey(true);
                                relation.setFieldName(field.getName().toString());
                                relation.setFkField(field);
                                if (relation.getLocalColumn() == null) {
                                    relation.setLocalColumn(relation.getFkField().getColumnName());
                                }
                                logToConsole("relation " + relation + ": local-side fk field is " + entity.getName() + ":" + field);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates objects concerned with the 'foreign' side of CMR relations, namely:
     * 1: for every Relation, set the foreign-side primary key Field object (the primary key of the entity on the
     * foreign side of the relation).
     */
    private void updateForeignSideRelations() {
        Iterator entities = root.getEntityEjbs().iterator();
        while (entities.hasNext()) {
            Entity entity = (Entity) entities.next();
            for (int i = 0; i < entity.getChildCount(); i++) {
                TreeNode child = entity.getChildAt(i);
                if (child instanceof Relation) {
                    Relation relation = (Relation) child;
                    Entity relatedEntity = relation.getRelatedEntity();
                    String column = relation.getForeignColumn();
                    for (int j = 0; j < relatedEntity.getChildCount(); j++) {
                        TreeNode child2 = relatedEntity.getChildAt(j);
                        if (child2 instanceof Field) {
                            Field field = (Field) child2;
                            if (field.getColumnName().equals(column)) {
                                relation.setForeignPkField(field);
                                logToConsole("relation " + relation + ": foreign-side pk is " + relatedEntity + ":" + field);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Sometimes a relation specifies an entity that hasn't been added to a session bean (all related entities
     * must be represented in the session fa?ade).  This method adds those entities automatically.
     *
     * @return <code>false</code> if a related entity couldn't be imported into the application.
     */
    private boolean addRelatedEntitiesToSessionBeans() {
        boolean somethingAdded = false;
        //1: create a map of entity name --> set of related foreign tables, and
        //            map of local table --> entity bean name, and
        //            map of entity bean name --> local table
        HashMap relatedTablesPerEB = new HashMap();
        HashMap entityPerTable = new HashMap();
        HashMap tablePerEntity = new HashMap();
        Iterator entities = root.getEntityEjbs().iterator();
        while (entities.hasNext()) {
            Entity entity = (Entity) entities.next();
            entityPerTable.put(entity.getLocalTableName().toString(), entity.getRefName());
            tablePerEntity.put(entity.getRefName(), entity.getLocalTableName().toString());
            for (int i = 0; i < entity.getChildCount(); i++) {
                TreeNode child = entity.getChildAt(i);
                if (child instanceof Relation) {
                    Relation relation = (Relation) child;
                    String relatedTableName = relation.getForeignTable();
                    Set existing = (Set) relatedTablesPerEB.get(entity.getRefName());
                    if (existing == null) {
                        existing = new HashSet();
                    }
                    existing.add(relatedTableName);
                    relatedTablesPerEB.put(entity.getRefName(), existing);

                }
            }
        }

        //2: remove all related foreign tables from the map, where the table is the local table of an entity that already
        //   appears within a session bean.
        Iterator sessions = root.getSessionEjbs().iterator();
        while (sessions.hasNext()) {
            Session session = (Session) sessions.next();
            Iterator entitiesWithinSession = session.getEntityRefs().iterator();
            while (entitiesWithinSession.hasNext()) {
                String localTable = (String) tablePerEntity.get(entitiesWithinSession.next());
                Iterator relatedEntitySets = relatedTablesPerEB.values().iterator();
                while (relatedEntitySets.hasNext()) {
                    Set set = (Set) relatedEntitySets.next();
                    set.remove(localTable);
                }
            }
        }

        //3: for each session bean, add any related entities not already contained within a session bean.
        HashSet addedTables = new HashSet();
        sessions = root.getSessionEjbs().iterator();
        while (sessions.hasNext()) {
            Session session = (Session) sessions.next();
            Iterator entitiesWithinSession = session.getEntityRefs().iterator();
            while (entitiesWithinSession.hasNext()) {
                String entityName = (String) entitiesWithinSession.next();
                Set tablesToBeAdded = (Set) relatedTablesPerEB.get(entityName);
                if (tablesToBeAdded != null) {
                    Iterator i = tablesToBeAdded.iterator();
                    while (i.hasNext()) {
                        String table = (String) i.next();
                        if (!addedTables.contains(table)) {
                            String entity = (String) entityPerTable.get(table);
                            if (entity == null) {
                                JOptionPane.showMessageDialog(this,
                                        "Entity '" + entityName + "' contains a relation to a table '" + table + "'\n" +
                                                "for which no entity bean exists in the current application.\n" +
                                                "Please either create a new entity bean for this table, or delete the relation.",
                                        "Invalid Container-managed relation!",
                                        JOptionPane.ERROR_MESSAGE);
                                return false;
                            } else {
                                session.addRelationRef(entity);
                                addedTables.add(table);
                                somethingAdded = true;
                                JOptionPane.showMessageDialog(this,
                                        "Entity '" + entityName + "' added to the service bean '" + session.getRefName() +
                                                "' contains a relation to the entity '" + entity + "', which doesn't appear in any service beans.\n" +
                                                "The relation requires accessor methods to '" + entity + "', so these were automatically added to the service bean.",
                                        "Service bean modified",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            }
        }

        //recursively call this method if an entity had to be added, because possible that entity also contains
        //relation references to other entities that need to be imported into a session bean...
        if (somethingAdded) {
            addRelatedEntitiesToSessionBeans();
        }
        return true;
    }


    private File selectJagOutDirectory(String startDir) {
        File directory = null;
        int fileChooserStatus;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an ouput directory for the generated application..");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(startDir));
        fileChooserStatus = fileChooser.showOpenDialog(this);
        if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getSelectedFile();
            String projectName = root.app.nameText.getText();
            directory = new File(directory.getAbsoluteFile(), projectName);
        }
        return directory;

    }

    /**
     * Generates relations for a specified entity by reading foreign key info from the database.
     *
     * @param entity the Entity whose relations will be generated.
     */
    private boolean generateRelationsFromDB(Entity entity) {
        if (getConManager() == null) {
            logger.log("Can't regenerate relations - no database!");
            return false;
        }
        log.debug("Get the foreign keys for table: " + entity.getTableName());
        List fkeys = DatabaseUtils.getForeignKeys(entity.getTableName());
        HashMap foreignTablesTally = new HashMap();
        Set foreignKeyFieldNames = new HashSet(fkeys.size());
        Iterator i = fkeys.iterator();
        while (i.hasNext()) {
            ForeignKey fk = (ForeignKey) i.next();
            foreignKeyFieldNames.add(Utils.format(fk.getFkColumnName()));
            Relation relation = new Relation(entity, fk);
            String foreignTable = fk.getPkTableName();

            // Default to unidirectional:
            relation.setBidirectional(false);
            // No support for one-to-one relations yet. So default to many-to-one
            relation.setTargetMultiple(true);
            /*
            if (relation.getFkField() != null && relation.getFkField().isPrimaryKey()) {
               // In case the local side is a primary key, we have a one to one relation
               relation.setTargetMultiple(false);
            } else {
               // In case the local side isn't a primary key, we asume to have a many to one relation.
               relation.setTargetMultiple(true);
            }
            */

            if (foreignTablesTally.keySet().contains(foreignTable)) {
                int tally = ((Integer) foreignTablesTally.get(foreignTable)).intValue() + 1;
                relation.setName(relation.getName() + tally);
                foreignTablesTally.put(foreignTable, new Integer(tally));
            } else {
                foreignTablesTally.put(foreignTable, new Integer(1));
            }
            addObject(entity, relation, false, true);
        }
        return true;
    }

    private ArrayList sortColumns(ArrayList columns, ArrayList pKeys, Entity entity, String pKey) {
        ArrayList sortedColumns = new ArrayList();
        // Make sure the primary key will be the first field!
        ArrayList primaryKeyColumns = new ArrayList();
        Column primaryKeyColumn = null;
        for (Iterator colIt = columns.iterator(); colIt.hasNext();) {
            Column column = (Column) colIt.next();
            if (pKeys.contains(column.getName())) {
                // We found the primary key column!
                primaryKeyColumn = column;
                primaryKeyColumn.setPrimaryKey(true);
                primaryKeyColumns.add(primaryKeyColumn);
            } else {
                column.setPrimaryKey(false);
                sortedColumns.add(column);
            }
        }
        if (pKeys.size() > 1) {
            // We have a composite primary key!
            entity.isCompositeCombo.setSelectedItem("true");
            String compositePK = entity.rootPackageText.getText() + "." + entity.nameText.getText() + "PK";
            entity.pKeyTypeText.setText(compositePK);
            entity.pKeyText.setText("");// name is irrelevant now.
        } else {
            entity.isCompositeCombo.setSelectedItem("false");
            if (pKeys.size() == 1) {
                entity.pKeyText.setText(Utils.format(pKey));// name is irrelevant now.

            }
        }
        // If a primary key column was found, we put it in front of the list.
        columns = new ArrayList();
        if (primaryKeyColumn != null)
            columns.addAll(primaryKeyColumns);
        columns.addAll(sortedColumns);
        return columns;
    }

    private void addObject(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, boolean forceUpdate, boolean topOfQueue) {
        if (parent == null) {
            parent = root;
        }
        treeModel.insertNodeInto(child, parent, topOfQueue ? 0 : parent.getChildCount());
        if (forceUpdate) {
            tree.setSelectionPath(new TreePath(child.getPath()));
        }
    }

    public boolean save() {
        //the object graph is incomplete at this stage, so finalise it now:
        updateLocalSideRelations();
        updateForeignSideRelations();

        if (!addRelatedEntitiesToSessionBeans()) {
            return false;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element skelet = doc.createElement("skelet");
            doc.appendChild(skelet);
            root.getXML(skelet);
            String XMLDoc = outXML(doc);
            String fileName = file.getName();
            if (fileName != null) {
                if (fileName.indexOf(".xml") == -1) {
                    file = new File(file.getAbsolutePath() + ".xml");
                }
            }
            FileWriter fw = new FileWriter(file);
            fw.write(XMLDoc);
            fw.close();
            fileNameLabel.setText("Application file: " + file.getName());
            fileNameLabel.setToolTipText(file.getAbsolutePath());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFileNeedsSavingIndicator(false);
        return true;
    }

    /* Use the JAXB JDK1.4 parser to serialize to XML. */
    public static String outXML(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter sw = new StringWriter();
            StreamResult streamResult = new StreamResult(sw);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.transform(domSource, streamResult);
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setFileNeedsSavingIndicator(boolean sterretje) {
        if (jagGenerator != null && jagGenerator.file != null) {
            String filename = jagGenerator.fileNameLabel.getText();
            if (sterretje && filename.charAt(filename.length() - 1) != '*') {
                jagGenerator.fileNameLabel.setText(filename + '*');
            } else if (!sterretje && filename.charAt(filename.length() - 1) == '*') {
                jagGenerator.fileNameLabel.setText(filename.substring(0, filename.length() - 1));
            }
        }
    }

    private void initCustomComponents() {
        // Since a custom menu can't be added using Netbeans form editor,
        // We add it just after the initComponents.
        recentMenu = new com.finalist.jaggenerator.menu.RecentMenu();
        recentMenu.setMnemonic(KeyEvent.VK_R);
        recentMenu.setText("Recent...");
        recentMenu.setMainApp(this);
        fileMenu.insert(recentMenu, 2);
    }



//###########################################################################################
// All method signatures and "// doc" below here are automatically created
// by NetBeans (we used version 5.0).  Only change the method BODIES please,
// as this will enable later re-use the NetBeans form editor to make changes in the GUI!
//###########################################################################################


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        splitPane = new javax.swing.JSplitPane();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        toolBar = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        entityButton = new javax.swing.JButton();
        sessionButton = new javax.swing.JButton();
        relationButton = new javax.swing.JButton();
        businessMethodButton = new javax.swing.JButton();
        fieldButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        executeButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        spacer = new javax.swing.JPanel();
        applicationFileInfoPanel = new javax.swing.JPanel();
        fileNameLabel = new javax.swing.JLabel();
        databaseConnectionInfoPanel = new javax.swing.JPanel();
        databaseConnectionLabel = new javax.swing.JLabel();
        desktopConsoleSplitPane = new javax.swing.JSplitPane();
        desktopPane = new javax.swing.JDesktopPane();
        consoleScrollPane = new javax.swing.JScrollPane();
        console = new javax.swing.JTextPane();
        textConsole = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        importMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        generateJavaApplicationAsMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        addSubMenu = new javax.swing.JMenu();
        addEntityMenuItem = new javax.swing.JMenuItem();
        addSessionMenuItem = new javax.swing.JMenuItem();
        addRelationMenuItem = new javax.swing.JMenuItem();
        addBusinessMenuItem = new javax.swing.JMenuItem();
        addFieldMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        connectionMenu = new javax.swing.JMenu();
        driverManagerMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        connectMenuItem = new javax.swing.JMenuItem();
        disconnectMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        contentMenuItem = new javax.swing.JMenuItem();

        splitPane.setBorder(null);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(3);
        splitPane.setContinuousLayout(true);
        splitPane.setOpaque(false);
        splitPane.setVerifyInputWhenFocusTarget(false);
        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeValueChanged(evt);
            }
        });

        treeScrollPane.setViewportView(tree);

        splitPane.setLeftComponent(treeScrollPane);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        toolBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        toolBar.setFloatable(false);
        toolBar.setName("toolBar");
        newButton.setIcon(new javax.swing.ImageIcon("../images/new.png"));
        newButton.setText(" ");
        newButton.setToolTipText("New JAG application");
        newButton.setBorder(null);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(newButton);

        openButton.setIcon(new javax.swing.ImageIcon("../images/open.png"));
        openButton.setText(" ");
        openButton.setToolTipText("Open a JAG application file");
        openButton.setBorder(null);
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(openButton);

        saveButton.setIcon(new javax.swing.ImageIcon("../images/save.png"));
        saveButton.setText(" ");
        saveButton.setToolTipText("Save");
        saveButton.setBorder(null);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        toolBar.add(saveButton);

        toolBar.addSeparator();
        entityButton.setIcon(new javax.swing.ImageIcon("../images/entity.png"));
        entityButton.setText(" ");
        entityButton.setToolTipText("New entity bean");
        entityButton.setBorder(null);
        entityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntityMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(entityButton);

        sessionButton.setIcon(new javax.swing.ImageIcon("../images/session.png"));
        sessionButton.setText(" ");
        sessionButton.setToolTipText("New service bean");
        sessionButton.setBorder(null);
        sessionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSessionMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(sessionButton);

        relationButton.setIcon(new javax.swing.ImageIcon("../images/relation.png"));
        relationButton.setText(" ");
        relationButton.setToolTipText("New relation");
        relationButton.setBorder(null);
        relationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRelationPopupMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(relationButton);

        businessMethodButton.setIcon(new javax.swing.ImageIcon("../images/business.png"));
        businessMethodButton.setText(" ");
        businessMethodButton.setToolTipText("New business method");
        businessMethodButton.setBorder(null);
        businessMethodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                businessMethodButtonActionPerformed(evt);
            }
        });

        toolBar.add(businessMethodButton);

        fieldButton.setIcon(new javax.swing.ImageIcon("../images/field.png"));
        fieldButton.setText(" ");
        fieldButton.setToolTipText("New entity field");
        fieldButton.setBorder(null);
        fieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldButtonActionPerformed(evt);
            }
        });

        toolBar.add(fieldButton);

        deleteButton.setIcon(new javax.swing.ImageIcon("../images/delete.png"));
        deleteButton.setText(" ");
        deleteButton.setToolTipText("Delete");
        deleteButton.setBorder(null);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(deleteButton);

        toolBar.addSeparator();
        executeButton.setIcon(new javax.swing.ImageIcon("../images/execute.png"));
        executeButton.setText(" ");
        executeButton.setToolTipText("Generate the application");
        executeButton.setBorder(null);
        executeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateJavaApplicationAsMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(executeButton);

        toolBar.addSeparator();
        helpButton.setIcon(new javax.swing.ImageIcon("../images/help.png"));
        helpButton.setText(" ");
        helpButton.setToolTipText("Help");
        helpButton.setBorder(null);
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentMenuItemActionPerformed(evt);
            }
        });

        toolBar.add(helpButton);

        spacer.setLayout(null);

        toolBar.add(spacer);

        applicationFileInfoPanel.setName("fileStatusComponent");
        fileNameLabel.setText("Application file:");
        fileNameLabel.setToolTipText("File name of the XML skelet");
        fileNameLabel.setName("fileNameLabel");
        applicationFileInfoPanel.add(fileNameLabel);

        toolBar.add(applicationFileInfoPanel);

        databaseConnectionInfoPanel.setName("databaseConnectionComponent");
        databaseConnectionLabel.setText("Database Connection:");
        databaseConnectionLabel.setName("databaseConnectionLabel");
        databaseConnectionInfoPanel.add(databaseConnectionLabel);

        toolBar.add(databaseConnectionInfoPanel);

        getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);

        desktopConsoleSplitPane.setDividerLocation(590);
        desktopConsoleSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        desktopConsoleSplitPane.setTopComponent(desktopPane);

        console.setBackground(new java.awt.Color(204, 204, 204));
        console.setEditable(false);
        console.setFont(new java.awt.Font("Lucida Console", 0, 10));
        console.setForeground(new java.awt.Color(0, 0, 1));
        consoleScrollPane.setViewportView(console);

        textConsole.setColumns(20);
        textConsole.setRows(5);
        consoleScrollPane.setViewportView(textConsole);

        desktopConsoleSplitPane.setBottomComponent(consoleScrollPane);

        getContentPane().add(desktopConsoleSplitPane, java.awt.BorderLayout.CENTER);

        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setText("File");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(newMenuItem);

        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        importMenuItem.setMnemonic(KeyEvent.VK_I);
        importMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK));
        importMenuItem.setText("Import UML model..");
        importMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(importMenuItem);

        fileMenu.add(jSeparator1);

        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveAsMenuItem);

        exportMenuItem.setMnemonic(KeyEvent.VK_E);
        exportMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
        exportMenuItem.setText("Export UML model..");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exportMenuItem);

        fileMenu.add(jSeparator2);

        generateJavaApplicationAsMenuItem.setMnemonic(KeyEvent.VK_G);
        generateJavaApplicationAsMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        generateJavaApplicationAsMenuItem.setText("Generate application...");
        generateJavaApplicationAsMenuItem.setToolTipText("Generate a J2EE Applicatin");
        generateJavaApplicationAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateJavaApplicationAsMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(generateJavaApplicationAsMenuItem);

        fileMenu.add(jSeparator3);

        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.setText("Edit");
        editMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuActionPerformed(evt);
            }
        });

        addSubMenu.setText("Add");
        addEntityMenuItem.setMnemonic(KeyEvent.VK_1);
        addEntityMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_MASK));
        addEntityMenuItem.setText("entity bean");
        addEntityMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntityMenuItemActionPerformed(evt);
            }
        });

        addSubMenu.add(addEntityMenuItem);

        addSessionMenuItem.setMnemonic(KeyEvent.VK_2);
        addSessionMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_MASK));
        addSessionMenuItem.setText("service bean");
        addSessionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSessionMenuItemActionPerformed(evt);
            }
        });

        addSubMenu.add(addSessionMenuItem);

        addRelationMenuItem.setMnemonic(KeyEvent.VK_3);
        addRelationMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_MASK));
        addRelationMenuItem.setText("relation");
        addRelationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRelationPopupMenuItemActionPerformed(evt);
            }
        });

        addSubMenu.add(addRelationMenuItem);

        addBusinessMenuItem.setMnemonic(KeyEvent.VK_4);
        addBusinessMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_MASK));
        addBusinessMenuItem.setText("business");
        addBusinessMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBusinessMenuItemActionPerformed(evt);
            }
        });

        addSubMenu.add(addBusinessMenuItem);

        addFieldMenuItem.setMnemonic(KeyEvent.VK_5);
        addFieldMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.CTRL_MASK));
        addFieldMenuItem.setText("field");
        addFieldMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFieldMenuItemActionPerformed(evt);
            }
        });

        addSubMenu.add(addFieldMenuItem);

        editMenu.add(addSubMenu);

        deleteMenuItem.setMnemonic(KeyEvent.VK_D);
        deleteMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_MASK));
        deleteMenuItem.setText(" Delete");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });

        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        connectionMenu.setMnemonic(KeyEvent.VK_D);
        connectionMenu.setText("Database");
        driverManagerMenuItem.setText("Driver Manager..");
        driverManagerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverManagerMenuItemActionPerformed(evt);
            }
        });

        connectionMenu.add(driverManagerMenuItem);

        connectionMenu.add(jSeparator4);

        connectMenuItem.setText("Create connection...");
        connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectMenuItemActionPerformed(evt);
            }
        });

        connectionMenu.add(connectMenuItem);

        disconnectMenuItem.setText("Disconnect");
        disconnectMenuItem.setEnabled(false);
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectMenuItemActionPerformed(evt);
            }
        });

        connectionMenu.add(disconnectMenuItem);

        menuBar.add(connectionMenu);

        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.setText("Help");
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        contentMenuItem.setMnemonic(KeyEvent.VK_C);
        contentMenuItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        contentMenuItem.setText("Content");
        contentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(contentMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addFieldMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFieldMenuItemActionPerformed
// TODO add your handling code here:
        fieldButtonActionPerformed(evt);
    }//GEN-LAST:event_addFieldMenuItemActionPerformed

    private void fieldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldButtonActionPerformed
// TODO add your handling code here:
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (!(selected instanceof Entity)) {
            JOptionPane.showMessageDialog(this,
                    "A field can only be added to an entity.  Please first select an entity in the application tree.", "Can't add field!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Entity selectedEntity = (Entity) selected;
        Field newField = new Field(selectedEntity, new Column());
        selectedEntity.add(newField);
        tree.setSelectionPath(new TreePath(newField.getPath()));
        tree.updateUI();
    }//GEN-LAST:event_fieldButtonActionPerformed

    private void addBusinessMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBusinessMenuItemActionPerformed
        // TODO add your handling code here:
        businessMethodButtonActionPerformed(evt);
    }//GEN-LAST:event_addBusinessMenuItemActionPerformed

    private void editMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editMenuActionPerformed

    private void businessMethodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_businessMethodButtonActionPerformed
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (!(selected instanceof Session)) {
            //see if the parent is an Entity..
            TreePath selectedPath = tree.getSelectionPath();
            selected = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();
            if (!(selected instanceof Session)) {
                JOptionPane.showMessageDialog(this,
                        "A business method can only be added to a service bean.  Please first select a session in the application tree.", "Can't add business method!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Session selectedSession = (Session) selected;

        BusinessMethod newBusinessMethod = new BusinessMethod(selectedSession);
        selectedSession.add(newBusinessMethod);
        tree.setSelectionPath(new TreePath(newBusinessMethod.getPath()));
        tree.updateUI();
    }//GEN-LAST:event_businessMethodButtonActionPerformed

    private void driverManagerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverManagerMenuItemActionPerformed
        DatabaseManagerFrame.getInstance().show();
    }//GEN-LAST:event_driverManagerMenuItemActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        if (file == null) {
            logger.log("UML Export: save application file first!");
            String message = "Before exporting the current application to UML, please save it to an application file.";
            JOptionPane.showMessageDialog(this, message, "No application file!", JOptionPane.ERROR_MESSAGE);
            saveButtonActionPerformed(null);
            if (file == null) {
                logger.log("Aborted UML Export!");
                return;
            }
        } else {
            saveButtonActionPerformed(null);
        }

        int fileChooserStatus;
        logToConsole("Exporting application to an XMI file.  Please wait...");
        final JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_UMLEXPORT));
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        String[] extenstions = {"xmi", "xml"};
        ExtensionsFileFilter filter = new ExtensionsFileFilter(extenstions);
        fileChooser.setFileFilter(filter);

        fileChooser.setDialogTitle("UML Export: Choose a destination XMI file..");
        fileChooserStatus = fileChooser.showSaveDialog(this);
        if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
            File xmiFile = fileChooser.getSelectedFile();
            if (!xmiFile.getAbsolutePath().endsWith(XMI_SUFFIX)) {
                xmiFile = new File(xmiFile.getAbsolutePath() + XMI_SUFFIX);
            }
            //run the export tool
            new Jag2UMLGenerator(logger).generateXMI(file.getAbsolutePath(), xmiFile);
            logToConsole("...UML export complete.");
            setFileChooserStartDir(FILECHOOSER_UMLEXPORT, xmiFile);

        } else {
            logToConsole("...aborted!");
        }
    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMenuItemActionPerformed
        int fileChooserStatus;
        logToConsole("Importing UML model from XMI file.  Please wait...");
        final JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_UMLIMPORT));
        String[] extenstions = {"xmi", "xml"};
        ExtensionsFileFilter filter = new ExtensionsFileFilter(extenstions);
        fileChooser.setDialogTitle("UML Import: Choose an XMI file..");
        fileChooser.setFileFilter(filter);
        fileChooserStatus = fileChooser.showOpenDialog(this);
        if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
            String xmiFile = fileChooser.getSelectedFile().getAbsolutePath();
            String outputDir = ".";
            //run the import tool - creates an XML application file in the output directory
            File xmi = new UML2JagGenerator(logger).generateXML(xmiFile, outputDir);
            log.info("Generated the jag project file from the UML Model. Now load the file to JAG.");
            loadApplicationFile(xmi);
            log.info("JAG project file was loaded.");
            xmi.delete(); // delete the generated XML file: give the user the choice of where to store it later.
            logToConsole("...UML import complete.");
            setFileChooserStartDir(FILECHOOSER_UMLIMPORT, fileChooser.getSelectedFile());


        } else {
            logToConsole("...aborted!");
        }

    }//GEN-LAST:event_importMenuItemActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (file == null) {
            saveAsMenuItemActionPerformed(evt);
        } else {
            save();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectMenuItemActionPerformed
        conManager = null;
        databaseConnectionLabel.setText("Database Connection: not connected");
        disconnectMenuItem.setEnabled(false);
        DatabaseUtils.clearCache();
    }//GEN-LAST:event_disconnectMenuItemActionPerformed

    private void addEntityMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntityMenuItemActionPerformed
       if (!isOfflineMode()) {
         getConManager();
       }
       if (isOfflineMode()) {
          Entity entity = new Entity(root.getRootPackage(), "entity", null);
          DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeModel.getRoot();
          addObject(parent, entity, true, false);
          tree.updateUI();
          return;
       }
        if (conManager == null) {
            logger.log("Can't add entity - no database connection!");
            return;
        }
        new SelectTablesDialog(this).show();

        new Thread(new Runnable() {
            public void run() {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeModel.getRoot();
                Object referencingModule = tree.getLastSelectedPathComponent();
                String templateValue = (String) root.config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS);
                if ("true".equalsIgnoreCase(templateValue)) {
                    relationsEnabled = true;
                } else if ("false".equalsIgnoreCase(templateValue)) {
                    relationsEnabled = false;
                } else {
                    relationsEnabled = false;
                }
                ArrayList createdEntities = new ArrayList();
                for (Iterator tabIt = SelectTablesDialog.getTablelist().iterator(); tabIt.hasNext();) {
                    String table = (String) tabIt.next();
                    logger.log("Creating entity for table '" + table + "'...");
                    ArrayList pKeys = DatabaseUtils.getPrimaryKeys(table);
                    String pKey = "";
                    if (pKeys.size() == 1) {
                        pKey = (String) pKeys.get(0);
                    } else if (pKeys.size() > 1) {
                        String tableClassName = Utils.toClassName(table);
                        pKey = root.getRootPackage() + ".entity" + tableClassName + "PK";
                    }

                    Entity entity = new Entity(root.getRootPackage(), table, pKey);
                    entity.setTableName(table);
                    addObject(parent, entity, true, false);
                    if (referencingModule instanceof Session) {
                        Session session = (Session) referencingModule;
                        session.addRef(entity.getRefName());
                    }

                    ArrayList columns = sortColumns(DatabaseUtils.getColumns(table), pKeys, entity, pKey);
                    if (relationsEnabled) {
                        generateRelationsFromDB(entity);
                    }

                    // Now build the fields.
                    for (Iterator colIt = columns.iterator(); colIt.hasNext();) {
                        Column column = (Column) colIt.next();
                        Field field = new Field(entity, column);
                        addObject(entity, field, false, false);
                        if (column.getName().equalsIgnoreCase(pKey)) {
                            entity.setPKeyType(field.getType(column));
                        }
                    }
                    createdEntities.add(entity);
                }
                if (relationsEnabled) {
                    checkForAssociationEntities(createdEntities);
                }
                // This will make sure the relations are updated correctly in the gui.
                for (Iterator iterator = createdEntities.iterator(); iterator.hasNext();) {
                    Entity entity = (Entity) iterator.next();
                    entity.notifyRelationsThatConstructionIsFinished();
                }
                logger.log("...finished!");
                tree.updateUI();
            }

        }).start();
    }//GEN-LAST:event_addEntityMenuItemActionPerformed


    /**
     * Check all entities and determine if there is a possible many-to-many relation
     * This is the case if an entity has EXACLTY 2 many-to-one relations.
     * The entity will be marked as "Association entity" and the related entities,
     * that were marked as one-to-many, will be marked as many-to-many.
     */
    private void checkForAssociationEntities(ArrayList createdEntities) {
        for (Iterator iterator = createdEntities.iterator(); iterator.hasNext();) {
            Entity entity = (Entity) iterator.next();
            if (entity.getRelations() != null && entity.getRelations().size() == 2 && entity.getFields().size() == 2) {
                // It's an entity with exaclty 2 foreign keys (targetMultiple is false).
                if (((Relation) entity.getRelations().get(0)).isTargetMultiple() &&
                        ((Relation) entity.getRelations().get(1)).isTargetMultiple()
                        ) {
                    // Mark the entity as an assocation entity.
                    entity.setIsAssociationEntity("true");
                }
            }
        }
    }

    private void addRelationPopupMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_addRelationPopupMenuItemActionPerformed
        newRelationMenuItemActionPerformed();
    }//GEN-LAST:event_addRelationPopupMenuItemActionPerformed

    private void contentMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_contentMenuItemActionPerformed
        URL helpURL = null;
        String s = null;
        try {
            s = "file:"
                    + System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "../doc/help/help.html";
            helpURL = new URL(s);
        } catch (IOException e) {
            JagGenerator.logToConsole("Missing help file: " + s, LogLevel.ERROR);
        }
        new HtmlContentPopUp(null, "JAG Help", false, helpURL).show();
    }//GEN-LAST:event_contentMenuItemActionPerformed

    private void aboutMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        URL helpURL = null;
        String s = null;
        try {
            s = "file:"
                    + System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "../doc/help/about.html";
            helpURL = new URL(s);
        } catch (IOException e) {
            JagGenerator.logToConsole("Missing help file: " + s, LogLevel.ERROR);
        }
        new HtmlContentPopUp(null, "JAG About", false, helpURL).show();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void generateJavaApplicationAsMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_generateJavaApplicationAsMenuItemActionPerformed
        if (evt.getActionCommand() == STOP_ACTION) {
            runningThread.interrupt();
            return;
        }

        if (file == null) {
            logger.log("No file specified! Save file first.");
            String message = "No application file (XML skelet) has been selected.\n" +
                    "Please save the current application to a file or open an existing application file.";
            JOptionPane.showMessageDialog(this, message, "No application file!", JOptionPane.ERROR_MESSAGE);
        } else {
            SkeletValidator validator = new SkeletValidator(root, tree, entitiesByTableName, logger);
            String message = validator.validateSkelet();
            if (message != null) {
                logger.log("Not a valid application file!");
                message += "\r\nSelect 'Yes' if you want to generate anyway. This will very probably lead to incorrect code!";
                //JOptionPane.showMessageDialog(this, message, "Invalid configuration", JOptionPane.YES_NO_OPTION);
                int rc = JOptionPane.showConfirmDialog(this, message, "Invalid configuration", JOptionPane.YES_NO_OPTION);
                if (rc != 0) {
                    // 0 is the yes option, which means we want to generate anyway.
                    return;
                }
                logger.log("Warning! Code is generated in spite of an invalid project file!");
            }
            // Make sure the lates skelet has been saved:
            if (!save()) {
                logger.log("Can't generate application - Invalid relation(s).");
                return;
            }

            String outDir = Settings.getLastSelectedOutputDir();
            // Now select an output directory for the generated java application.

            if (outputDir != null) {
                outDir = outputDir.getParentFile().getAbsolutePath();
            }
            outputDir = selectJagOutDirectory(outDir);
            if (outputDir == null) {
                return;
            }

            Settings.setLastSelectedOutputDir(outputDir.getParentFile().getAbsolutePath());

            final String[] args = new String[3];
            args[0] = outputDir.getAbsolutePath();
            args[1] = file.getAbsolutePath();
            runningThread = new Thread() {
                public void run() {

                    logger.log("Running jag in the " + args[0] + " directory for application file: " + args[1]);
                    JApplicationGen.setLogger(logger);
                    JApplicationGen.main(args);
                }
            };
            runningThread.start();
            executeButton.setIcon(CANCEL_ICON);
            executeButton.setActionCommand(STOP_ACTION);
        }
        setFileNeedsSavingIndicator(false);
    }//GEN-LAST:event_generateJavaApplicationAsMenuItemActionPerformed

    private void deleteMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        TreePath[] sel = tree.getSelectionPaths();
        for (int i = 0; i < sel.length; i++) {
            Object selectedObject = sel[i].getLastPathComponent();
            if (!(selectedObject instanceof Config ||
                    selectedObject instanceof App ||
                    selectedObject instanceof Paths ||
                    selectedObject instanceof Datasource)) {

                treeModel.removeNodeFromParent((DefaultMutableTreeNode) selectedObject);
            }
            if (selectedObject instanceof Entity) {
                TemplateString table = ((Entity) selectedObject).getLocalTableName();
                SelectTablesDialog.getAlreadyselected().remove(table);
                DatabaseUtils.clearColumnsCacheForTable(table.toString());
            }
        }
        setFileNeedsSavingIndicator(true);
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void newMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        root = new Root();
        file = null;
        fileNameLabel.setText("Application file:");
        fileNameLabel.setToolTipText("No application file selected");
        disconnectMenuItemActionPerformed(null);

        treeModel.setRoot(root);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void openMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        int fileChooserStatus;
        JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_APPFILE_OPEN));
        ExtensionsFileFilter filter = new ExtensionsFileFilter("xml");
        logToConsole("Opening application file..");

        fileChooser.setDialogTitle("Open an existing application file..");
        fileChooser.setFileFilter(filter);
        fileChooserStatus = fileChooser.showOpenDialog(this);
        if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            loadApplicationFile(file);

        } else {
            logToConsole("..aborted application file load!");
        }

        if (file != null) {
            fileNameLabel.setText("Application file: " + file.getName());
            fileNameLabel.setToolTipText(file.getAbsolutePath());
            setFileChooserStartDir(FILECHOOSER_APPFILE_OPEN, file);
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    public void loadApplicationFile
            (File
                    file) {
        this.file = file;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = dbf.newDocumentBuilder();
            doc = builder.parse(file);
            root = new Root(doc);
            logToConsole("..application file " + file + " loaded!");
            treeModel.setRoot(root);
            tree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) root.getFirstChild()).getPath()));
            setFileNeedsSavingIndicator(false);
            SelectTablesDialog.clear();
            disconnectMenuItemActionPerformed(null);
            getRecentMenu().addToRecentList(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            logToConsole("Failed to load application file! (" + e + ")", LogLevel.ERROR);
            getRecentMenu().removeFromRecentList(file.getAbsolutePath());
        }
    }

    private static void logToConsole(Object o, LogLevel error) {
        if (jagGenerator == null) {
            System.out.println(o);
        } else {
            jagGenerator.logger.log(o.toString(), error);
        }
    }

    private void saveMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        saveButtonActionPerformed(evt);
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        int fileChooserStatus;
        JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_APPFILE_SAVE));
        fileChooser.setDialogTitle("Save application file..");
        ExtensionsFileFilter filter = new ExtensionsFileFilter("xml");
        fileChooser.setFileFilter(filter);
        fileChooserStatus = fileChooser.showSaveDialog(this);
        if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            setFileChooserStartDir(FILECHOOSER_APPFILE_SAVE, file);
            save();
        }
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void connectMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_connectMenuItemActionPerformed
        GenericJdbcManager previous = conManager;
        conManager = null;
        getConManager();
        if (conManager == null) {
            conManager = previous;

        } else {
            //we're connected!
            DatabaseUtils.clearCache();

            Iterator entities = root.getEntityEjbs().iterator();
            while (entities.hasNext()) {
                Entity entity = (Entity) entities.next();
                for (int i = 0; i < entity.getChildCount(); i++) {
                    TreeNode child = entity.getChildAt(i);
                    if (child instanceof Relation) {
                        ((RelationPanel) ((Relation) child).getPanel()).initValues(false);
                    }
                }
            }
        }
    }//GEN-LAST:event_connectMenuItemActionPerformed

    private void addSessionMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_addSessionMenuItemActionPerformed
        addObject(root, new Session(root.getRootPackage()), true, false);
        setFileNeedsSavingIndicator(true);
    }//GEN-LAST:event_addSessionMenuItemActionPerformed

    private void treeValueChanged
            (TreeSelectionEvent
                    evt) {//GEN-FIRST:event_treeValueChanged
        TreePath path = evt.getNewLeadSelectionPath();
        JagBean jagBean;
        if (path != null) {
            jagBean = (JagBean) path.getLastPathComponent();
        } else {
            jagBean = (JagBean) treeModel.getRoot();
        }
        splitPane.setRightComponent(jagBean.getPanel());
        splitPane.setDividerLocation(SPLIT_PANE_WIDTH);
    }//GEN-LAST:event_treeValueChanged

    private void exitMenuItemActionPerformed
            (java.awt.event.ActionEvent
                    evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        exitForm(null);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void exitForm
            (java.awt.event.WindowEvent
                    evt) {//GEN-FIRST:event_exitForm
        kickTheBucket(null);
    }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem addBusinessMenuItem;
    private javax.swing.JMenuItem addEntityMenuItem;
    private javax.swing.JMenuItem addFieldMenuItem;
    private javax.swing.JMenuItem addRelationMenuItem;
    private javax.swing.JMenuItem addSessionMenuItem;
    private javax.swing.JMenu addSubMenu;
    public javax.swing.JPanel applicationFileInfoPanel;
    private javax.swing.JButton businessMethodButton;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JMenu connectionMenu;
    private javax.swing.JTextPane console;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JMenuItem contentMenuItem;
    public javax.swing.JPanel databaseConnectionInfoPanel;
    public javax.swing.JLabel databaseConnectionLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JSplitPane desktopConsoleSplitPane;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JMenuItem driverManagerMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JButton entityButton;
    private javax.swing.JButton executeButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JButton fieldButton;
    private javax.swing.JMenu fileMenu;
    public javax.swing.JLabel fileNameLabel;
    private javax.swing.JMenuItem generateJavaApplicationAsMenuItem;
    private javax.swing.JButton helpButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importMenuItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newButton;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JButton openButton;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JButton relationButton;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JButton sessionButton;
    private javax.swing.JPanel spacer;
    private javax.swing.JSplitPane splitPane;
    public javax.swing.JTextArea textConsole;
    public javax.swing.JToolBar toolBar;
    private javax.swing.JTree tree;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
    private com.finalist.jaggenerator.menu.RecentMenu recentMenu;

    public com.finalist.jaggenerator.menu.RecentMenu getRecentMenu() {
        return recentMenu;
    }

}
