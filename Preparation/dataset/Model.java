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
package org.java.plugin.registry.xml;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.java.plugin.registry.ExtensionMultiplicity;
import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.ParameterMultiplicity;
import org.java.plugin.registry.ParameterType;
import org.java.plugin.registry.Version;

/**
 * @version $Id: Model.java,v 1.4 2007/03/03 17:16:26 ddimon Exp $
 */
abstract class ModelPluginManifest {
    private URL location;
    private String id;
    private Version version;
    private String vendor;
    private String docsPath;
    private ModelDocumentation documentation;
    private LinkedList<ModelAttribute> attributes =
        new LinkedList<ModelAttribute>();
    private LinkedList<ModelPrerequisite> prerequisites =
        new LinkedList<ModelPrerequisite>();
    private LinkedList<ModelLibrary> libraries =
        new LinkedList<ModelLibrary>();
    private LinkedList<ModelExtensionPoint> extensionPoints =
        new LinkedList<ModelExtensionPoint>();
    private LinkedList<ModelExtension> extensions =
        new LinkedList<ModelExtension>();
    
    URL getLocation() {
        return location;
    }
    
    void setLocation(final URL value) {
        location = value;
    }
    
    String getDocsPath() {
        return docsPath;
    }
    
    void setDocsPath(final String value) {
        docsPath = value;
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    String getVendor() {
        return vendor;
    }
    
    void setVendor(final String value) {
        vendor = value;
    }
    
    Version getVersion() {
        return version;
    }
    
    void setVersion(final String value) {
        version = Version.parse(value);
    }
    
    List<ModelAttribute> getAttributes() {
        return attributes;
    }
    
    List<ModelExtensionPoint> getExtensionPoints() {
        return extensionPoints;
    }
    
    List<ModelExtension> getExtensions() {
        return extensions;
    }
    
    List<ModelLibrary> getLibraries() {
        return libraries;
    }
    
    List<ModelPrerequisite> getPrerequisites() {
        return prerequisites;
    }
}

final class ModelPluginDescriptor extends ModelPluginManifest {
    private String className;
    
    ModelPluginDescriptor() {
       // no-op
    }
    
    String getClassName() {
        return className;
    }
    
    void setClassName(final String value) {
        className = value;
    }
}

final class ModelPluginFragment extends ModelPluginManifest {
    private String pluginId;
    private Version pluginVersion;
    private MatchingRule matchingRule = MatchingRule.COMPATIBLE;
    
    ModelPluginFragment() {
        // no-op
    }
    
    MatchingRule getMatchingRule() {
        return matchingRule;
    }
    
    void setMatchingRule(final MatchingRule value) {
        matchingRule = value;
    }
    
    String getPluginId() {
        return pluginId;
    }
    
    void setPluginId(final String value) {
        pluginId = value;
    }
    
    Version getPluginVersion() {
        return pluginVersion;
    }
    
    void setPluginVersion(final String value) {
        pluginVersion = Version.parse(value);
    }
}

final class ModelDocumentation {
    private LinkedList<ModelDocumentationReference> references =
        new LinkedList<ModelDocumentationReference>();
    private String caption;
    private String text;
    
    ModelDocumentation() {
        // no-op
    }
    
    String getCaption() {
        return caption;
    }
    
    void setCaption(final String value) {
        caption = value;
    }
    
    String getText() {
        return text;
    }
    
    void setText(final String value) {
        text = value;
    }
    
    List<ModelDocumentationReference> getReferences() {
        return references;
    }
}

final class ModelDocumentationReference {
    private String path;
    private String caption;
    
    ModelDocumentationReference() {
        // no-op
    }
    
    String getCaption() {
        return caption;
    }
    
    void setCaption(final String value) {
        caption = value;
    }
    
    String getPath() {
        return path;
    }
    
    void setPath(final String value) {
        path = value;
    }
}

final class ModelAttribute {
    private String id;
    private String value;
    private ModelDocumentation documentation;
    private LinkedList<ModelAttribute> attributes =
        new LinkedList<ModelAttribute>();
    
    ModelAttribute() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation aValue) {
        documentation = aValue;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String aValue) {
        id = aValue;
    }
    
    String getValue() {
        return value;
    }
    
    void setValue(final String aValue) {
        value = aValue;
    }
    
    List<ModelAttribute> getAttributes() {
        return attributes;
    }
}

final class ModelPrerequisite {
    private String id;
    private String pluginId;
    private Version pluginVersion;
    private MatchingRule matchingRule = MatchingRule.COMPATIBLE;
    private ModelDocumentation documentation;
    private boolean isExported;
    private boolean isOptional;
    private boolean isReverseLookup;
    
    ModelPrerequisite() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    boolean isExported() {
        return isExported;
    }
    
    void setExported(final String value) {
        isExported = "true".equals(value); //$NON-NLS-1$
    }
    
    boolean isOptional() {
        return isOptional;
    }
    
    void setOptional(final String value) {
        isOptional = "true".equals(value); //$NON-NLS-1$
    }
    
    boolean isReverseLookup() {
        return isReverseLookup;
    }
    
