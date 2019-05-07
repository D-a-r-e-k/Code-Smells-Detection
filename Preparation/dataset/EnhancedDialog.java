/*
EnhancedDialog.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

Copyright (C) 1998, 1999, 2001 Slava Pestov
Portions Copyright (C) 2002 Ian Lewis (IanLewis@member.fsf.org)

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

import net.sourceforge.jsxe.jsXe;
import net.sourceforge.jsxe.OperatingSystem;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * A dialog box that handles window closing, the ENTER key and the ESCAPE
 * key for you. All you have to do is implement ok() (called when
 * Enter is pressed) and cancel() (called when Escape is pressed, or window
 * is closed).
 * @author Slava Pestov
 * @author Ian Lewis (<a href="mailto:IanLewis@member.fsf.org">IanLewis@member.fsf.org</a>)
 * @version $Id: EnhancedDialog.java,v 1.5 2005/04/15 20:00:53 ian_lewis Exp $
 */
public abstract class EnhancedDialog extends JDialog {
    
    //{{{ EnhancedDialog constructor
    
    public EnhancedDialog(Frame parent, String title, boolean modal) {
        super(parent,title,modal);
        _init();
    }//}}}
    
    //{{{ EnhancedDialog constructor
    
    public EnhancedDialog(Dialog parent, String title, boolean modal) {
        super(parent,title,modal);
        _init();
    }//}}}
    
    //{{{ loadGeometry()
    /**
     * Loads a windows's geometry from the properties.
     * The geometry is loaded from the <code><i>name</i>.x</code>,
     * <code><i>name</i>.y</code>, <code><i>name</i>.width</code> and
     * <code><i>name</i>.height</code> properties.
     *
     * @param win The window
     * @param name The window name
     */
    public static void loadGeometry(Window win, String name) {
        int x, y, width, height;

        Dimension size = win.getSize();
        Dimension screen = win.getToolkit().getScreenSize();

        width = jsXe.getIntegerProperty(name + ".width",size.width);
        height = jsXe.getIntegerProperty(name + ".height",size.height);

        Component parent = win.getParent();
        if (parent == null) {
            x = (screen.width - width) / 2;
            y = (screen.height - height) / 2;
        } else {
            Rectangle bounds = parent.getBounds();
            x = bounds.x + (bounds.width - width) / 2;
            y = bounds.y + (bounds.height - height) / 2;
        }

        x = jsXe.getIntegerProperty(name + ".x",x);
        y = jsXe.getIntegerProperty(name + ".y",y);

        // Make sure the window is displayed in visible region
        Rectangle osbounds = OperatingSystem.getScreenBounds();
        
        if (x < osbounds.x || x+width > osbounds.width) {
            if (width > osbounds.width)
                width = osbounds.width;
            x = (osbounds.width - width) / 2;
        }
        if (y < osbounds.y || y+height > osbounds.height) {
            if (height >= osbounds.height) {
                height = osbounds.height;
            }
            y = (osbounds.height - height) / 2;
        }

        Rectangle desired = new Rectangle(x,y,width,height);
        win.setBounds(desired);

        if ((win instanceof Frame) && OperatingSystem.hasJava14()) {
            int extState = jsXe.getIntegerProperty(name +   ".extendedState", Frame.NORMAL);

            try {
                java.lang.reflect.Method meth = Frame.class.getMethod("setExtendedState", new Class[] {int.class});

                meth.invoke(win, new Object[] {new Integer(extState)});
            } catch(NoSuchMethodException e) {}
              catch(SecurityException e2) {}
              catch(IllegalAccessException e3) {}
              catch(java.lang.reflect.InvocationTargetException e4) {}
        }
    } //}}}
    
