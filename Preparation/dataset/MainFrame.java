/*
 *
 *  JMoney - A Personal Finance Manager
 *  Copyright (c) 2001-2003 Johann Gyger <jgyger@users.sf.net>
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package net.sf.jmoney.gui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import net.sf.jmoney.*;
import net.sf.jmoney.io.*;
import net.sf.jmoney.model.*;

/**
 * The main frame of the JMoney application
 */
public class MainFrame extends JFrame implements Constants {
    // gui components
    AboutDialog aboutDialog = new AboutDialog(this);
    PreferencesDialog optionsDialog = new PreferencesDialog(this);
    WaitDialog waitDialog = new WaitDialog(this);
    AccountChooser accountChooser = new AccountChooser(this);
    CategoryPanel categoryPanel = new CategoryPanel();
    AccountPanel accountPanel = new AccountPanel();
    AccountBalancesReportPanel accountBalancesReportPanel =
        new AccountBalancesReportPanel();
    IncomeExpenseReportPanel incomeExpenseReportPanel =
        new IncomeExpenseReportPanel();
    JFileChooser fileChooser = new JFileChooser();
    JFileChooser qifFileChooser = new JFileChooser();
    JFileChooser mt940FileChooser = new JFileChooser();
    FileFormat qif = new QIF(this, accountChooser);
    FileFormat mt940 = new MT940(this, accountChooser);
    MenuBar menuBar = new MenuBar();
    ToolBar toolBar = new ToolBar();
    JTree navigationTree = new JTree();
    JTabbedPane jTabbedPane1 = new JTabbedPane();
    JPanel emptyPanel = new JPanel();
    JSplitPane splitPane = new JSplitPane();
    JScrollPane navigationScrollPane = new JScrollPane();
    JPopupMenu navigationPopup = new JPopupMenu();
    JMenuItem newAccountItem = new JMenuItem();
    JMenuItem deleteAccountItem = new JMenuItem();

