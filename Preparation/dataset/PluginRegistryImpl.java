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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.PathResolver;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.Identity;
import org.java.plugin.registry.IntegrityCheckReport;
import org.java.plugin.registry.ManifestInfo;
import org.java.plugin.registry.ManifestProcessingException;
import org.java.plugin.registry.MatchingRule;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.registry.Version;
import org.java.plugin.registry.IntegrityCheckReport.ReportItem;
import org.java.plugin.registry.xml.IntegrityChecker.ReportItemImpl;
import org.java.plugin.util.ExtendedProperties;

/**
 * This is an implementation of plug-in registry of XML syntax plug-in
 * manifests. Manifests should be prepared according to
 * <a href="{@docRoot}/../plugin_1_0.dtd">plug-in DTD</a>.
 * <p>
 * <b>Configuration parameters</b>
 * <p>
 * This registry implementation supports following configuration parameters:
 * <dl>
 *   <dt>isValidating</dt>
 *   <dd>Regulates is registry should use validating parser when loading
 *     plug-in manifests. The default parameter value is <code>true</code>.</dd>
 *   <dt>stopOnError</dt>
 *   <dd>Regulates is registry should stop and throw RuntimeException if an
 *     error occurred while {@link PluginRegistry#register(URL[]) registering}
 *     or {@link PluginRegistry#unregister(String[]) un-registering} plug-ins.
 *     If this is <code>false</code>, the registration errors will be stored
 *     in the internal report that is available with
 *     {@link PluginRegistry#checkIntegrity(PathResolver)} method.
 *     The default parameter value is <code>false</code>.</dd>
 * </dl>
 * 
 * @see org.java.plugin.ObjectFactory#createRegistry()
 * 
 * @version $Id: PluginRegistryImpl.java,v 1.6 2007/05/13 16:10:51 ddimon Exp $
 */
public final class PluginRegistryImpl implements PluginRegistry {
    static final String PACKAGE_NAME = "org.java.plugin.registry.xml"; //$NON-NLS-1$
    private static final char UNIQUE_SEPARATOR = '@';
    private static final Log log = LogFactory.getLog(PluginRegistryImpl.class);

    private final List<ReportItem> registrationReport = new LinkedList<ReportItem>();
    private final Map<String, PluginDescriptor> registeredPlugins = new HashMap<String, PluginDescriptor>();
    private final Map<String, PluginFragment> registeredFragments = new HashMap<String, PluginFragment>();
    private final List<RegistryChangeListener> listeners = Collections.synchronizedList(new LinkedList<RegistryChangeListener>());
    private ManifestParser manifestParser;
    private boolean stopOnError = false;
    
