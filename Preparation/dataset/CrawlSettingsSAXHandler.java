/*
 * CrawlSettingsSAXHandler
 *
 * $Id: CrawlSettingsSAXHandler.java 5111 2007-05-03 01:43:43Z gojomo $
 *
 * Created on Dec 8, 2003
 *
 * Copyright (C) 2004 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or any later version.
 *
 * Heritrix is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along with
 * Heritrix; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.archive.crawler.settings;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;

import org.archive.crawler.settings.Constraint.FailedCheck;
import org.archive.crawler.settings.refinements.PortnumberCriteria;
import org.archive.crawler.settings.refinements.Refinement;
import org.archive.crawler.settings.refinements.RegularExpressionCriteria;
import org.archive.crawler.settings.refinements.TimespanCriteria;
import org.archive.util.ArchiveUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * An SAX element handler that updates a CrawlerSettings object.
 *
 * This is a helper class for the XMLSettingsHandler.
 *
 * @author John Erik Halse
 */
public class CrawlSettingsSAXHandler extends DefaultHandler implements
        ValueErrorHandler {

    private static Logger logger = Logger
            .getLogger("org.archive.crawler.settings.XMLSettingsHandler");

    private Locator locator;

    private CrawlerSettings settings;

    private SettingsHandler settingsHandler;

    private Map<String,ElementHandler> handlers
     = new HashMap<String,ElementHandler>();

    private Stack<ElementHandler> handlerStack = new Stack<ElementHandler>();

    private Stack<Object> stack = new Stack<Object>();

    /** Keeps track of elements which subelements should be skipped. */
    private Stack<Boolean> skip = new Stack<Boolean>();

    private StringBuffer buffer = new StringBuffer();

    private String value;

    /**
     * Creates a new CrawlSettingsSAXHandler.
     *
     * @param settings the settings object that should be updated from this
     *            handler.
     */
    public CrawlSettingsSAXHandler(CrawlerSettings settings) {
        super();
        this.settings = settings;
        this.settingsHandler = settings.getSettingsHandler();
        handlers.put(XMLSettingsHandler.XML_ROOT_ORDER, new RootHandler());
        handlers.put(XMLSettingsHandler.XML_ROOT_HOST_SETTINGS,
                new RootHandler());
        handlers.put(XMLSettingsHandler.XML_ROOT_REFINEMENT, new RootHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_CONTROLLER,
                new ModuleHandler());
        handlers
                .put(XMLSettingsHandler.XML_ELEMENT_OBJECT, new ModuleHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_NEW_OBJECT,
                new NewModuleHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_META, new MetaHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_NAME, new NameHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_DESCRIPTION,
                new DescriptionHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_OPERATOR,
                new OperatorHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_ORGANIZATION,
                new OrganizationHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_AUDIENCE,
                new AudienceHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_DATE, new DateHandler());
        handlers.put(SettingsHandler.MAP, new MapHandler());
        handlers.put(SettingsHandler.INTEGER_LIST, new ListHandler());
        handlers.put(SettingsHandler.STRING_LIST, new ListHandler());
        handlers.put(SettingsHandler.DOUBLE_LIST, new ListHandler());
        handlers.put(SettingsHandler.FLOAT_LIST, new ListHandler());
        handlers.put(SettingsHandler.LONG_LIST, new ListHandler());
        handlers.put(SettingsHandler.STRING, new SimpleElementHandler());
        handlers.put(SettingsHandler.TEXT, new SimpleElementHandler());
        handlers.put(SettingsHandler.INTEGER, new SimpleElementHandler());
        handlers.put(SettingsHandler.FLOAT, new SimpleElementHandler());
        handlers.put(SettingsHandler.LONG, new SimpleElementHandler());
        handlers.put(SettingsHandler.BOOLEAN, new SimpleElementHandler());
        handlers.put(SettingsHandler.DOUBLE, new SimpleElementHandler());

        handlers.put(XMLSettingsHandler.XML_ELEMENT_REFINEMENTLIST,
                new RefinementListHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_REFINEMENT,
                new RefinementHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_REFERENCE,
                new ReferenceHandler());
        handlers
                .put(XMLSettingsHandler.XML_ELEMENT_LIMITS, new LimitsHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_TIMESPAN,
                new TimespanHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_PORTNUMBER,
                new PortnumberHandler());
        handlers.put(XMLSettingsHandler.XML_ELEMENT_URIMATCHES,
                new URIMatcherHandler());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        settingsHandler.registerValueErrorHandler(this);
        skip.push(new Boolean(false));
        super.startDocument();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        settingsHandler.unregisterValueErrorHandler(this);
        super.endDocument();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        buffer.append(ch, start, length);
    }

    /**
     * Start of an element. Decide what handler to use, and call it.
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        ElementHandler handler = ((ElementHandler) handlers.get(qName));
        if (handler != null) {
            handlerStack.push(handler);

            if (((Boolean) skip.peek()).booleanValue()) {
                skip.push(new Boolean(true));
                String moduleName = attributes
                        .getValue(XMLSettingsHandler.XML_ATTRIBUTE_NAME);
                logger.fine("Skipping: " + qName + " " + moduleName);
            } else {
                try {
                    handler.startElement(qName, attributes);
                    skip.push(new Boolean(false));
                } catch (SAXException e) {
                    if (e.getException() instanceof InvocationTargetException
                            || e.getException() instanceof AttributeNotFoundException) {
                        skip.push(new Boolean(true));
                    } else {
                        skip.push(new Boolean(false));
                        throw e;
                    }
                }
            }
        } else {
            String tmp = "Unknown element '" + qName + "' in '" +
                locator.getSystemId() + "', line: " + locator.getLineNumber() +
                ", column: " + locator.getColumnNumber();
            if (this.settingsHandler.getOrder() != null &&
                    this.settingsHandler.getOrder().getController() !=  null) {
                logger.log(Level.WARNING, tmp);
            }
            logger.warning(tmp);
        }
    }

    /**
     * End of an element.
     *
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        value = buffer.toString().trim();
        buffer.setLength(0);
        ElementHandler handler = (ElementHandler) handlerStack.pop();
        if (!((Boolean) skip.pop()).booleanValue()) {
            if (handler != null) {
                handler.endElement(qName);
            }
        }
    }

    public void illegalElementError(String name) throws SAXParseException {
        throw new SAXParseException("Element '" + name + "' not allowed here",
                locator);
    }

    /**
     * Superclass of all the elementhandlers.
     *
     * This class should be subclassed for the different XML-elements.
     *
     * @author John Erik Halse
     */
    private class ElementHandler {

        /**
         * Start of an element
         *
         * @param name
         * @param atts
         * @throws SAXException
         */
        public void startElement(String name, Attributes atts)
                throws SAXException {
        }

        /**
         * End of an element
         *
         * @param name
         * @throws SAXException
         */
        public void endElement(String name) throws SAXException {
        }
    }

    /**
     * Handle the root element.
     *
     * This class checks that the root element is of the right type.
     *
     * @author John Erik Halse
     */
    private class RootHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            //  Check filetype
            if ((name.equals(XMLSettingsHandler.XML_ROOT_ORDER) && settings
                    .getScope() != null)
                    || (name.equals(XMLSettingsHandler.XML_ROOT_HOST_SETTINGS) && settings
                            .getScope() == null)
                    || (name.equals(XMLSettingsHandler.XML_ROOT_REFINEMENT) && !settings
                            .isRefinement())) {
                throw new SAXParseException("Wrong document type '" + name
                        + "'", locator);
            }
        }
    }

    // Meta handlers
    private class MetaHandler extends ElementHandler {
    }

    private class NameHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof MetaHandler) {
                settings.setName(value);
            } else {
                illegalElementError(name);
            }
        }
    }

    private class DescriptionHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof MetaHandler) {
                settings.setDescription(value);
            } else if (handlerStack.peek() instanceof RefinementHandler) {
                ((Refinement) stack.peek()).setDescription(value);
            } else {
                illegalElementError(name);
            }
        }
    }

    private class OrganizationHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof MetaHandler) {
                settings.setOrganization(value);
            } else if (handlerStack.peek() instanceof RefinementHandler) {
                ((Refinement) stack.peek()).setOrganization(value);
            } else {
                illegalElementError(name);
            }
        }
    }

    private class OperatorHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof MetaHandler) {
                settings.setOperator(value);
            } else if (handlerStack.peek() instanceof RefinementHandler) {
                ((Refinement) stack.peek()).setOperator(value);
            } else {
                illegalElementError(name);
            }
        }
    }

    private class AudienceHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof MetaHandler) {
                settings.setAudience(value);
            } else if (handlerStack.peek() instanceof RefinementHandler) {
                ((Refinement) stack.peek()).setAudience(value);
            } else {
                illegalElementError(name);
            }
        }
    }

    private class DateHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof MetaHandler) {
                try {
                    settings.setLastSavedTime(ArchiveUtils
                            .parse14DigitDate(value));
                } catch (ParseException e) {
                    throw new SAXException(e);
                }
            } else {
                illegalElementError(name);
            }
        }
    }

    // Refinement handlers
    private class RefinementListHandler extends ElementHandler {

        public void startElement(String name) throws SAXException {
            if (!(handlerStack.peek() instanceof RootHandler)) {
                illegalElementError(name);
            }
        }
    }

    private class RefinementHandler extends ElementHandler {
        public void startElement(String name, Attributes atts)
                throws SAXException {
            stack.push(new Refinement(settings, atts
                    .getValue(XMLSettingsHandler.XML_ELEMENT_REFERENCE)));
        }
    }

    private class ReferenceHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof RefinementHandler) {
                ((Refinement) stack.peek()).setReference(value);
            } else {
                illegalElementError(name);
            }
        }
    }

    private class LimitsHandler extends ElementHandler {
    }

    private class TimespanHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            if (stack.peek() instanceof Refinement) {
                String from = atts
                        .getValue(XMLSettingsHandler.XML_ATTRIBUTE_FROM);
                String to = atts.getValue(XMLSettingsHandler.XML_ATTRIBUTE_TO);
                try {
                    TimespanCriteria timespan = new TimespanCriteria(from, to);
                    ((Refinement) stack.peek()).addCriteria(timespan);
                } catch (ParseException e) {
                    throw new SAXException(e);
                }
            } else {
                illegalElementError(name);
            }
        }
    }

    private class PortnumberHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof LimitsHandler) {
                ((Refinement) stack.peek()).addCriteria(new PortnumberCriteria(value));
            } else {
                illegalElementError(name);
            }
        }
    }

    private class URIMatcherHandler extends ElementHandler {

        public void endElement(String name) throws SAXException {
            if (handlerStack.peek() instanceof LimitsHandler) {
                ((Refinement) stack.peek()).addCriteria(new RegularExpressionCriteria(value));
            } else {
                illegalElementError(name);
            }
        }
    }


    // Handlers for objects and attributes
    private class ModuleHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            ModuleType module;
            if (name.equals(XMLSettingsHandler.XML_ELEMENT_CONTROLLER)) {
                module = settingsHandler.getOrder();
            } else {
                module = settingsHandler.getSettingsObject(null).getModule(
                        atts.getValue(XMLSettingsHandler.XML_ATTRIBUTE_NAME));
            }
            stack.push(module);
        }

        public void endElement(String name) throws SAXException {
            stack.pop();
        }
    }

    private class NewModuleHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            ComplexType parentModule = (ComplexType) stack.peek();
            String moduleName = atts
                    .getValue(XMLSettingsHandler.XML_ATTRIBUTE_NAME);
            String moduleClass = atts
                    .getValue(XMLSettingsHandler.XML_ATTRIBUTE_CLASS);
            try {
                ModuleType module = SettingsHandler
                        .instantiateModuleTypeFromClassName(moduleName,
                                moduleClass);
                try {
                    parentModule.setAttribute(settings, module);
                } catch (AttributeNotFoundException e) {
                    // Attribute was not found, but the complex type might
                    // be a MapType and then we are allowed to add new
                    // elements.
                    try {
                        parentModule.addElement(settings, module);
                    } catch (IllegalStateException ise) {
                        // An attribute in the settings file is not in the
                        // ComplexType's definition, log and skip.
                        logger.log(Level.WARNING,"Module '" + moduleName + "' in '"
                                + locator.getSystemId() + "', line: "
                                + locator.getLineNumber() + ", column: "
                                + locator.getColumnNumber()
                                + " is not defined in '"
                                + parentModule.getName() + "'.");
                        throw new SAXException(new AttributeNotFoundException(
                                ise.getMessage()));
                    }
                }
                stack.push(module);
            } catch (InvocationTargetException e) {
                logger.log(Level.WARNING,"Couldn't instantiate " + moduleName
                        + ", from class: " + moduleClass + "' in '"
                        + locator.getSystemId() + "', line: "
                        + locator.getLineNumber() + ", column: "
                        + locator.getColumnNumber(), e);
                throw new SAXException(e);
            } catch (InvalidAttributeValueException e) {
                throw new SAXException(e);
            }
        }

        public void endElement(String name) throws SAXException {
            stack.pop();
        }
    }

    private class MapHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            String mapName = atts
                    .getValue(XMLSettingsHandler.XML_ATTRIBUTE_NAME);
            ComplexType parentModule = (ComplexType) stack.peek();
            try {
                stack.push(parentModule.getAttribute(settings, mapName));
            } catch (AttributeNotFoundException e) {
                throw new SAXException(e);
            }
        }

        public void endElement(String name) throws SAXException {
            stack.pop();
        }
    }

    private class SimpleElementHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            stack.push(atts.getValue(XMLSettingsHandler.XML_ATTRIBUTE_NAME));
        }

        public void endElement(String name) throws SAXException {
            String elementName = (String) stack.pop();
            Object container = stack.peek();
            if (container instanceof ComplexType) {
                try {
                    try {
                        ((ComplexType) container).setAttribute(settings,
                                new Attribute(elementName, value));
                    } catch (AttributeNotFoundException e) {
                        // Attribute was not found, but the complex type might
                        // be a MapType and then we are allowed to add new
                        // elements.
                        try {
                            ((ComplexType) container).addElement(settings,
                                    new SimpleType(elementName, "", value));
                        } catch (IllegalStateException ise) {
                            logger.warning("Unknown attribute '" + elementName
                                    + "' in '" + locator.getSystemId()
                                    + "', line: " + locator.getLineNumber()
                                    + ", column: " + locator.getColumnNumber());
                        }
                    }
                } catch (InvalidAttributeValueException e) {
                    try {
                        logger.warning("Illegal value '"
                                + value
                                + "' for attribute '"
                                + elementName
                                + "' in '"
                                + locator.getSystemId()
                                + "', line: "
                                + locator.getLineNumber()
                                + ", column: "
                                + locator.getColumnNumber()
                                + ", Value reset to default value: "
                                + ((ComplexType) container).getAttribute(
                                        settings, elementName));
                    } catch (AttributeNotFoundException e1) {
                        throw new SAXException(e1);
                    }
                }
            } else {
                if (container == null) {
                	// We can get here if an override is referring to a global
                    // filter since removed.  Log it as severe; operator will
                    // probably want to know of all overrides with references
                    // to a global filter since removed.
                    logger.severe("Empty container (Was a referenced parent" +
                        " filter removed?).  Element details: elementName " +
                        elementName + ", name " + name);
                } else {
                	((ListType) container).add(value);
                }
            }
        }
    }

    private class ListHandler extends ElementHandler {

        public void startElement(String name, Attributes atts)
                throws SAXException {
            String listName = atts
                    .getValue(XMLSettingsHandler.XML_ATTRIBUTE_NAME);
            ComplexType parentModule = (ComplexType) stack.peek();
            ListType list;
            try {
                list = (ListType) parentModule.getAttribute(settings, listName);
            } catch (AttributeNotFoundException e) {
                throw new SAXException(e);
            }
            list.clear();
            stack.push(list);
        }

        public void endElement(String name) throws SAXException {
            stack.pop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.archive.crawler.settings.ValueErrorHandler#handleValueError(org.archive.crawler.settings.Constraint.FailedCheck)
     */
    public void handleValueError(FailedCheck error) {
        logger.warning(error.getMessage() + "\n Attribute: '"
                + error.getOwner().getName() + ":"
                + error.getDefinition().getName() + "'\n Value:     '" + value
                + "'\n File:      '" + locator.getSystemId() + "', line: "
                + locator.getLineNumber() + ", column: "
                + locator.getColumnNumber());
    }
}