    void setReverseLookup(final String value) {
        isReverseLookup = "true".equals(value); //$NON-NLS-1$
    }
    
    MatchingRule getMatchingRule() {
        return matchingRule;
    }
    
    void setMatchingRule(final MatchingRule value) {
        matchingRule = value;
    }
    
    String getPluginId() {
        return pluginId;
    }
    
    void setPluginId(final String value) {
        pluginId = value;
    }
    
    Version getPluginVersion() {
        return pluginVersion;
    }
    
    void setPluginVersion(final String value) {
        pluginVersion = Version.parse(value);
    }
}

final class ModelLibrary {
    private String id;
    private String path;
    private boolean isCodeLibrary;
    private ModelDocumentation documentation;
    private LinkedList<String> exports = new LinkedList<String>();
    private Version version;
    
    ModelLibrary() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    boolean isCodeLibrary() {
        return isCodeLibrary;
    }
    
    void setCodeLibrary(final String value) {
        isCodeLibrary = "code".equals(value); //$NON-NLS-1$
    }
    
    String getPath() {
        return path;
    }
    
    void setPath(final String value) {
        path = value;
    }
    
    List<String> getExports() {
        return exports;
    }
    
    Version getVersion() {
        return version;
    }
    
    void setVersion(final String value) {
        version = Version.parse(value);
    }
}

final class ModelExtensionPoint {
    private String id;
    private String parentPluginId;
    private String parentPointId;
    private ExtensionMultiplicity extensionMultiplicity =
        ExtensionMultiplicity.ONE;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameterDef> paramDefs =
        new LinkedList<ModelParameterDef>();

    ModelExtensionPoint() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    ExtensionMultiplicity getExtensionMultiplicity() {
        return extensionMultiplicity;
    }
    
    void setExtensionMultiplicity(final ExtensionMultiplicity value) {
        extensionMultiplicity = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    String getParentPluginId() {
        return parentPluginId;
    }
    
    void setParentPluginId(final String value) {
        parentPluginId = value;
    }
    
    String getParentPointId() {
        return parentPointId;
    }
    
    void setParentPointId(final String value) {
        parentPointId = value;
    }
    
    List<ModelParameterDef> getParamDefs() {
        return paramDefs;
    }
}

final class ModelParameterDef {
    private String id;
    private ParameterMultiplicity multiplicity = ParameterMultiplicity.ONE;
    private ParameterType type = ParameterType.STRING;
    private String customData;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameterDef> paramDefs =
        new LinkedList<ModelParameterDef>();
    private String defaultValue;
    
    ModelParameterDef() {
        // no-op
    }
    
    String getCustomData() {
        return customData;
    }
    
    void setCustomData(final String value) {
        customData = value;
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    ParameterMultiplicity getMultiplicity() {
        return multiplicity;
    }
    
    void setMultiplicity(final ParameterMultiplicity value) {
        multiplicity = value;
    }
    
    ParameterType getType() {
        return type;
    }
    
    void setType(final ParameterType value) {
        type = value;
    }
    
    List<ModelParameterDef> getParamDefs() {
        return paramDefs;
    }
    
    String getDefaultValue() {
        return defaultValue;
    }
    
    void setDefaultValue(final String value) {
        defaultValue = value;
    }
}

final class ModelExtension {
    private String id;
    private String pluginId;
    private String pointId;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameter> params =
        new LinkedList<ModelParameter>();
    
    ModelExtension() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    String getPluginId() {
        return pluginId;
    }
    
    void setPluginId(final String value) {
        pluginId = value;
    }
    
    String getPointId() {
        return pointId;
    }
    
    void setPointId(final String value) {
        pointId = value;
    }
    
    List<ModelParameter> getParams() {
        return params;
    }
}

final class ModelParameter {
    private String id;
    private String value;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameter> params =
        new LinkedList<ModelParameter>();
    
    ModelParameter() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation aValue) {
        documentation = aValue;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String aValue) {
        id = aValue;
    }
    
    String getValue() {
        return value;
    }
    
    void setValue(final String aValue) {
        value = aValue;
    }
    
    List<ModelParameter> getParams() {
        return params;
    }
}

final class ModelManifestInfo {
    private String id;
    private Version version;
    private String vendor;
    private String pluginId;
    private Version pluginVersion;
    private MatchingRule matchingRule = MatchingRule.COMPATIBLE;
    
    ModelManifestInfo() {
        // no-op
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    String getVendor() {
        return vendor;
    }
    
    void setVendor(final String value) {
        vendor = value;
    }
    
    Version getVersion() {
        return version;
    }
    
    void setVersion(final String value) {
        version = Version.parse(value);
    }
    
    MatchingRule getMatchRule() {
        return matchingRule;
    }
    
    void setMatchingRule(final MatchingRule value) {
        matchingRule = value;
    }
    
    String getPluginId() {
        return pluginId;
    }
    
    void setPluginId(final String value) {
        pluginId = value;
    }
    
    Version getPluginVersion() {
        return pluginVersion;
    }
    
    void setPluginVersion(final String value) {
        pluginVersion = Version.parse(value);
    }
}
