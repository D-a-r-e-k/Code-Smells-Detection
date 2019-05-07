/*
 * Copyright (C) 2001-2004 Gaudenz Alder
 *
 * JGraphpad is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 *
 * GPGraphpad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GPGraphpad; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.microplatform.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.microplatform.ActionCommand;
import org.microplatform.BarFactory;
import org.microplatform.Document;
import org.microplatform.ICommandRegistery;
import org.microplatform.SessionParameters;
import org.microplatform.SwingWorker;
import org.microplatform.loaders.LocaleChangeAdapter;
import org.microplatform.loaders.PluginInvoker;
import org.microplatform.loaders.Translator;
import org.microplatform.loaders.PluginInvoker.TrampolineAction;

/**
 * A JComponent containing a multi document JGraph interface. To
 * gain the control of this class, you could subclass it or  decorate it.
 * But probably registering custom delegates and properties in the properties
 * file will be sufficient.
 * 
 * @author Gaudenz Alder
 * @author Sven Luzar
 * @author Raphael Valyi (major refactoring since Jan 2006, contributed as LGPL)
 * @version 1.3.2 Actions moved to an own package
 * @version 1.0 1/1/02
 */
public abstract class MDIContainer extends JComponent implements ICommandRegistery {
	
	/**
	 * parameters that can change from one sessions to another
	 */
	protected SessionParameters sessionParameters;

	/**
	 * Boolean for the visible state of the toolbars
	 */
	protected boolean toolBarsVisible = true;

	/**
	 * Log console for the System in and out messages
	 */
	protected static Component logger;

	/**
	 * Desktoppane for the internal frames
	 */
	protected JDesktopPane desktop = new JDesktopPane();

	/**
	 * Contains the mapping between GPDocument objects and GPInternalFrames.
	 */
	protected Hashtable doc2InternalFrame = new Hashtable();

	/** 
	 * The toolbar for this graphpad
	 */
	protected JPanel toolBarMainPanel = new JPanel(new BorderLayout());

	/**
	 * The toolbar for this graphpad
	 */
	protected JPanel toolBarInnerPanel;

	/**
	 * The menubar for this graphpad
	 */
	protected JMenuBar menubar;

	/**
	 * The statusbar for this Graphpad instance
	 */
	protected StatusBar statusbar;

	/**
	 * The main Panel with the status bar and the desktop pane
	 */
	protected JPanel mainPanel;
	
	protected PluginInvoker pluginInvoker;

	/**
	 * A configuration specific to the Graphpad instance. Remark: we would
	 * hardly need it, the static configuration should be sufficient.
	 */
	public MDIContainer(SessionParameters sessionParameters) {
		this.sessionParameters = sessionParameters;
	}
		
	public void init() {
		pluginInvoker = new PluginInvoker(this);
		mainPanel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        add(mainPanel);

		// create the statusbar:
		createStatusBar();

		//	room for plugins customization:
        PluginInvoker.decorateGraphpad(this);
        
        //lazy toolbars initialization:
        final MDIContainer container = this;
        mainPanel.add(BorderLayout.CENTER, desktop);
        
        if (true) {
            final SwingWorker worker =
            new SwingWorker() {
              public Object construct() {
                  if (container.getSessionParameters().isApplet()) {
                  	container.getSessionParameters().getApplet().repaint();
              	}
                  menubar =BarFactory.getInstance().createMenubar(container);
                  toolBarInnerPanel = BarFactory.getInstance().createToolBars(
                  toolBarMainPanel, BarFactory.PADTOOLBARS, container);
             
    			mainPanel.remove(desktop);
    			toolBarInnerPanel.add(desktop, BorderLayout.CENTER);
                toolBarInnerPanel.add(statusbar, BorderLayout.SOUTH);
                mainPanel.add(BorderLayout.NORTH, menubar);
    			mainPanel.add(toolBarMainPanel, BorderLayout.CENTER);
                update();

                Runnable callRevalidate = new Runnable() {
                    public void run() {
                        LocaleChangeAdapter.updateContainer(menubar);//really needs to be done at the end of the swing thread!
          	        	LocaleChangeAdapter.updateContainer(toolBarMainPanel);
          	        	setBorder(BorderFactory.createEtchedBorder());
                        if (container.getSessionParameters().isApplet())
                          	container.getSessionParameters().getApplet().validate();
                    }
                };
                SwingUtilities.invokeLater(callRevalidate);
                backgroundFetchTrampolineActions();
                
                return null;
              }
          };
          worker.start();
        }
	}
    
