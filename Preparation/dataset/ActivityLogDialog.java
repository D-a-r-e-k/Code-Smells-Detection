/*
ActivityLog.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

Copyright (C) 2005 Trish Harnett (trishah136@member.fsf.org)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
Optionally, you may find a copy of the GNU General Public License
from http://www.fsf.org/copyleft/gpl.txt
*/

package net.sourceforge.jsxe.gui;

//{{{ imports
/*
All classes are listed explicitly so
it is easy to see which package it
belongs to.
*/

//{{{ Java SDK classes
import java.util.*;
import java.io.*;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
//}}}

//{{{ JSXE classes
import net.sourceforge.jsxe.util.Log;
//}}}

//}}}


/**
 * Dialog box which appears in response to ActivityLogAction being triggered
 *
 * @author  Trish Hartnett
 * @version $Id: ActivityLogDialog.java,v 1.6 2006/04/08 20:37:04 ian_lewis Exp $
 */
public class ActivityLogDialog  extends EnhancedDialog {
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JList contentsJList;
    private javax.swing.JButton close;  
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JScrollPane activityLogJScrollPane;
    private String m_geometryName = "activitylog";
    // End of variables declaration//GEN-END:variables
	
    // {{{ ActivityLogDialog()
    /**
     * @param TabbedView parent view containing the JSXE editor.
     * Constructor for the ActivityLogDialog class
     * @since jsXe 0.3pre15
     */
	public ActivityLogDialog(TabbedView parent) {
		super(parent, Messages.getMessage("ActivityLogDialog.Dialog.Title"), true);		
		contentsJList = new JList();
        contentsJList.setModel(getContents());
        activityLogJScrollPane = new javax.swing.JScrollPane(contentsJList);
		loadGeometry(this, m_geometryName);
        initComponents();
	}//}}}
	
    // {{{ initComponents()
    /**
     * @param JList containing contents of log file
     * Arranges all the components of the GUI
     * @since jsXe 0.3pre15
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        iconJLabel = new javax.swing.JLabel();

        //setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(12,12));
        content.setBorder(new EmptyBorder(12,12,12,12));
        setContentPane(content);

        iconJLabel.setText(Messages.getMessage("ActivityLogDialog.Dialog.Message"));
        iconJLabel.setIcon(new javax.swing.ImageIcon(DirtyFilesDialog.class
				.getResource("/net/sourceforge/jsxe/icons/metal-Inform.png")));
        getContentPane().add(iconJLabel, BorderLayout.NORTH);
        
        getContentPane().add(activityLogJScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		
		buttonPanel.add(Box.createGlue());
		close = new JButton(Messages.getMessage("common.close"));
		close.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okayJButtonActionPerformed(evt);
			}
		});
        
        getRootPane().setDefaultButton(close);
		buttonPanel.add(close);
		buttonPanel.add(Box.createGlue());
		getContentPane().add(BorderLayout.SOUTH,buttonPanel);
        
    }//}}}
    
    // {{{ okayJButtonActionPerformed()
    /**
     * Provides action for clicking on the OK button
     * @since jsXe 0.3pre15
     */
	private void okayJButtonActionPerformed(java.awt.event.ActionEvent evt) {
		cancel();
	}//}}}
	
    // {{{ getActivityLogContents()
    /**
     * Gets contents of the ativity log jsxe.log
     * @return ArrayList containing lines from the activity log
     * @since jsXe 0.3pre15
     */	
	public ArrayList getActivityLogContents() {
		String homeDir = System.getProperty("user.home");
		File activityLog = new File(homeDir+ System.getProperty("file.separator")+".jsxe"+System.getProperty("file.separator")+"jsXe.log");
			
		String line;
		ArrayList logContents = new ArrayList();
		try {
			BufferedReader reader = new BufferedReader( new FileReader(activityLog));			
			try {
				while ((line = reader.readLine()) != null) {
					logContents.add(line);
				}
                reader.close();
			} catch (IOException e1) {
				Log.log(Log.ERROR, this, e1);
			}
            
		} catch (FileNotFoundException e) {
			Log.log(Log.ERROR, this, e);
		}
		return logContents;		
    }//}}}
	 	
	// {{{ ok()
	public void ok() {
		cancel();
	}//}}}

	//{{{ cancel()
	public void cancel() {
		saveGeometry(this, m_geometryName);
		dispose();
	}//}}}
	
    //{{{ getContents()
	public ListModel getContents(){
		ArrayList contents = getActivityLogContents();
		
		DefaultListModel contentsJListModel = new DefaultListModel();
		JTextArea newArea = new JTextArea(5, 30);
		for (Iterator it=contents.iterator(); it.hasNext(); ) {
			String line = (String)it.next();	
			contentsJListModel.addElement(line);
		}
        return contentsJListModel;
	}//}}}
	
    //{{{ refreshContents()
	public void refreshContents() {
        Log.flushStream();
		contentsJList.setModel(getContents());
		//contentsJList.updateUI();
	}//}}}

    //{{{ getContentsList()
	/**
	 * @return Returns the contentsJList.
	 */
	public JList getContentsJList() {
		return contentsJList;
	}//}}}
    
    //{{{ setContentsJList()
	/**
	 * @param contentsJList The contentsJList to set.
	 */
	public void setContentsJList(JList contentsJList) {
		this.contentsJList = contentsJList;
	}//}}}
}


