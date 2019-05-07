/* Extractor
*
* $Id: Extractor.java 4497 2006-08-15 01:31:35Z stack-sf $
*
* Created on Sep 22, 2005
*
* Copyright (C) 2005 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/ 
package org.archive.crawler.extractor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.Processor;

/**
 * Convenience shared superclass for Extractor Processors.
 * 
 * Currently only wraps Extractor-specific extract() action with
 * a StackOverflowError catch/log/proceed handler, so that any
 * extractors that recurse too deep on problematic input will
 * only suffer a local error, and other normal CrawlURI processing
 * can continue. See:
 *  [ 1122836 ] Localize StackOverflowError in Extractors
 *  http://sourceforge.net/tracker/index.php?func=detail&aid=1122836&group_id=73833&atid=539099
 * 
 * This class could also become home to common utility features
 * of extractors, like a running tally of the URIs examined/discovered,
 * etc.
 * 
 * @author gojomo
 */
public abstract class Extractor extends Processor {
    private static final Logger logger = Logger
        .getLogger(Extractor.class.getName());

    /**
     * Passthrough constructor.
     * 
     * @param name
     * @param description
     */
    public Extractor(String name, String description) {
        super(name, description);
        // TODO Auto-generated constructor stub
    }

    public void innerProcess(CrawlURI curi) {
        try {
            extract(curi);
        } catch (NullPointerException npe) {
            // both annotate (to highlight in crawl log) & add as local-error
            curi.addAnnotation("err=" + npe.getClass().getName());
            curi.addLocalizedError(getName(), npe, "");
            // also log as warning
            logger.log(Level.WARNING, getName() + ": NullPointerException",
                npe);
        } catch (StackOverflowError soe) {
            // both annotate (to highlight in crawl log) & add as local-error
            curi.addAnnotation("err=" + soe.getClass().getName());
            curi.addLocalizedError(getName(), soe, "");
            // also log as warning
            logger.log(Level.WARNING, getName() + ": StackOverflowError", soe);
        } catch (java.nio.charset.CoderMalfunctionError cme) {
            // See http://sourceforge.net/tracker/index.php?func=detail&aid=1540222&group_id=73833&atid=539099
            // Both annotate (to highlight in crawl log) & add as local-error
            curi.addAnnotation("err=" + cme.getClass().getName());
            curi.addLocalizedError(getName(), cme, ""); // <-- Message field ignored when logging.
            logger.log(Level.WARNING, getName() + ": CoderMalfunctionError",
                cme);
        }
    }

    protected abstract void extract(CrawlURI curi);
}
