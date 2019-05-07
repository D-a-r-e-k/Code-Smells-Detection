/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2006-2007 Dmitry Olshansky
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *****************************************************************************/
package org.java.plugin.boot;

import java.net.URL;

import org.java.plugin.util.ExtendedProperties;

/**
 * Interface to control application splash screen.
 * 
 * @see org.java.plugin.boot.Boot#getSplashHandler()
 * 
 * @version $Id$
 */
public interface SplashHandler {
    /**
     * Configures this handler instance. This method is called ones immediately
     * after handler instantiation.
     * @param config handler configuration data, here included all configuration
     *               parameters which name starts with
     *               <code>org.java.plugin.boot.splash.</code> prefix
     */
    void configure(ExtendedProperties config);
    
    /**
     * @return boot progress value that is normalized to [0; 1] interval
     */
    float getProgress();
    
    /**
     * Sets boot progress value and optionally adjust visual progress bar
     * control. The value should be in [0; 1] interval.
     * @param value new progress value
     */
    void setProgress(float value);
    
    /**
     * @return current text caption
     */
    String getText();
    
    /**
     * Sets new text caption and optionally display it on the screen.
     * @param value new text caption
     */
    void setText(String value);
    
    /**
     * @return current image URL
     */
    URL getImage();
    
    /**
     * Sets new image URL and optionally displays it on the splash screen.
     * @param value new image URL
     */
    void setImage(URL value);
    
    /**
     * @return <code>true</code> if splash screen is displayed now
     */
    boolean isVisible();
    
    /**
     * Shows/hides splash screen.
     * @param value <code>true</code> to show splash screen, <code>false</code>
     *              - to hide and dispose it
     */
    void setVisible(boolean value);
    
    /**
     * Useful method to get access to handler internals. The actually returned
     * object depends on handler implementation.
     * @return original implementation of this handler, usually you return
     *         <code>this</code> (useful for handler wrappers)
     */
    Object getImplementation();
}