    PropertyChangeListener accountNameListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof Account
                && evt.getPropertyName().equals("name"))
                accountNameChanged();
        }
    };
    String title = "JMoney " + GENERAL.getString("Version");
    JMoneyFileFilter jmoneyFileFilter = new JMoneyFileFilter();
    FileFilter qifFileFilter = qif.fileFilter();
    FileFilter mt940FileFilter = mt940.fileFilter();

    // user propertes
    Properties properties = new Properties();
    UserProperties userProperties = new UserProperties();
    File propertiesFile;
    String dateFormat;
    EditableMetalTheme theme;

    // session specific
    Session session;
    NavigationTreeModel navigator;
    File sessionFile;

    /**
     * Creates a new main frame.
     */
    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initProperties() throws Exception {
        // read properties
        File homeDir = FileSystemView.getFileSystemView().getDefaultDirectory();
        File jMoneyDir = new File(homeDir, ".jmoney");
        if (!jMoneyDir.exists()) {
            jMoneyDir.mkdir();
        }
        if (jMoneyDir.isDirectory()) {
            propertiesFile = new File(jMoneyDir, "preferences.txt");
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
            }
        }
        InputStream in = new FileInputStream(propertiesFile);
        properties.load(in);
        userProperties.setProperties(properties);

        accountPanel
            .getEntriesPanel()
            .getEntryListItemLabels()
            .setUserProperties(
            userProperties);
        accountPanel.getEntriesPanel().setDateFormat(
            userProperties.getDateFormat());
        accountPanel.getEntriesPanel().setEntryStyle(
            userProperties.getEntryStyle());
        accountPanel.getEntriesPanel().setEntryOrder(
            userProperties.getEntryOrderField(),
            userProperties.getEntryOrder());

        Account.setDefaultCurrencyCode(userProperties.getDefaultCurrency());
        userProperties.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals("lookAndFeel")) {
                    initLookAndFeel();
                } else if (event.getPropertyName().equals("dateFormat")) {
                    accountPanel.getEntriesPanel().setDateFormat(
                        userProperties.getDateFormat());
                } else if (event.getPropertyName().equals("defaultCurrency")) {
                    Account.setDefaultCurrencyCode(
                        userProperties.getDefaultCurrency());
                    updateUIs();
                } else if (event.getPropertyName().equals("entryStyle")) {
                    accountPanel.getEntriesPanel().setEntryStyle(
                        userProperties.getEntryStyle());
                } else if (event.getPropertyName().equals("entryOrder")) {
                    accountPanel.getEntriesPanel().setEntryOrder(
                        userProperties.getEntryOrderField(),
                        userProperties.getEntryOrder());
                }
            }
        });

        // init window geometry and position
        int x = 0, y = 0, w = 800, h = 600;
        try {
            x = Integer.parseInt(properties.getProperty("locationX"));
            y = Integer.parseInt(properties.getProperty("locationY"));
            w = Integer.parseInt(properties.getProperty("width"));
            h = Integer.parseInt(properties.getProperty("height"));
        } catch (NumberFormatException ex) {}
        this.setLocation(x, y);
        this.setSize(w, h);

        initLookAndFeel();

        // init current (last opened) file
        String filename = properties.getProperty("currentFile");
        if (filename == null || filename.equals(""))
            return;
        setSessionFile(new File(filename));
        readSession();
    }

    public void fileReadError(File file) {
        JOptionPane.showMessageDialog(
            this,
            LANGUAGE.getString("MainFrame.CouldNotReadFile")
                + " "
                + file.getPath(),
            LANGUAGE.getString("MainFrame.FileError"),
            JOptionPane.ERROR_MESSAGE);
    }

    public void fileWriteError(File file) {
        JOptionPane.showMessageDialog(
            this,
            LANGUAGE.getString("MainFrame.CouldNotWriteFile")
                + " "
                + file.getPath(),
            LANGUAGE.getString("MainFrame.FileError"),
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Overridden so we can exit on system close.
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            exit();
        }
    }

    /**
     * Auxiliary method to update all UIs of the application
     */
    protected void updateUIs() {
        SwingUtilities.updateComponentTreeUI(this);
        SwingUtilities.updateComponentTreeUI(accountPanel);
        SwingUtilities.updateComponentTreeUI(categoryPanel);
        SwingUtilities.updateComponentTreeUI(aboutDialog);
        SwingUtilities.updateComponentTreeUI(optionsDialog);
        SwingUtilities.updateComponentTreeUI(waitDialog);
        SwingUtilities.updateComponentTreeUI(accountChooser);
        SwingUtilities.updateComponentTreeUI(fileChooser);
        SwingUtilities.updateComponentTreeUI(qifFileChooser);
        SwingUtilities.updateComponentTreeUI(mt940FileChooser);
        SwingUtilities.updateComponentTreeUI(accountBalancesReportPanel);
        SwingUtilities.updateComponentTreeUI(incomeExpenseReportPanel);
        navigationTree.setCellRenderer(new NavigationTreeCellRenderer());
    }

    private boolean dontOverwrite(File file) {
        if (file.exists()) {
            int answer =
                JOptionPane.showConfirmDialog(
                    this,
                    LANGUAGE.getString("MainFrame.OverwriteExistingFile")
                        + " "
                        + file.getPath()
                        + "?",
                    LANGUAGE.getString("MainFrame.FileExists"),
                    JOptionPane.YES_NO_OPTION);
            return answer != JOptionPane.YES_OPTION;
        } else
            return false;
    }

    private void storeProperties() {
        try {
            OutputStream out = new FileOutputStream(propertiesFile);
            properties.setProperty(
                "currentFile",
                sessionFile == null ? "" : sessionFile.getPath());
            properties.setProperty("locationX", Integer.toString(getX()));
            properties.setProperty("locationY", Integer.toString(getY()));
            properties.setProperty("width", Integer.toString(getWidth()));
            properties.setProperty("height", Integer.toString(getHeight()));
            properties.store(out, "User Specific Properties");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(userProperties.getLookAndFeel());
        } catch (Exception ex) {
            System.err.println(
                "Invalid/missing Look&Feel: "
                    + userProperties.getLookAndFeel());
        }
        updateUIs();
    }

    /**
     * Build the GUI.
     */
    private void jbInit() throws Exception {
        setIconImage(ACCOUNTS_ICON.getImage());

        navigationTree.setRootVisible(false);
        navigationTree.setCellRenderer(new NavigationTreeCellRenderer());
        newAccountItem.setText(LANGUAGE.getString("MainFrame.newAccount"));
        newAccountItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newAccount();
            }
        });
        deleteAccountItem.setEnabled(false);
        deleteAccountItem.setText(
            LANGUAGE.getString("MainFrame.deleteAccount"));
        deleteAccountItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

        navigationTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        navigationTree.setModel(null);
        navigationTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                navigationTreeSelection(
                    (DefaultMutableTreeNode) e
                        .getPath()
                        .getLastPathComponent());
            }
        });
        // north panel -------------------------------------------------------------
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane.add(navigationScrollPane, JSplitPane.LEFT);
        splitPane.add(emptyPanel, JSplitPane.RIGHT);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        navigationScrollPane.getViewport().add(navigationTree, null);
        navigationPopup.add(newAccountItem);
        navigationPopup.add(deleteAccountItem);

        // this --------------------------------------------------------------------
        setTitle(title);
        setJMenuBar(menuBar);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(true);

        qifFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        qifFileChooser.setMultiSelectionEnabled(false);
        qifFileChooser.setAcceptAllFileFilterUsed(true);

        mt940FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        mt940FileChooser.setMultiSelectionEnabled(false);
        mt940FileChooser.setAcceptAllFileFilterUsed(true);

        splitPane.setDividerLocation(150);
        navigationTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    if (navigationTree.getSelectionCount() > 0) {
                        DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) navigationTree
                                .getSelectionPath()
                                .getLastPathComponent();
                        deleteAccountItem.setEnabled(
                            node.getUserObject() instanceof Account);
                    }
                    navigationPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void navigationTreeSelection(DefaultMutableTreeNode node) {
        if (node == null) {
            splitPane.add(emptyPanel, JSplitPane.RIGHT);
        } else if (node.getParent() == navigator.getAccountNode()) {
            accountPanel.setModel((Account) node.getUserObject());
            splitPane.add(accountPanel, JSplitPane.RIGHT);
        } else if (node == navigator.getCategoryNode()) {
            splitPane.add(categoryPanel, JSplitPane.RIGHT);
        } else if (node == navigator.getBalancesReportNode()) {
            splitPane.add(accountBalancesReportPanel, JSplitPane.RIGHT);
            accountBalancesReportPanel.setSession(session);
            accountBalancesReportPanel.setDateFormat(
                userProperties.getDateFormat());
        } else if (node == navigator.getIncomeExpenseReportNode()) {
            splitPane.add(incomeExpenseReportPanel, JSplitPane.RIGHT);
            incomeExpenseReportPanel.setSession(session);
            incomeExpenseReportPanel.setDateFormat(
                userProperties.getDateFormat());
            //		} else if (node == navigator.getAccountReportNode()) {
        } else {
            splitPane.add(emptyPanel, JSplitPane.RIGHT);
        }
        splitPane.setDividerLocation(splitPane.getDividerLocation());
    }

    /**
     * Creates a new session. Used by Menu File->New.
     */
    private void newSession() {
        if (saveOldSession()) {
            setSessionFile(null);
            setSession(new Session(0));
        }
    }

    /**
     * Read session from file.
     */
    private void readSession() {
        try {
            waitDialog.show(
                LANGUAGE.getString("MainFrame.OpeningFile")
                    + " "
                    + sessionFile);
            FileInputStream fin = new FileInputStream(sessionFile);
            GZIPInputStream gin = new GZIPInputStream(fin);
            BufferedInputStream bin = new BufferedInputStream(gin);
            XMLDecoder dec = new XMLDecoder(bin);
            Session newSession = (Session) dec.readObject();
            dec.close();
            setSession(newSession);
            waitDialog.stop();
        } catch (IOException ex) {
            waitDialog.stop();
            fileReadError(sessionFile);
            setSessionFile(null);
        }
    }

    /**
     * Write session to file.
     */
    private void writeSession() {
        boolean modified = session.isModified();
        try {
            waitDialog.show(
                LANGUAGE.getString("MainFrame.SavingFile") + " " + sessionFile);
            FileOutputStream fout = new FileOutputStream(sessionFile);
            GZIPOutputStream gout = new GZIPOutputStream(fout);
            BufferedOutputStream bout = new BufferedOutputStream(gout);
            XMLEncoder enc = new XMLEncoder(bout);
            session.setModified(false);
            enc.writeObject(session);
            enc.close();
            waitDialog.stop();
        } catch (IOException ex) {
            session.setModified(modified);
            waitDialog.stop();
            fileWriteError(sessionFile);
        }
    }

    /**
     * Opens the session from a file.
     */
    private void openSession() {
        if (saveOldSession()) {
            fileChooser.setFileFilter(jmoneyFileFilter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File oldFile = sessionFile;
                setSessionFile(fileChooser.getSelectedFile());
                readSession();
                if (sessionFile == null)
                    setSessionFile(oldFile);
            }
        }
    }

    /**
     * Closes the session.
     */
    private void closeSession() {
        if (saveOldSession()) {
            removeAccountNameListener();
            menuBar.setSessionOpened(false);
            toolBar.setSessionOpened(false);
            navigationTree.setModel(null);
            splitPane.add(emptyPanel, JSplitPane.RIGHT);
            splitPane.setDividerLocation(splitPane.getDividerLocation());
            setSessionFile(null);
            session = null;
            navigator = null;
        }
    }

    /**
     * Saves the session in the selected file.
     */
    private void saveSession() {
        if (sessionFile == null)
            saveSessionAs();
        else
            writeSession();
    }

    /**
     * Saves the session in a user specified file.
     */
    private void saveSessionAs() {
        fileChooser.setFileFilter(jmoneyFileFilter);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (dontOverwrite(file))
                return;

            setSessionFile(file);
            writeSession();
        }
    }

    /**
     * Import QIF file
     */
    private void importQIF() {
        qifFileChooser.setDialogTitle(
            LANGUAGE.getString("MainFrame.import"));
        qifFileChooser.setFileFilter(qifFileFilter);
        int result = qifFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            navigationTree.clearSelection();
            navigationTreeSelection(null);
            qif.importFile(session, qifFileChooser.getSelectedFile());
            initAccountNode();
            updateNavigationTree();
        }
    }

    /**
     * Import MT940 file
     */
    private void importMT940() {
        mt940FileChooser.setDialogTitle(
            LANGUAGE.getString("MainFrame.import"));
        mt940FileChooser.setFileFilter(mt940FileFilter);
        int result = mt940FileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            navigationTree.clearSelection();
            navigationTreeSelection(null);
            mt940.importFile(session, mt940FileChooser.getSelectedFile());
            initAccountNode();
            updateNavigationTree();
        }
    }

    /**
     * Export to QIF file.
     */
    private void exportQIF() {
        qifFileChooser.setDialogTitle(
            LANGUAGE.getString("MainFrame.qifExportTitle"));
        qifFileChooser.setFileFilter(qifFileFilter);
        int result = qifFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File qifFile = qifFileChooser.getSelectedFile();
            if (dontOverwrite(qifFile))
                return;

            result =
                accountChooser.showDialog(
                    session.getAccounts(),
                    LANGUAGE.getString("MainFrame.chooseAccountToExport"));
            if (result == OK)
                qif.exportAccount(
                    session,
                    accountChooser.getSelectedAccount(),
                    qifFileChooser.getSelectedFile());
        }
    }

    /**
     * Exit application.
     */
    private void exit() {
        if (saveOldSession()) {
            storeProperties();
            System.exit(0);
        }
    }

    /**
     * Creates a new account.
     */
    private void newAccount() {
        Account account =
            session.getNewAccount(LANGUAGE.getString("Account.newAccount"));
        Collections.sort(session.getAccounts());
        CategoryNode node = new CategoryNode(account);
        account.addPropertyChangeListener(accountNameListener);
        navigator.insertNodeInto(node, navigator.getAccountNode(), 0);
        Object path[] = navigator.getPathToRoot(node);
        navigationTree.setSelectionPath(new TreePath(path));
        accountPanel.tabbedPane.setSelectedComponent(
            accountPanel.propertiesPanel);
    }

    /**
     * Removes an existing account.
     */
    private void deleteAccount() {
        CategoryNode node =
            (CategoryNode) navigationTree
                .getSelectionPath()
                .getLastPathComponent();
        Account account = (Account) node.getUserObject();
        session.getCategories().removeNodeFromParent(account.getCategoryNode());
        session.getAccounts().removeElement(account);
        navigator.removeNodeFromParent(node);
        session.modified();
    }

    private void about() {
        aboutDialog.showDialog();
    }

    private void setEntryOrder() {
        //accountPanel.setEntryOrder(entryOrderBox.getSelectedIndex());
    }

    /**
     * Saves the old session.
     * Returns false if canceled by user.
     */
    private boolean saveOldSession() {
        if (session != null && session.isModified()) {
            String title = LANGUAGE.getString("MainFrame.saveOldSessionTitle");
            String question =
                LANGUAGE.getString("MainFrame.saveOldSessionQuestion");
            int answer =
                JOptionPane.showConfirmDialog(
                    this,
                    question,
                    title,
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (answer == JOptionPane.YES_OPTION)
                saveSession();
            return answer != JOptionPane.CANCEL_OPTION;
        }
        return true;
    }

    private void setSession(Session newSession) {
        session = newSession;

        Collections.sort(session.getAccounts());
        navigator = new NavigationTreeModel();
        initAccountNode();
        navigationTree.setModel(navigator);
        Object[] path = navigator.getPathToRoot(navigator.getAccountNode());
        navigationTree.expandPath(new TreePath(path));
        path = navigator.getPathToRoot(navigator.getReportNode());
        navigationTree.expandPath(new TreePath(path));

        accountPanel.setSession(session);
        categoryPanel.setSession(session);
        menuBar.setSessionOpened(true);
        toolBar.setSessionOpened(true);
    }

    private void removeAccountNameListener() {
        for (Enumeration e = navigator.getAccountNode().children();
            e.hasMoreElements();
            ) {
            Account a =
                (Account) ((CategoryNode) e.nextElement()).getUserObject();
            a.removePropertyChangeListener(accountNameListener);
        }
    }

    private void initAccountNode() {
        DefaultMutableTreeNode accountNode = navigator.getAccountNode();

        removeAccountNameListener();
        accountNode.removeAllChildren();

        // add accounts to navigator and install listeners
        Vector accounts = session.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            Account a = (Account) accounts.elementAt(i);
            accountNode.add(new CategoryNode(a));
            a.addPropertyChangeListener(accountNameListener);
        }
    }

    private void updateNavigationTree() {
        TreePath tp = navigationTree.getSelectionPath();
        navigationTree.clearSelection();
        navigationTree.updateUI();
        navigationTree.setSelectionPath(tp);
    }

    private void accountNameChanged() {
        Collections.sort(session.getAccounts());

        TreePath tp = navigationTree.getSelectionPath();
        navigator.sortChildren(navigator.getAccountNode());
        navigationTree.setSelectionPath(tp);
    }

    private void setSessionFile(File file) {
        sessionFile = file;
        setTitle(
            sessionFile == null
                ? title
                : title + " - " + sessionFile.getName());
    }

    class MenuBar extends JMenuBar {
        JMenu fileMenu = new JMenu();
        JMenu editMenu = new JMenu();
        JMenu helpMenu = new JMenu();
        JMenu optionsMenu = new JMenu();
        JMenuItem newFileItem = new JMenuItem();
        JMenuItem openFileItem = new JMenuItem();
        JMenuItem closeFileItem = new JMenuItem();
        JMenuItem saveFileItem = new JMenuItem();
        JMenuItem saveAsFileItem = new JMenuItem();
        JMenuItem undoItem = new JMenuItem();
        JMenuItem redoItem = new JMenuItem();
        JMenuItem cutItem = new JMenuItem();
        JMenuItem copyItem = new JMenuItem();
        JMenuItem pasteItem = new JMenuItem();
        JMenuItem findItem = new JMenuItem();
        JMenuItem findAgainItem = new JMenuItem();
        JMenuItem preferencesItem = new JMenuItem();
        JMenuItem exitFileItem = new JMenuItem();
        JMenuItem aboutItem = new JMenuItem();
        JMenu importMenu = new JMenu();
        JMenuItem qifImportItem = new JMenuItem();
        JMenuItem mt940ImportItem = new JMenuItem();
        JMenuItem exportFileItem = new JMenuItem();
        JMenuItem printItem = new JMenuItem();

        /**
         * Creates a new main frame.
         */
        public MenuBar() {
            try {
                jbInit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * De-/activates the corresponding buttons.
         */
        private void setSessionOpened(boolean state) {
            closeFileItem.setEnabled(state);
            saveFileItem.setEnabled(state);
            saveAsFileItem.setEnabled(state);
            qifImportItem.setEnabled(state);
            mt940ImportItem.setEnabled(state);
            exportFileItem.setEnabled(state);
        }

        private void jbInit() throws Exception {
            setSessionOpened(false);

            // file menu -------------------------------------------------------
            // new file
            newFileItem.setIcon(NEW_ICON);
            newFileItem.setText(LANGUAGE.getString("MainFrame.new"));
            newFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.new.mnemonic").charAt(0));
            newFileItem.setAccelerator(
                KeyStroke.getKeyStroke('N', KeyEvent.CTRL_MASK));
            newFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    newSession();
                }
            });
            // open file
            openFileItem.setIcon(OPEN_ICON);
            openFileItem.setText(LANGUAGE.getString("MainFrame.open"));
            openFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.open.mnemonic").charAt(0));
            openFileItem.setAccelerator(
                KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));
            openFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openSession();
                }
            });
            // close file
            closeFileItem.setText(LANGUAGE.getString("MainFrame.close"));
            closeFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.close.mnemonic").charAt(0));
            closeFileItem.setAccelerator(
                KeyStroke.getKeyStroke('W', KeyEvent.CTRL_MASK));
            closeFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeSession();
                }
            });
            // save file
            saveFileItem.setIcon(SAVE_ICON);
            saveFileItem.setText(LANGUAGE.getString("MainFrame.save"));
            saveFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.save.mnemonic").charAt(0));
            saveFileItem.setAccelerator(
                KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));
            saveFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveSession();
                }
            });
            // save as file
            saveAsFileItem.setIcon(SAVE_AS_ICON);
            saveAsFileItem.setText(LANGUAGE.getString("MainFrame.saveAs"));
            saveAsFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.saveAs.mnemonic").charAt(0));
            saveAsFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveSessionAs();
                }
            });
            // print
            printItem.setEnabled(false);
            printItem.setIcon(PRINT_ICON);
            printItem.setText(LANGUAGE.getString("MainFrame.print"));
            printItem.setMnemonic(
                LANGUAGE.getString("MainFrame.print.mnemonic").charAt(0));
            printItem.setAccelerator(
                KeyStroke.getKeyStroke('P', KeyEvent.CTRL_MASK));
            // import menu
            importMenu.setIcon(IMPORT_ICON);
            importMenu.setText(LANGUAGE.getString("MainFrame.import"));
            // import QIF
            qifImportItem.setIcon(IMPORT_ICON);
            qifImportItem.setText("QIF...");
            qifImportItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    importQIF();
                }
            });
            // import MT940
            mt940ImportItem.setIcon(IMPORT_ICON);
            mt940ImportItem.setText("MT940...");
            mt940ImportItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    importMT940();
                }
            });
            // export file
            exportFileItem.setIcon(EXPORT_ICON);
            exportFileItem.setText(LANGUAGE.getString("MainFrame.export"));
            exportFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.export.mnemonic").charAt(0));
            exportFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportQIF();
                }
            });
            // exit
            exitFileItem.setText(LANGUAGE.getString("MainFrame.exit"));
            exitFileItem.setMnemonic(
                LANGUAGE.getString("MainFrame.exit.mnemonic").charAt(0));
            exitFileItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exit();
                }
            });
            // add items to file menu
            fileMenu.add(newFileItem);
            fileMenu.add(openFileItem);
            fileMenu.add(closeFileItem);
            fileMenu.addSeparator();
            fileMenu.add(saveFileItem);
            fileMenu.add(saveAsFileItem);
            fileMenu.addSeparator();
            fileMenu.add(printItem);
            fileMenu.addSeparator();
            fileMenu.add(importMenu);
            importMenu.add(qifImportItem);
            importMenu.add(mt940ImportItem);
            fileMenu.add(exportFileItem);
            fileMenu.addSeparator();
            fileMenu.add(exitFileItem);

            // edit menu -------------------------------------------------------
            undoItem.setEnabled(false);
            undoItem.setIcon(UNDO_ICON);
            undoItem.setText(LANGUAGE.getString("MainFrame.undo"));
            undoItem.setMnemonic(
                LANGUAGE.getString("MainFrame.undo.mnemonic").charAt(0));
            undoItem.setAccelerator(
                KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_MASK));
            redoItem.setEnabled(false);
            redoItem.setIcon(REDO_ICON);
            redoItem.setText(LANGUAGE.getString("MainFrame.redo"));
            redoItem.setMnemonic(
                LANGUAGE.getString("MainFrame.redo.mnemonic").charAt(0));
            redoItem.setAccelerator(
                KeyStroke.getKeyStroke('Y', KeyEvent.CTRL_MASK));
            cutItem.setEnabled(false);
            cutItem.setIcon(CUT_ICON);
            cutItem.setText(LANGUAGE.getString("MainFrame.cut"));
            cutItem.setMnemonic(
                LANGUAGE.getString("MainFrame.cut.mnemonic").charAt(0));
            cutItem.setAccelerator(
                KeyStroke.getKeyStroke('X', KeyEvent.CTRL_MASK));
            copyItem.setEnabled(false);
            copyItem.setIcon(COPY_ICON);
            copyItem.setText(LANGUAGE.getString("MainFrame.copy"));
            copyItem.setMnemonic(
                LANGUAGE.getString("MainFrame.copy.mnemonic").charAt(0));
            copyItem.setAccelerator(
                KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK));
            pasteItem.setEnabled(false);
            pasteItem.setIcon(PASTE_ICON);
            pasteItem.setText(LANGUAGE.getString("MainFrame.paste"));
            pasteItem.setMnemonic(
                LANGUAGE.getString("MainFrame.paste.mnemonic").charAt(0));
            pasteItem.setAccelerator(
                KeyStroke.getKeyStroke('V', KeyEvent.CTRL_MASK));
            findItem.setEnabled(false);
            findItem.setIcon(FIND_ICON);
            findItem.setText(LANGUAGE.getString("MainFrame.find"));
            findItem.setMnemonic(
                LANGUAGE.getString("MainFrame.find.mnemonic").charAt(0));
            findItem.setAccelerator(
                KeyStroke.getKeyStroke('F', KeyEvent.CTRL_MASK));
            findAgainItem.setEnabled(false);
            findAgainItem.setIcon(FIND_AGAIN_ICON);
            findAgainItem.setText(LANGUAGE.getString("MainFrame.findAgain"));
            findAgainItem.setMnemonic(
                LANGUAGE.getString("MainFrame.findAgain.mnemonic").charAt(0));
            findAgainItem.setAccelerator(
                KeyStroke.getKeyStroke('G', KeyEvent.CTRL_MASK));
            // add items to edit menu
            editMenu.add(undoItem);
            editMenu.add(redoItem);
            editMenu.addSeparator();
            editMenu.add(cutItem);
            editMenu.add(copyItem);
            editMenu.add(pasteItem);
            editMenu.addSeparator();
            editMenu.add(findItem);
            editMenu.add(findAgainItem);

            // options menu ----------------------------------------------------
            preferencesItem.setIcon(PREFERENCES_ICON);
            preferencesItem.setText(
                LANGUAGE.getString("MainFrame.preferences"));
            preferencesItem.setMnemonic(
                LANGUAGE.getString("MainFrame.preferences.mnemonic").charAt(0));
            preferencesItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    optionsDialog.showDialog(userProperties);
                }
            });
            optionsMenu.add(preferencesItem);

            // help menu ---------------------------------------------------------------
            aboutItem.setIcon(ABOUT_ICON);
            aboutItem.setText(LANGUAGE.getString("MainFrame.about"));
            aboutItem.setMnemonic(
                LANGUAGE.getString("MainFrame.about.mnemonic").charAt(0));
            aboutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    about();
                }
            });
            helpMenu.add(aboutItem);

            // menu bar ----------------------------------------------------------------
            fileMenu.setText(LANGUAGE.getString("MainFrame.file"));
            fileMenu.setMnemonic(
                LANGUAGE.getString("MainFrame.file.mnemonic").charAt(0));
            editMenu.setText(LANGUAGE.getString("MainFrame.edit"));
            editMenu.setMnemonic(
                LANGUAGE.getString("MainFrame.edit.mnemonic").charAt(0));
            optionsMenu.setText(LANGUAGE.getString("MainFrame.options"));
            optionsMenu.setMnemonic(
                LANGUAGE.getString("MainFrame.options.mnemonic").charAt(0));
            helpMenu.setText(LANGUAGE.getString("MainFrame.help"));
            helpMenu.setMnemonic(
                LANGUAGE.getString("MainFrame.help.mnemonic").charAt(0));
            // add menus to menu bar
            add(fileMenu);
            add(editMenu);
            add(optionsMenu);
            add(helpMenu);
        }
    }

    class ToolBar extends JToolBar {
        private JButton newFile = new JButton();
        private JButton open = new JButton();
        private JButton save = new JButton();
        private JButton saveAs = new JButton();
        private JButton print = new JButton();

        private JButton cut = new JButton();
        private JButton copy = new JButton();
        private JButton paste = new JButton();
        private Insets insets = new Insets(0, 0, 0, 0);

        public ToolBar() {
            try {
                jbInit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setSessionOpened(boolean state) {
            save.setEnabled(state);
            saveAs.setEnabled(state);
        }

        private void jbInit() throws Exception {
            setFloatable(false);
            setRollover(true);
            setSessionOpened(false);

            newFile.setIcon(NEW_ICON);
            newFile.setMargin(insets);
            newFile.setToolTipText(LANGUAGE.getString("MainFrame.new"));
            newFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    newSession();
                }
            });

            open.setIcon(OPEN_ICON);
            open.setMargin(insets);
            open.setToolTipText(LANGUAGE.getString("MainFrame.open"));
            open.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openSession();
                }
            });

            save.setIcon(SAVE_ICON);
            save.setMargin(insets);
            save.setToolTipText(LANGUAGE.getString("MainFrame.save"));
            save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveSession();
                }
            });

            saveAs.setIcon(SAVE_AS_ICON);
            saveAs.setMargin(insets);
            saveAs.setToolTipText(LANGUAGE.getString("MainFrame.saveAs"));
            saveAs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveSessionAs();
                }
            });

            print.setEnabled(false);
            print.setIcon(PRINT_ICON);
            print.setMargin(insets);
            print.setToolTipText(LANGUAGE.getString("MainFrame.print"));

            cut.setEnabled(false);
            cut.setIcon(CUT_ICON);
            cut.setMargin(insets);
            cut.setToolTipText(LANGUAGE.getString("MainFrame.cut"));

            copy.setEnabled(false);
            copy.setIcon(COPY_ICON);
            copy.setMargin(insets);
            copy.setToolTipText(LANGUAGE.getString("MainFrame.copy"));

            paste.setEnabled(false);
            paste.setIcon(PASTE_ICON);
            paste.setMargin(insets);
            paste.setToolTipText(LANGUAGE.getString("MainFrame.paste"));

            add(newFile);
            add(open);
            add(save);
            add(saveAs);
            add(print);
            addSeparator();
            add(cut);
            add(copy);
            add(paste);
        }
    }

    /**
     * A filter that accepts JMoney files.
     */
    public class JMoneyFileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory())
                return true;
            return f.getName().toLowerCase().endsWith(FILE_EXTENSION);
        }
        public String getDescription() {
            return Constants.FILE_FILTER_NAME;
        }
    }

}
