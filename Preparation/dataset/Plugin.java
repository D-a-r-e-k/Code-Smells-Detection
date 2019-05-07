/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2004-2007 Dmitry Olshansky
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
package org.java.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.registry.PluginDescriptor;

/**
 * This is base for "home" class of plug-in runtime. Using this class,
 * plug-in code can get access to plug-in framework
 * ({@link org.java.plugin.PluginManager manager},
 * {@link org.java.plugin.registry.PluginRegistry registry}) which was loaded it.
 * It is also used by manager during plug-in life cycle management (activation
 * and deactivation).
 * <br>
 * Plug-in vendor may provide it's own implementation of this class if some
 * actions should be performed during plug-in activation/deactivation. When no
 * class specified, framework provides default "empty" implementation that does
 * nothing when plug-in started and stopped.
 * @version $Id$
 */
public abstract class Plugin {
    /**
     * Makes logging service available for descending classes.
     */
    protected final Log log = LogFactory.getLog(getClass());

    private PluginManager manager;
    private PluginDescriptor descriptor;
    private boolean started;

    /**
     * @return descriptor of this plug-in
     */
    public final PluginDescriptor getDescriptor() {
        return descriptor;
    }
    
    /*
     * For internal use only!
     */
    final void setDescriptor(final PluginDescriptor descr) {
        this.descriptor = descr;
    }
    
    /**
     * @return manager which controls this plug-in
     */
    public final PluginManager getManager() {
        return manager;
    }
    
    /*
     * For internal use only!
     */
    final void setManager(final PluginManager aManager) {
        this.manager = aManager;
    }

    /*
     * For internal use only!
     */
    final void start() throws Exception {
        if (!started) {
            doStart();
            started = true;
        }
    }

    /*
     * For internal use only!
     */
    final void stop() throws Exception {
        if (started) {
            doStop();
            started = false;
        }
    }

    /**
     * @return <code>true</code> if this plug-in is in active state
     */
    public final boolean isActive() {
        return started;
    }

    /**
     * This method will be called once during plug-in activation before any
     * access to any code from this plug-in.
     * @throws Exception if an error has occurred during plug-in start-up
     */
    protected abstract void doStart() throws Exception;

    /**
     * This method will be called once during plug-in deactivation. After
     * this method call, no other code from this plug-in can be accessed,
     * unless {@link #doStart()} method will be called again (but for another
     * instance of this class).
     * @throws Exception if an error has occurred during plug-in shutdown
     */
    protected abstract void doStop() throws Exception;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{" + getClass().getName() //$NON-NLS-1$
            + ": manager=" + manager //$NON-NLS-1$
            + ", descriptor=" + descriptor //$NON-NLS-1$
            + "}"; //$NON-NLS-1$
    }
}
