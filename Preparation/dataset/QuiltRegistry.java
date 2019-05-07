/* QuiltRegistry.java */
package org.quilt.reg;

import org.quilt.cl.*;

/**
 * A registry for use by Quilt class loaders.  Adds transformers to the
 * class loader prior to use.  Stores information collected while loading
 * classes under a String array key.  Dumps that information on request.
 *
 * @author <a href="jddixon@users.sourceforge.net">Jim Dixon</a>
 */
public abstract class QuiltRegistry extends Registry {

    /** List of class transformers associated with this registry. */
    protected ClassXformer cxf[];

    /** List of method transformers associated with this registry. */
    protected MethodXformer mxf[];

    /** List of graph transformers associated with this registry. */
    protected GraphXformer gxf[];

    /** The Quilt class loader using this registry. */
    protected QuiltClassLoader qcl_;

    public QuiltRegistry ( QuiltClassLoader qcl ) {
        super();
        if (qcl == null) {
            throw new IllegalArgumentException("null QuiltClassLoader");
        }
        qcl_ = qcl;
    }

    /**
     * Attaches transformers to the class loader; classes extending
     * this class should call in the constructor after setting up the
     * lists.
     */
    public final void setTransformers () {
        if (cxf != null) {
            for (int i = 0; i < cxf.length; i++) {
                qcl_.addClassXformer(cxf[i]);
            }
        }
        if (mxf != null) {
            for (int i = 0; i < mxf.length; i++) {
                qcl_.addMethodXformer(mxf[i]);
            }
        }
        if (gxf != null) {
            for (int i = 0; i < gxf.length; i++) {
                qcl_.addGraphXformer(gxf[i]);
            }
        }
    }
    /** Resets items in the registry in an application-specific manner. */
    abstract public void reset();

    /** Dumps the registry in a form appropriate to the application. */
    abstract public String getReport ();

}