	public void executeCommand(String key) {
		 getCommand(key).actionPerformed(null);
	}

	public Action getCommand(String key) {
		return getCommand(key, getActionMap());
	}
	
	public final Action getCommand(final String key, ActionMap map) {
		return pluginInvoker.getCommand(key, this);
	}
    
    public final void initCommand(ActionListener action) {//nothing required here
    }

	/**
	 * return a shutdown routine.
	 */
	public WindowAdapter getAppCloser() {
		return new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				getCommand("FileExit").actionPerformed(null);
			}
		};
	}

	/**
	 * Find the hosting frame, for the file-chooser dialog.
	 */
	public Frame getFrame() {
		for (Container p = getParent(); p != null; p = p.getParent()) {
			if (p instanceof Frame) {
				return (Frame) p;
			}
		}
		return null;
	}

	public JMenuBar getMenubar() {
		return menubar;
	}

	/**
	 * Create a status bar
	 */
	protected StatusBar createStatusBar() {
		statusbar = new StatusBar();
		return statusbar;
	}

	public StatusBar getStatusBar() {
		return statusbar;
	}

	/**
	 * Show a dialog with the given error message.
	 */
	public static void error(String message, Component pad) {
		JOptionPane.showMessageDialog(pad, message, Translator.getString("Title"),
				JOptionPane.ERROR_MESSAGE);
	}
	
	public  void error(String message) {
		message(message, JOptionPane.ERROR_MESSAGE);
	}
	
	public  void warning(String message) {
		message(message, JOptionPane.WARNING_MESSAGE);
	}
	
	public void info(String message) {
		message(message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void message(String message, int type) {
		JOptionPane.showMessageDialog(this, message, Translator.getString("Title"),
				type);
	}

	public JInternalFrame getCurrentInternalFrame() {
		DocFrame internalFrame = (DocFrame) desktop
				.getSelectedFrame();
		if (internalFrame == null) {
			JInternalFrame[] frames = desktop.getAllFrames();
			if (frames.length > 0) {
				try {
					frames[0].setSelected(true);
					internalFrame = (DocFrame) frames[0];
				} catch (PropertyVetoException e) {
					return null;
				}
			}
		}
		if (internalFrame == null)
			return null;
		return internalFrame;
	}

	public Document getCurrentDocument() {
		DocFrame internalFrame = (DocFrame) desktop
				.getSelectedFrame();
		if (internalFrame == null) {
			JInternalFrame[] frames = desktop.getAllFrames();
			if (frames.length > 0) {
				try {
					frames[0].setSelected(true);
					internalFrame = (DocFrame) frames[0];
				} catch (PropertyVetoException e) {
					return null;
				}
			}
		}
		if (internalFrame == null)
			return null;
		return internalFrame.getDocument();
	}

	public Document[] getAllDocuments() {
		JInternalFrame[] frames = desktop.getAllFrames();

		if (frames != null && frames.length > 0) {
			ArrayList docs = new ArrayList();
			for (int i = 0; i < frames.length; i++) {
				// make sure to only pick up GPDocFrame instances
				if (frames[i] instanceof DocFrame) {
					docs.add(((DocFrame) frames[i]).getDocument());
				}
			}
			return (Document[]) docs.toArray(new Document[docs.size()]);
		}
		return null;
	}

	/**
	 * Returns the undoAction.
	 * 
	 * @return UndoAction
	 */
	public ActionCommand getEditUndoAction() {
		return (ActionCommand) this.getCommand("EditUndo");
	}

	/**
	 * Returns the redoAction.
	 * 
	 * @return RedoAction
	 */
	public ActionCommand getEditRedoAction() {
		return (ActionCommand) this.getCommand("EditRedo");
	}

	public Component getLogConsole() {
		return logger;
	}

	public void setLogConsole(Component console) {
		logger = console;
	}

	public boolean isToolBarsVisible() {
		return toolBarsVisible;
	}

	public void setToolBarsVisible(boolean state) {
		toolBarsVisible = state;

		if (state) {
			mainPanel.remove(desktop);
			toolBarInnerPanel.add(desktop, BorderLayout.CENTER);
            toolBarInnerPanel.add(statusbar, BorderLayout.SOUTH);
            mainPanel.add(BorderLayout.NORTH, menubar);
			mainPanel.add(toolBarMainPanel, BorderLayout.CENTER);
		} else {
			mainPanel.remove(toolBarMainPanel);//TODO: repair!
			toolBarInnerPanel.remove(desktop);
			mainPanel.add(BorderLayout.CENTER, desktop);
            toolBarInnerPanel.add(statusbar, BorderLayout.SOUTH);
		}
        update();
	}

	public void addGPInternalFrame(DocFrame f) {
		desktop.add(f);
		try {
			f.setMaximum(true);
			f.setSelected(true);
		} catch (Exception ex) {
		}
		doc2InternalFrame.put(f.getDocument(), f);
		if (doc2InternalFrame.size() > 1)
			this.executeCommand("WindowCascade");
	}

	/**
	 * removes the specified Internal Frame from the Graphpad
	 */
	public void removeGPInternalFrame(DocFrame f) {
		if (f == null)
			return;
		f.setVisible(false);
		desktop.remove(f);
		doc2InternalFrame.remove(f.getDocument());
		f.cleanUp();
		JInternalFrame[] frames = desktop.getAllFrames();
		if (frames.length > 0) {
			try {
				frames[0].setSelected(true);
			} catch (PropertyVetoException e) {
			}
		}
	}

	/**
	 * ask every action to update itself
	 */
	public void update() {
		Object[] keys = getActionMap().keys();
        if (keys == null)
            return;
		for (int i = 0; i < keys.length; i++) {
			Action a = getActionMap().get(keys[i]);

			if (a instanceof ActionCommand) {
				((ActionCommand) a).update();
			} else {
				if (getCurrentDocument() == null) {
					a.setEnabled(false);
				} else {
					a.setEnabled(true);
				}
			}
		}
	}
	
	public void backgroundFetchTrampolineActions() {
        final SwingWorker worker =
            new SwingWorker() {
              public Object construct() {
          		Object[] keys = getActionMap().keys();
                if (keys == null)
                    return null;
        		for (int i = 0; i < keys.length; i++) {
        			Action a = getActionMap().get(keys[i]);
        			if (a instanceof TrampolineAction) {
        				((TrampolineAction) a).initDelegate();
        			}
        		}
    			return null;
              }
          };
          worker.start();
	}

	public JInternalFrame[] getAllFrames() {
		return desktop.getAllFrames();
	}

	public void addDesktopContainerListener(ContainerListener listener) {
		desktop.addContainerListener(listener);
	}

	public void removeDesktopContainerListener(ContainerListener listener) {
		desktop.removeContainerListener(listener);
	}

	public Hashtable getDoc2InternalFrame() {
		return doc2InternalFrame;
	}

	public void setDoc2InternalFrame(Hashtable doc2InternalFrame) {
		this.doc2InternalFrame = doc2InternalFrame;
	}

	public abstract void addDocument(String name, InputStream input);

	public void removeDocument(Document doc) {
		DocFrame iFrame = (DocFrame) getDoc2InternalFrame().get(doc);
		removeGPInternalFrame(iFrame);
        if (getCurrentDocument() == null)
            update();
	}

	public SessionParameters getSessionParameters() {
		return sessionParameters;
	}

	public void setSessionParameters(SessionParameters sessionParameters) {
		this.sessionParameters = sessionParameters;
	}

    public JDesktopPane getDesktop() {
        return desktop;
    }

	public PluginInvoker getPluginInvoker() {
		return pluginInvoker;
	}

	public void setPluginInvoker(PluginInvoker pluginInvoker) {
		this.pluginInvoker = pluginInvoker;
	}
}