    /**
     * Creates plug-in registry object.
     */
    public PluginRegistryImpl() {
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR, "registryStart", null)); //$NON-NLS-1$
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#configure(
     *      ExtendedProperties)
     */
    public void configure(final ExtendedProperties config) {
        stopOnError = "true".equalsIgnoreCase( //$NON-NLS-1$
                config.getProperty("stopOnError", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        boolean isValidating = !"false".equalsIgnoreCase( //$NON-NLS-1$
                config.getProperty("isValidating", "true")); //$NON-NLS-1$ //$NON-NLS-2$
        manifestParser = new ManifestParser(isValidating);
        log.info("configured, stopOnError=" + stopOnError //$NON-NLS-1$
                + ", isValidating=" + isValidating); //$NON-NLS-1$
    }
    
    /**
     * @see org.java.plugin.registry.PluginRegistry#readManifestInfo(
     *      java.net.URL)
     */
    public ManifestInfo readManifestInfo(final URL url)
            throws ManifestProcessingException {
        try {
            return new ManifestInfoImpl(manifestParser.parseManifestInfo(url));
        } catch (Exception e) {
            throw new ManifestProcessingException(PACKAGE_NAME,
                        "manifestParsingError", url, e); //$NON-NLS-1$
        }
    }

    /**
     * General algorithm:
     * <ol>
     *   <li>Collect all currently registered extension points.</li>
     *   <li>Parse given URL's as XML content files and separate them on plug-in
     *       and plug-in fragment descriptors.</li>
     *   <li>Process new plug-in descriptors first:
     *     <ol>
     *       <li>Instantiate new PluginDescriptorImpl object.</li>
     *       <li>Handle versions correctly - register new descriptor as most
     *           recent version or as an old version.</li>
     *       <li>If other versions of the same plug-in already registered, take
     *           their fragments and register them with this version.</li>
     *     </ol>
     *   </li>
     *   <li>Process new plug-in fragments next:
     *     <ol>
     *       <li>Instantiate new PluginFragmentImpl object.</li>
     *       <li>Check if older version of the same fragment already registered.
     *           If yes, un-register it and move to old plug-in fragments
     *           collection.</li>
     *       <li>Register new fragment with all matches plug-in descriptors (if
     *           this fragment is of most recent version).</li>
     *      </ol>
     *    </li>
     *    <li>Notify collected extension points about potential changes in
     *        extensions set.</li>
     *    <li>Propagate events about registry changes.</li>
     * </ol>
     * @see org.java.plugin.registry.PluginRegistry#register(java.net.URL[])
     */
    public Map<String, Identity> register(final URL[] manifests)
            throws ManifestProcessingException {
        // collecting registered extension points and extensions
        List<ExtensionPoint> registeredPoints =
            new LinkedList<ExtensionPoint>();
        Map<String, Extension> registeredExtensions =
            new HashMap<String, Extension>();
        for (PluginDescriptor descriptor : registeredPlugins.values()) {
            for (ExtensionPoint point : descriptor.getExtensionPoints()) {
                registeredPoints.add(point);
                for (Extension ext : point.getConnectedExtensions()) {
                    registeredExtensions.put(ext.getUniqueId(), ext);
                }
            }
        }
        Map<String, Identity> result =
            new HashMap<String, Identity>(manifests.length); 
        Map<String, ModelPluginManifest> plugins =
            new HashMap<String, ModelPluginManifest>();
        Map<String, ModelPluginManifest> fragments =
            new HashMap<String, ModelPluginManifest>();
        // parsing given manifests
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR, "manifestsParsingStart", //$NON-NLS-1$
                null));
        for (URL url : manifests) {
            ModelPluginManifest model;
            try {
                model = manifestParser.parseManifest(url);
            } catch (Exception e) {
                log.error("can't parse manifest file " + url, e); //$NON-NLS-1$
                if (stopOnError) {
                    throw new ManifestProcessingException(PACKAGE_NAME,
                            "manifestParsingError", url, e); //$NON-NLS-1$
                }
                registrationReport.add(new ReportItemImpl(
                        IntegrityCheckReport.Severity.ERROR, null,
                        IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED,
                        "manifestParsingError", new Object[] {url, e})); //$NON-NLS-1$
                continue;
            }
            if (model instanceof ModelPluginFragment) {
                fragments.put(url.toExternalForm(), model);
                continue;
            }
            if (!(model instanceof ModelPluginDescriptor)) {
                log.warn("URL " + url //$NON-NLS-1$
                        + " points to XML document of unknown type"); //$NON-NLS-1$
                continue;
            }
            plugins.put(url.toExternalForm(), model);
        }
        if (log.isDebugEnabled()) {
            log.debug("manifest files parsed, plugins.size=" + plugins.size() //$NON-NLS-1$
                    + ", fragments.size=" + fragments.size()); //$NON-NLS-1$
        }
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR, "manifestsParsingFinish", //$NON-NLS-1$
                new Object[] {Integer.valueOf(plugins.size()),
                        Integer.valueOf(fragments.size())}));
        checkVersions(plugins);
        if (log.isDebugEnabled()) {
            log.debug("plug-ins versions checked, plugins.size=" //$NON-NLS-1$
                    + plugins.size());
        }
        checkVersions(fragments);
        if (log.isDebugEnabled()) {
            log.debug("plug-in fragments versions checked, fragments.size=" //$NON-NLS-1$
                    + fragments.size());
        }
        RegistryChangeDataImpl registryChangeData =
            new RegistryChangeDataImpl();
        // registering new plug-ins
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "registeringPluginsStart", null)); //$NON-NLS-1$
        for (ModelPluginManifest model : plugins.values()) {
            PluginDescriptor descr = registerPlugin(
                    (ModelPluginDescriptor) model, registryChangeData);
            if (descr != null) {
                result.put(descr.getLocation().toExternalForm(), descr);
            }
        }
        plugins.clear();
        // registering new plug-in fragments
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "registeringFragmentsStart", null)); //$NON-NLS-1$
        for (ModelPluginManifest entry : fragments.values()) {
            PluginFragment fragment = registerFragment(
                    (ModelPluginFragment) entry, registryChangeData);
            if (fragment != null) {
                result.put(fragment.getLocation().toExternalForm(), fragment);
            }
        }
        fragments.clear();
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "registeringPluginsFinish", //$NON-NLS-1$
                Integer.valueOf(registeredPlugins.size())));
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "registeringFragmentsFinish", //$NON-NLS-1$
                Integer.valueOf(registeredFragments.size())));
        log.info("plug-in and fragment descriptors registered - " //$NON-NLS-1$
                + result.size());
        dump();
        if (result.isEmpty()) {
            return result;
        }
        // notify all interested members that plug-ins set has been changed
        for (ExtensionPoint  extensionPoint : registeredPoints) {
            ((ExtensionPointImpl) extensionPoint).registryChanged();
        }
        for (Extension extension : registeredExtensions.values()) {
            ((ExtensionImpl) extension).registryChanged();
        }
        if (!listeners.isEmpty() || log.isDebugEnabled()) {
            // analyze changes in extensions set
            for (PluginDescriptor pluginDescriptor
                    : registeredPlugins.values()) {
                for (ExtensionPoint extensionPoint
                        : pluginDescriptor.getExtensionPoints()) {
                    for (Extension ext
                            : extensionPoint.getConnectedExtensions()) {
                        if (!registeredExtensions.containsKey(
                                ext.getUniqueId())) {
                            registryChangeData.putAddedExtension(
                                    ext.getUniqueId(),
                                    makeUniqueId(ext.getExtendedPluginId(),
                                            ext.getExtendedPointId()));
                        } else {
                            registeredExtensions.remove(ext.getUniqueId());
                            if (registryChangeData.modifiedPlugins().contains(
                                    ext.getDeclaringPluginDescriptor().getId())
                                    || registryChangeData.modifiedPlugins()
                                        .contains(ext.getExtendedPluginId())) {
                                registryChangeData.putModifiedExtension(
                                        ext.getUniqueId(),
                                        makeUniqueId(ext.getExtendedPluginId(),
                                                ext.getExtendedPointId()));
                            }
                        }
                    }
                }
            }
            for (Extension ext : registeredExtensions.values()) {
                registryChangeData.putRemovedExtension(ext.getUniqueId(),
                        makeUniqueId(ext.getExtendedPluginId(),
                                ext.getExtendedPointId()));
            }
            // fire event
            fireEvent(registryChangeData);
        }
        return result;
    }
    
    private void checkVersions(final Map<String, ModelPluginManifest> plugins)
            throws ManifestProcessingException {
        Map<String, Object[]> versions = new HashMap<String, Object[]>(); //<ID, [Version, URL]>
        Set<String> toBeRemovedUrls = new HashSet<String>();
        for (Iterator<Map.Entry<String, ModelPluginManifest>> it =
                plugins.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, ModelPluginManifest> entry = it.next();
            String url = entry.getKey();
            ModelPluginManifest model = entry.getValue();
            if (registeredPlugins.containsKey(model.getId())) {
                if (stopOnError) {
                    throw new ManifestProcessingException(PACKAGE_NAME,
                            "duplicatePlugin", //$NON-NLS-1$
                            model.getId());
                }
                it.remove();
                registrationReport.add(new ReportItemImpl(
                        IntegrityCheckReport.Severity.ERROR, null,
                        IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED,
                        "duplicatedPluginId", model.getId())); //$NON-NLS-1$
                continue;
            }
            if (registeredFragments.containsKey(model.getId())) {
                if (stopOnError) {
                    throw new ManifestProcessingException(PACKAGE_NAME,
                            "duplicatePluginFragment", //$NON-NLS-1$
                            model.getId());
                }
                it.remove();
                registrationReport.add(new ReportItemImpl(
                        IntegrityCheckReport.Severity.ERROR, null,
                        IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED,
                        "duplicatedFragmentId", model.getId())); //$NON-NLS-1$
                continue;
            }
            Object[] version = versions.get(model.getId());
            if (version == null) {
                versions.put(model.getId(),
                        new Object[] {model.getVersion(), url});
                continue;
            }
            if (((Version) version[0]).compareTo(model.getVersion()) < 0) {
                toBeRemovedUrls.add((String) version[1]);
                versions.put(model.getId(),
                        new Object[] {model.getVersion(), url});
            } else {
                toBeRemovedUrls.add(url);
            }
        }
        versions.clear();
        for (String url : toBeRemovedUrls) {
            plugins.remove(url);
        }
        toBeRemovedUrls.clear();
    }

    private PluginDescriptor registerPlugin(final ModelPluginDescriptor model,
            final RegistryChangeDataImpl registryChangeData)
            throws ManifestProcessingException {
        if (log.isDebugEnabled()) {
            log.debug("registering plug-in, URL - " + model.getLocation()); //$NON-NLS-1$
        }
        PluginDescriptorImpl result = null;
        try {
            result = new PluginDescriptorImpl(this, model);
            registryChangeData.addedPlugins().add(result.getId());
            // applying fragments to the new plug-in
            for (PluginFragment pluginFragment : registeredFragments.values()) {
                PluginFragmentImpl fragment = (PluginFragmentImpl) pluginFragment;
                if (fragment.matches(result)) {
                    result.registerFragment(fragment);
                }
            }
            registrationReport.add(new ReportItemImpl(
                    IntegrityCheckReport.Severity.INFO, null,
                    IntegrityCheckReport.Error.NO_ERROR,
                    "pluginRegistered", result.getUniqueId())); //$NON-NLS-1$
        } catch (ManifestProcessingException mpe) {
            log.error("failed registering plug-in, URL - " //$NON-NLS-1$
                    + model.getLocation(), mpe);
            if (stopOnError) {
                throw mpe;
            }
            registrationReport.add(new ReportItemImpl(
                    IntegrityCheckReport.Severity.ERROR, null,
                    IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED,
                    "pluginRegistrationFailed", //$NON-NLS-1$
                    new Object[] {model.getLocation(), mpe}));
            return null;
        }
        registeredPlugins.put(result.getId(), result);
        return result;
    }

    private PluginFragment registerFragment(final ModelPluginFragment model,
            final RegistryChangeDataImpl registryChangeData)
            throws ManifestProcessingException {
        if (log.isDebugEnabled()) {
            log.debug("registering plug-in fragment descriptor, URL - " //$NON-NLS-1$
                    + model.getLocation());
        }
        PluginFragmentImpl result = null;
        try {
            result = new PluginFragmentImpl(this, model);
            // register fragment with all matches plug-ins
            boolean isRegistered = false;
            PluginDescriptorImpl descr =
                (PluginDescriptorImpl) getPluginDescriptor(
                        result.getPluginId());
            if (result.matches(descr)) {
                descr.registerFragment(result);
                if (!registryChangeData.addedPlugins().contains(
                        descr.getId())) {
                    registryChangeData.modifiedPlugins().add(descr.getId());
                }
                isRegistered = true;
            }
            if (!isRegistered) {
                log.warn("no matching plug-ins found for fragment " //$NON-NLS-1$
                        + result.getUniqueId());
                registrationReport.add(new ReportItemImpl(
                        IntegrityCheckReport.Severity.WARNING, null,
                        IntegrityCheckReport.Error.NO_ERROR,
                        "noMatchingPluginFound", result.getUniqueId())); //$NON-NLS-1$
            }
            registrationReport.add(new ReportItemImpl(
                    IntegrityCheckReport.Severity.INFO, null,
                    IntegrityCheckReport.Error.NO_ERROR,
                    "fragmentRegistered", result.getUniqueId())); //$NON-NLS-1$
        } catch (ManifestProcessingException mpe) {
            log.error("failed registering plug-in fragment descriptor, URL - " //$NON-NLS-1$
                    + model.getLocation(), mpe);
            if (stopOnError) {
                throw mpe;
            }
            registrationReport.add(new ReportItemImpl(
                    IntegrityCheckReport.Severity.ERROR, null,
                    IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED,
                    "fragmentRegistrationFailed", //$NON-NLS-1$
                    new Object[] {model.getLocation(), mpe}));
            return null;
        }
        registeredFragments.put(result.getId(), result);
        return result;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#unregister(java.lang.String[])
     */
    public Collection<String> unregister(final String[] ids) {
        // collecting registered extension points and extensions
        final List<ExtensionPoint> registeredPoints = new LinkedList<ExtensionPoint>();
        final Map<String, Extension> registeredExtensions = new HashMap<String, Extension>();
        for (PluginDescriptor pluginDescriptor : registeredPlugins.values()) {
            for (ExtensionPoint point : pluginDescriptor.getExtensionPoints()) {
                registeredPoints.add(point);
                for (Extension ext : point.getConnectedExtensions()) {
                    registeredExtensions.put(ext.getUniqueId(), ext);
                }
            }
        }
        final Set<String> result = new HashSet<String>();
        RegistryChangeDataImpl registryChangeData =
            new RegistryChangeDataImpl();
        // collect objects to be unregistered
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR, "unregisteringPrepare", //$NON-NLS-1$
                null));
        Map<String, PluginDescriptor> removingPlugins = new HashMap<String, PluginDescriptor>();
        Map<String, PluginFragment> removingFragments = new HashMap<String, PluginFragment>();
        for (String element : ids)
        {
            PluginDescriptor descr = registeredPlugins.get(element);
            if (descr != null) {
                for (PluginDescriptor depDescr : getDependingPlugins(descr)) {
                    removingPlugins.put(depDescr.getId(), depDescr);
                    registryChangeData.removedPlugins().add(depDescr.getId());
                }
                removingPlugins.put(descr.getId(), descr);
                registryChangeData.removedPlugins().add(descr.getId());
                continue;
            }
            PluginFragment fragment = registeredFragments.get(element);
            if (fragment != null) {
                removingFragments.put(fragment.getId(), fragment);
                continue;
            }
            registrationReport.add(new ReportItemImpl(
                    IntegrityCheckReport.Severity.WARNING, null,
                    IntegrityCheckReport.Error.NO_ERROR,
                    "pluginToUngregisterNotFound", element)); //$NON-NLS-1$
        }
        for (PluginDescriptor descr : removingPlugins.values()) {
            for (PluginFragment fragment : descr.getFragments()) {
                if (removingFragments.containsKey(fragment.getId())) {
                    continue;
                }
                removingFragments.put(fragment.getId(), fragment);
            }
        }
        // notify about plug-ins removal first
        fireEvent(registryChangeData);
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "unregisteringFragmentsStart", null)); //$NON-NLS-1$
        for (PluginFragment pluginFragment : removingFragments.values()) {
            PluginFragmentImpl fragment = (PluginFragmentImpl) pluginFragment;
            unregisterFragment(fragment);
            if (!removingPlugins.containsKey(fragment.getPluginId())) {
                registryChangeData.modifiedPlugins().add(
                        fragment.getPluginId());
            }
            result.add(fragment.getUniqueId());
        }
        removingFragments.clear();
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "unregisteringPluginsStart", null)); //$NON-NLS-1$
        for (PluginDescriptor pluginDescriptor : removingPlugins.values()) {
            PluginDescriptorImpl descr = (PluginDescriptorImpl) pluginDescriptor;
            unregisterPlugin(descr);
            result.add(descr.getUniqueId());
        }
        removingPlugins.clear();
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "unregisteringPluginsFinish", //$NON-NLS-1$
                Integer.valueOf(registeredPlugins.size())));
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "unregisteringFragmentsFinish", //$NON-NLS-1$
                Integer.valueOf(registeredFragments.size())));
        log.info("plug-in and fragment descriptors unregistered - " //$NON-NLS-1$
                + result.size());
        dump();
        if (result.isEmpty()) {
            return result;
        }
        // notify all interested members that plug-ins set has been changed
        for (ExtensionPoint extensionPoint : registeredPoints) {
            ((ExtensionPointImpl) extensionPoint).registryChanged();
        }
        for (Extension extension : registeredExtensions.values()) {
            ((ExtensionImpl) extension).registryChanged();
        }
        if (!listeners.isEmpty() || log.isDebugEnabled()) {
            // analyze changes in extensions set
            for (PluginDescriptor descriptor : registeredPlugins.values()) {
                for (ExtensionPoint point : descriptor.getExtensionPoints()) {
                    for (Extension ext : point.getConnectedExtensions()) {
                        if (!registeredExtensions.containsKey(
                                ext.getUniqueId())) {
                            registryChangeData.putAddedExtension(
                                    ext.getUniqueId(),
                                    makeUniqueId(ext.getExtendedPluginId(),
                                            ext.getExtendedPointId()));
                        } else {
                            registeredExtensions.remove(ext.getUniqueId());
                            if (registryChangeData.modifiedPlugins().contains(
                                    ext.getDeclaringPluginDescriptor().getId())
                                    || registryChangeData.modifiedPlugins()
                                        .contains(ext.getExtendedPluginId())) {
                                registryChangeData.putModifiedExtension(
                                        ext.getUniqueId(),
                                        makeUniqueId(ext.getExtendedPluginId(),
                                                ext.getExtendedPointId()));
                            }
                        }
                    }
                }
            }
            for (Extension ext : registeredExtensions.values()) {
                registryChangeData.putRemovedExtension(ext.getUniqueId(),
                        makeUniqueId(ext.getExtendedPluginId(),
                                ext.getExtendedPointId()));
            }
            // fire event
            fireEvent(registryChangeData);
        }
        return result;
    }
    
    private void unregisterPlugin(final PluginDescriptorImpl descr) {
        registeredPlugins.remove(descr.getId());
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "pluginUnregistered", descr.getUniqueId())); //$NON-NLS-1$
    }
    
    private void unregisterFragment(final PluginFragmentImpl fragment) {
        PluginDescriptorImpl descr =
            (PluginDescriptorImpl) registeredPlugins.get(
                    fragment.getPluginId());
        if (descr != null) {
            descr.unregisterFragment(fragment);
        }
        registeredFragments.remove(fragment.getId());
        registrationReport.add(new ReportItemImpl(
                IntegrityCheckReport.Severity.INFO, null,
                IntegrityCheckReport.Error.NO_ERROR,
                "fragmentUnregistered", fragment.getUniqueId())); //$NON-NLS-1$
    }

    private void dump() {
        if (!log.isDebugEnabled()) {
            return;
        }
        StringBuilder buf = new StringBuilder();
        buf.append("PLUG-IN REGISTRY DUMP:\r\n") //$NON-NLS-1$
            .append("-------------- DUMP BEGIN -----------------\r\n") //$NON-NLS-1$
            .append("\tPlug-ins: " + registeredPlugins.size() //$NON-NLS-1$
                    + "\r\n"); //$NON-NLS-1$
        for (PluginDescriptor descriptor : registeredPlugins.values()) {
            buf.append("\t\t") //$NON-NLS-1$
                .append(descriptor)
                .append("\r\n"); //$NON-NLS-1$
        }
        buf.append("\tFragments: " + registeredFragments.size() //$NON-NLS-1$
                + "\r\n"); //$NON-NLS-1$
        for (PluginFragment fragment : registeredFragments.values()) {
            buf.append("\t\t") //$NON-NLS-1$
                .append(fragment)
                .append("\r\n"); //$NON-NLS-1$
        }
        buf.append("Memory TOTAL/FREE/MAX: ") //$NON-NLS-1$
            .append(Runtime.getRuntime().totalMemory())
            .append("/") //$NON-NLS-1$
            .append(Runtime.getRuntime().freeMemory())
            .append("/") //$NON-NLS-1$
            .append(Runtime.getRuntime().maxMemory())
            .append("\r\n"); //$NON-NLS-1$
        buf.append("-------------- DUMP END -----------------\r\n"); //$NON-NLS-1$
        log.debug(buf.toString());
    }
    
    /**
     * @see org.java.plugin.registry.PluginRegistry#getExtensionPoint(
     *      java.lang.String, java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(final String pluginId,
            final String pointId) {
        PluginDescriptor descriptor = registeredPlugins.get(pluginId);
        if (descriptor == null) {
            throw new IllegalArgumentException("unknown plug-in ID " //$NON-NLS-1$
                + pluginId + " provided for extension point " + pointId); //$NON-NLS-1$
        }
        for (ExtensionPoint point : descriptor.getExtensionPoints()) {
            if (point.getId().equals(pointId)) {
                if (point.isValid()) {
                    return point;
                }
                log.warn("extension point " + point.getUniqueId() //$NON-NLS-1$
                        + " is invalid and ignored by registry"); //$NON-NLS-1$
                break;
            }
        }
        throw new IllegalArgumentException("unknown extension point ID - " //$NON-NLS-1$
                + makeUniqueId(pluginId, pointId));
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getExtensionPoint(java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(final String uniqueId) {
        return getExtensionPoint(extractPluginId(uniqueId),
                extractId(uniqueId));
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#isExtensionPointAvailable(
     *      java.lang.String, java.lang.String)
     */
    public boolean isExtensionPointAvailable(final String pluginId,
            final String pointId) {
        PluginDescriptor descriptor = registeredPlugins.get(pluginId);
        if (descriptor == null) {
            return false;
        }
        for (ExtensionPoint point : descriptor.getExtensionPoints()) {
            if (point.getId().equals(pointId)) {
                return point.isValid();
            }
        }
        return false;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#isExtensionPointAvailable(
     *      java.lang.String)
     */
    public boolean isExtensionPointAvailable(final String uniqueId) {
        return isExtensionPointAvailable(extractPluginId(uniqueId),
                extractId(uniqueId));
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getPluginDescriptor(java.lang.String)
     */
    public PluginDescriptor getPluginDescriptor(final String pluginId) {
        PluginDescriptor result =
            registeredPlugins.get(pluginId);
        if (result == null) {
            throw new IllegalArgumentException("unknown plug-in ID - " //$NON-NLS-1$
                    + pluginId);
        }
        return result;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#isPluginDescriptorAvailable(java.lang.String)
     */
    public boolean isPluginDescriptorAvailable(final String pluginId) {
        return registeredPlugins.containsKey(pluginId);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getPluginDescriptors()
     */
    public Collection<PluginDescriptor> getPluginDescriptors() {
        final Collection<PluginDescriptor> empty_collection = Collections.emptyList();
        return registeredPlugins.isEmpty() ? empty_collection
                : Collections.unmodifiableCollection(
                        registeredPlugins.values());
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getPluginFragments()
     */
    public Collection<PluginFragment> getPluginFragments() {
        final Collection<PluginFragment> empty_collection = Collections.emptyList();
        return registeredFragments.isEmpty() ? empty_collection
                : Collections.unmodifiableCollection(registeredFragments.values());
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getDependingPlugins(
     *      org.java.plugin.registry.PluginDescriptor)
     */
    public Collection<PluginDescriptor> getDependingPlugins(
            final PluginDescriptor descr) {
        Map<String, PluginDescriptor> result =
            new HashMap<String, PluginDescriptor>();
        for (PluginDescriptor dependedDescr : getPluginDescriptors()) {
            if (dependedDescr.getId().equals(descr.getId())) {
                continue;
            }
            for (PluginPrerequisite pre : dependedDescr.getPrerequisites()) {
                if (!pre.getPluginId().equals(descr.getId())
                        || !pre.matches()) {
                    continue;
                }
                if (!result.containsKey(dependedDescr.getId())) {
                    result.put(dependedDescr.getId(), dependedDescr);
                    for (PluginDescriptor descriptor
                            : getDependingPlugins(dependedDescr)) {
                        if (!result.containsKey(descriptor.getId())) {
                            result.put(descriptor.getId(), descriptor);
                        }
                    }
                }
                break;
            }
        }
        return result.values();
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#checkIntegrity(
     *      org.java.plugin.PathResolver)
     */
    public IntegrityCheckReport checkIntegrity(
            final PathResolver pathResolver) {
        return checkIntegrity(pathResolver, false);
    }
    
    /**
     * @see org.java.plugin.registry.PluginRegistry#checkIntegrity(
     *      org.java.plugin.PathResolver, boolean)
     */
    public IntegrityCheckReport checkIntegrity(final PathResolver pathResolver,
            final boolean includeRegistrationReport) {
        final Collection<ReportItem> empty_collection = Collections.emptyList();
        IntegrityChecker intergityCheckReport = new IntegrityChecker(this,
                includeRegistrationReport ? registrationReport
                        : empty_collection);
        intergityCheckReport.doCheck(pathResolver);
        return intergityCheckReport;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#getRegistrationReport()
     */
    public IntegrityCheckReport getRegistrationReport() {
        return new IntegrityChecker(this, registrationReport);
    }
    
    /**
     * @see org.java.plugin.registry.PluginRegistry#makeUniqueId(
     *      java.lang.String, java.lang.String)
     */
    public String makeUniqueId(final String pluginId, final String id) {
        return pluginId + UNIQUE_SEPARATOR + id;
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#makeUniqueId(
     *      java.lang.String, org.java.plugin.registry.Version)
     */
    public String makeUniqueId(final String pluginId, final Version version) {
        return pluginId + UNIQUE_SEPARATOR + version;
    }
    
    /**
     * @see org.java.plugin.registry.PluginRegistry#extractPluginId(java.lang.String)
     */
    public String extractPluginId(final String uniqueId) {
        int p = uniqueId.indexOf(UNIQUE_SEPARATOR);
        if ((p <= 0) || (p >= (uniqueId.length() - 1))) {
            throw new IllegalArgumentException("invalid unique ID - " //$NON-NLS-1$
                    + uniqueId);
        }
        return uniqueId.substring(0, p);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#extractId(java.lang.String)
     */
    public String extractId(final String uniqueId) {
        int p = uniqueId.indexOf(UNIQUE_SEPARATOR);
        if ((p <= 0) || (p >= (uniqueId.length() - 1))) {
            throw new IllegalArgumentException("invalid unique ID - " //$NON-NLS-1$
                    + uniqueId);
        }
        return uniqueId.substring(p + 1);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#extractVersion(java.lang.String)
     */
    public Version extractVersion(final String uniqueId) {
        int p = uniqueId.indexOf(UNIQUE_SEPARATOR);
        if ((p <= 0) || (p >= (uniqueId.length() - 1))) {
            throw new IllegalArgumentException("invalid unique ID - " //$NON-NLS-1$
                    + uniqueId);
        }
        return Version.parse(uniqueId.substring(p + 1));
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#registerListener(
     *      org.java.plugin.registry.PluginRegistry.RegistryChangeListener)
     */
    public void registerListener(final RegistryChangeListener listener) {
        if (listeners.contains(listener)) {
            throw new IllegalArgumentException("listener " + listener //$NON-NLS-1$
                    + " already registered"); //$NON-NLS-1$
        }
        listeners.add(listener);
    }

    /**
     * @see org.java.plugin.registry.PluginRegistry#unregisterListener(
     *      org.java.plugin.registry.PluginRegistry.RegistryChangeListener)
     */
    public void unregisterListener(final RegistryChangeListener listener) {
        if (!listeners.remove(listener)) {
            log.warn("unknown listener " + listener); //$NON-NLS-1$
        }
    }
    
    void fireEvent(final RegistryChangeDataImpl data) {
        data.dump();
        if (listeners.isEmpty()) {
            return;
        }
        // make local copy
        RegistryChangeListener[] arr =
            listeners.toArray(
            new RegistryChangeListener[listeners.size()]);
        data.beforeEventFire();
        if (log.isDebugEnabled()) {
            log.debug("propagating registry change event"); //$NON-NLS-1$
        }
        for (RegistryChangeListener element : arr)
        {
            element.registryChanged(data);
        }
        if (log.isDebugEnabled()) {
            log.debug("registry change event propagated"); //$NON-NLS-1$
        }
        data.afterEventFire();
    }
    
    private static final class RegistryChangeDataImpl
            implements RegistryChangeData {
        private Set<String> addedPlugins;
        private Set<String> removedPlugins;
        private Set<String> modifiedPlugins;
        private Map<String, String> addedExtensions;
        private Map<String, String> removedExtensions;
        private Map<String, String> modifiedExtensions;
        
        protected RegistryChangeDataImpl() {
            reset();
        }
        
        private void reset() {
            addedPlugins = new HashSet<String>();
            removedPlugins = new HashSet<String>();
            modifiedPlugins = new HashSet<String>();
            addedExtensions = new HashMap<String, String>();
            removedExtensions = new HashMap<String, String>();
            modifiedExtensions = new HashMap<String, String>();
        }
        
        protected void beforeEventFire() {
            addedPlugins = Collections.unmodifiableSet(addedPlugins);
            removedPlugins = Collections.unmodifiableSet(removedPlugins);
            modifiedPlugins = Collections.unmodifiableSet(modifiedPlugins);
            addedExtensions = Collections.unmodifiableMap(addedExtensions);
            removedExtensions = Collections.unmodifiableMap(removedExtensions);
            modifiedExtensions =
                Collections.unmodifiableMap(modifiedExtensions);
        }
        
        protected void afterEventFire() {
            reset();
        }
        
        protected void dump() {
            Log logger = LogFactory.getLog(getClass());
            if (!logger.isDebugEnabled()) {
                return;
            }
            StringBuilder buf = new StringBuilder();
            buf.append("PLUG-IN REGISTRY CHANGES DUMP:\r\n") //$NON-NLS-1$
                .append("-------------- DUMP BEGIN -----------------\r\n") //$NON-NLS-1$
                .append("\tAdded plug-ins: " + addedPlugins.size() //$NON-NLS-1$
                        + "\r\n"); //$NON-NLS-1$
            for (Object element : addedPlugins)
            {
            buf.append("\t\t") //$NON-NLS-1$
            .append(element)
            .append("\r\n"); //$NON-NLS-1$
         }
            buf.append("\tRemoved plug-ins: " + removedPlugins.size() //$NON-NLS-1$
                    + "\r\n"); //$NON-NLS-1$
            for (Object element : removedPlugins)
            {
            buf.append("\t\t") //$NON-NLS-1$
            .append(element)
            .append("\r\n"); //$NON-NLS-1$
         }
            buf.append("\tModified plug-ins: " + modifiedPlugins.size() //$NON-NLS-1$
                    + "\r\n"); //$NON-NLS-1$
            for (Object element : modifiedPlugins)
            {
            buf.append("\t\t") //$NON-NLS-1$
            .append(element)
            .append("\r\n"); //$NON-NLS-1$
         }
            buf.append("\tAdded extensions: " + addedExtensions.size() //$NON-NLS-1$
                    + "\r\n"); //$NON-NLS-1$
            for (Object element : addedExtensions.entrySet())
            {
            buf.append("\t\t") //$NON-NLS-1$
            .append(element)
            .append("\r\n"); //$NON-NLS-1$
         }
            buf.append("\tRemoved extensions: " + removedExtensions.size() //$NON-NLS-1$
                    + "\r\n"); //$NON-NLS-1$
            for (Object element : removedExtensions.entrySet())
            {
            buf.append("\t\t") //$NON-NLS-1$
            .append(element)
            .append("\r\n"); //$NON-NLS-1$
         }
            buf.append("\tModified extensions: " + modifiedExtensions.size() //$NON-NLS-1$
                    + "\r\n"); //$NON-NLS-1$
            for (Object element : modifiedExtensions.entrySet())
            {
            buf.append("\t\t") //$NON-NLS-1$
            .append(element)
            .append("\r\n"); //$NON-NLS-1$
         }
            buf.append("Memory TOTAL/FREE/MAX: ") //$NON-NLS-1$
                .append(Runtime.getRuntime().totalMemory())
                .append("/") //$NON-NLS-1$
                .append(Runtime.getRuntime().freeMemory())
                .append("/") //$NON-NLS-1$
                .append(Runtime.getRuntime().maxMemory())
                .append("\r\n"); //$NON-NLS-1$
            buf.append("-------------- DUMP END -----------------\r\n"); //$NON-NLS-1$
            logger.debug(buf.toString());
        }
        
        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#addedPlugins()
         */
        public Set<String> addedPlugins() {
            return addedPlugins;
        }
        
        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      removedPlugins()
         */
        public Set<String> removedPlugins() {
            return removedPlugins;
        }

        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      modifiedPlugins()
         */
        public Set<String> modifiedPlugins() {
            return modifiedPlugins;
        }
        
        void putAddedExtension(final String extensionUid,
                final String extensionPointUid) {
            addedExtensions.put(extensionUid, extensionPointUid);
        }

        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      addedExtensions()
         */
        public Set<String> addedExtensions() {
            return addedExtensions.keySet();
        }

        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      addedExtensions(java.lang.String)
         */
        public Set<String> addedExtensions(final String extensionPointUid) {
            final Set<String> result = new HashSet<String>();
            Entry<String, String> entry;
            for (Iterator<Entry<String, String>> it = addedExtensions.entrySet().iterator();
                    it.hasNext();) {
                entry = it.next();
                if (entry.getValue().equals(extensionPointUid)) {
                    result.add(entry.getKey());
                }
            }
            return Collections.unmodifiableSet(result);
        }
        
        void putRemovedExtension(final String extensionUid,
                final String extensionPointUid) {
            removedExtensions.put(extensionUid, extensionPointUid);
        }
        
        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      removedExtensions()
         */
        public Set<String> removedExtensions() {
            return removedExtensions.keySet();
        }

        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      removedExtensions(java.lang.String)
         */
        public Set<String> removedExtensions(final String extensionPointUid) {
            final Set<String> result = new HashSet<String>();
            Entry<String, String> entry;
            for (Iterator<Entry<String, String>> it = removedExtensions.entrySet().iterator();
                    it.hasNext();) {
               entry = it.next();
                if (entry.getValue().equals(extensionPointUid)) {
                    result.add(entry.getKey());
                }
            }
            return Collections.unmodifiableSet(result);
        }
        
        void putModifiedExtension(final String extensionUid,
                final String extensionPointUid) {
            modifiedExtensions.put(extensionUid, extensionPointUid);
        }

        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      modifiedExtensions()
         */
        public Set<String> modifiedExtensions() {
            return modifiedExtensions.keySet();
        }

        /**
         * @see org.java.plugin.registry.PluginRegistry.RegistryChangeData#
         *      modifiedExtensions(java.lang.String)
         */
        public Set<String> modifiedExtensions(final String extensionPointUid) {
            final Set<String> result = new HashSet<String>();
            Entry<String, String> entry;
            for (Iterator<Entry<String, String>> it = modifiedExtensions.entrySet().iterator();
                    it.hasNext();) {
                entry = it.next();
                if (entry.getValue().equals(extensionPointUid)) {
                    result.add(entry.getKey());
                }
            }
            return Collections.unmodifiableSet(result);
        }
    }
    
    private static final class ManifestInfoImpl implements ManifestInfo {
        private final ModelManifestInfo model;

        ManifestInfoImpl(final ModelManifestInfo aModel) {
            model = aModel;
        }
        
        /**
         * @see org.java.plugin.registry.ManifestInfo#getId()
         */
        public String getId() {
            return model.getId();
        }

        /**
         * @see org.java.plugin.registry.ManifestInfo#getVersion()
         */
        public Version getVersion() {
            return model.getVersion();
        }

        /**
         * @see org.java.plugin.registry.ManifestInfo#getVendor()
         */
        public String getVendor() {
            return model.getVendor();
        }

        /**
         * @see org.java.plugin.registry.ManifestInfo#getPluginId()
         */
        public String getPluginId() {
            return model.getPluginId();
        }

        /**
         * @see org.java.plugin.registry.ManifestInfo#getPluginVersion()
         */
        public Version getPluginVersion() {
            return model.getPluginVersion();
        }

        /**
         * @see org.java.plugin.registry.ManifestInfo#getMatchingRule()
         */
        public MatchingRule getMatchingRule() {
            return model.getMatchRule();
        }
    }
}