    //{{{ saveGeometry() method
    /**
     * Saves a window's geometry to the properties.
     * The geometry is saved to the <code><i>name</i>.x</code>,
     * <code><i>name</i>.y</code>, <code><i>name</i>.width</code> and
     * <code><i>name</i>.height</code> properties.
     * @param win The window
     * @param name The window name
     */
    public static void saveGeometry(Window win, String name) {
        
        if ((win instanceof Frame) && OperatingSystem.hasJava14()) {
            try {
                java.lang.reflect.Method meth = Frame.class.getMethod("getExtendedState",   new Class[0]);

                Integer extState = (Integer)meth.invoke(win, new Object[0]);

                jsXe.setIntegerProperty(name + ".extendedState", extState.intValue());

                if (extState.intValue() != Frame.NORMAL) {
                    return;
                }
            }
            catch(NoSuchMethodException e) {}
            catch(SecurityException e2) {}
            catch(IllegalAccessException e3) {}
            catch(java.lang.reflect.InvocationTargetException e4) {}
        }

        Rectangle bounds = win.getBounds();
        jsXe.setIntegerProperty(name + ".x",bounds.x);
        jsXe.setIntegerProperty(name + ".y",bounds.y);
        jsXe.setIntegerProperty(name + ".width",bounds.width);
        jsXe.setIntegerProperty(name + ".height",bounds.height);
    } //}}}

    public abstract void ok();
    public abstract void cancel();

    //{{{ Private members
    
    //{{{ init()
    
    private void _init() {
        ((Container)getLayeredPane()).addContainerListener(new ContainerHandler());
        getContentPane().addContainerListener(new ContainerHandler());

        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);
        addWindowListener(new WindowHandler());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }//}}}

    //}}}
    
    //{{{ Protected members
    protected KeyHandler keyHandler;

    //{{{ ContainerHandler class
    /**
     * Recursively adds our key listener to sub-components
     */
    class ContainerHandler extends ContainerAdapter {
        
        //{{{ componentAdded()
        
        public void componentAdded(ContainerEvent evt) {
            componentAdded(evt.getChild());
        }//}}}

        //{{{ componentRemoved()
        
        public void componentRemoved(ContainerEvent evt) {
            componentRemoved(evt.getChild());
        }//}}}

        //{{{ componentAdded()
        
        private void componentAdded(Component comp) {
            comp.addKeyListener(keyHandler);
            if(comp instanceof Container) {
                Container cont = (Container)comp;
                cont.addContainerListener(this);
                Component[] comps = cont.getComponents();
                for(int i = 0; i < comps.length; i++) {
                    componentAdded(comps[i]);
                }
            }
        }//}}}

        //{{{ componentRemoved()
        
        private void componentRemoved(Component comp) {
            comp.removeKeyListener(keyHandler);
            if(comp instanceof Container) {
                Container cont = (Container)comp;
                cont.removeContainerListener(this);
                Component[] comps = cont.getComponents();
                for(int i = 0; i < comps.length; i++) {
                    componentRemoved(comps[i]);
                }
            }
        }//}}}
    }//}}}

    //{{{ KeyHandler class
    class KeyHandler extends KeyAdapter {
        
        //{{{ keyPressed()
        
        public void keyPressed(KeyEvent evt) {
            if(evt.isConsumed()) {
                return;
            }

            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
                // crusty workaround
                Component comp = getFocusOwner();
                while(comp != null) {
                    if(comp instanceof JComboBox) {
                        JComboBox combo = (JComboBox)comp;
                        if(combo.isEditable()) {
                            Object selected = combo.getEditor().getItem();
                            if (selected != null) {
                                combo.setSelectedItem(selected);
                            }
                        }
                        break;
                    }

                    comp = comp.getParent();
                }

                ok();
                evt.consume();
            } else {
                if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancel();
                    evt.consume();
                }
            }
        }//}}}
    }//}}}

    //{{{ WindowHandler class
    
    class WindowHandler extends WindowAdapter {
        
        //{{{ windowClosing()
        
        public void windowClosing(WindowEvent evt) {
            cancel();
        }//}}}
    }//}}}

    //}}}
}
