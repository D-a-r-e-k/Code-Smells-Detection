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
package org.java.plugin.tools.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.java.plugin.ObjectFactory;
import org.java.plugin.PathResolver;
import org.java.plugin.registry.Identity;
import org.java.plugin.registry.ManifestInfo;
import org.java.plugin.registry.ManifestProcessingException;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.util.IoUtil;

/**
 * Base class for some JPF related ant tasks.
 * @version $Id: BaseJpfTask.java,v 1.8 2007/03/03 17:16:26 ddimon Exp $
 */
public abstract class BaseJpfTask extends MatchingTask {
    private static final FileUtils fileUtils = FileUtils.newFileUtils();
    
    private final LinkedList<FileSet> fileSets = new LinkedList<FileSet>();
    private File baseDir;
    private boolean verbose;
    private PluginRegistry registry;
    private PathResolver pathResolver;
    private Set<String> whiteList;
    private Set<String> blackList;

    /**
     * @param set the set of files to be registered as manifests
     */
     public void addFileset(final FileSet set) {
         fileSets.add(set);
     }

    /**
     * @param aBaseDir base directory for manifest files
     */
    public final void setBaseDir(final File aBaseDir) {
        this.baseDir = aBaseDir;
    }

    /**
     * @param aVerbose <code>true</code> if detailed integrity check report
     *                required
     */
    public final void setVerbose(final boolean aVerbose) {
        this.verbose = aVerbose;
    }
    
    /**
     * @param file while list file
     * @throws IOException if list reading failed
     */
    public final void setWhiteList(final File file) throws IOException {
        whiteList = loadList(file);
    }
    
    /**
     * @param file black list file
     * @throws IOException if list reading failed
     */
    public final void setBlackList(final File file) throws IOException {
        blackList = loadList(file);
    }
    
    protected Set<String> loadList(final File file) throws IOException {
        if (file == null) {
            return null;
        }
        Set<String> result = new HashSet<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8")); //$NON-NLS-1$
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    result.add(line);
                }
            }
        } finally {
            reader.close();
        }
        return result;
    }
    
    protected final boolean getVerbose() {
        return verbose;
    }

    protected final PathResolver getPathResolver() {
        return pathResolver;
    }
    
    protected final PluginRegistry getRegistry() {
        return registry;
    }
    
    protected Set<String> getWhiteList() {
        return whiteList;
    }
    
    protected Set<String> getBlackList() {
        return blackList;
    }

    protected final void initRegistry(final boolean usePathResolver) {
        if (baseDir != null) {
            if (!baseDir.isDirectory()) {
                throw new BuildException("basedir " + baseDir //$NON-NLS-1$
                        + " does not exist!", getLocation()); //$NON-NLS-1$
            }
        } else {
            baseDir = getProject().getBaseDir();
        }
        ObjectFactory objectFactory = ObjectFactory.newInstance();
        registry = objectFactory.createRegistry();
        File[] manifestFiles = getIncludedFiles();
        List<URL> manifestUrls = new LinkedList<URL>();
        final Map<String, URL> foldersMap = new HashMap<String, URL>();
        for (int i = 0; i < manifestFiles.length; i++) {
            File manifestFile = manifestFiles[i];
            try {
                //manifestUrls[i] = IoUtil.file2url(manifestFile);
                URL manifestUrl = getManifestURL(manifestFile);
                if (manifestUrl == null) {
                    if (verbose) {
                        log("Skipped file: " + manifestFile); //$NON-NLS-1$
                    }
                    continue;
                }
                try {
                    if (!isManifestAccepted(manifestUrl)) {
                        if (verbose) {
                            log("Skipped URL: " + manifestUrl); //$NON-NLS-1$
                        }
                        continue;
                    }
                } catch (ManifestProcessingException mpe) {
                    throw new BuildException("can't read manifest from URL " //$NON-NLS-1$
                            + manifestUrl, mpe, getLocation());
                }
                manifestUrls.add(manifestUrl);
                if (verbose) {
                    log("Added URL: " + manifestUrl); //$NON-NLS-1$
                }
                if (usePathResolver) {
                    /*foldersMap.put(manifestUrls[i],
                            IoUtil.file2url(manifestFile.getParentFile()));*/
                    if ("jar".equals(manifestUrl.getProtocol())) { //$NON-NLS-1$
                        foldersMap.put(manifestUrl.toExternalForm(),
                            IoUtil.file2url(manifestFile));
                    } else {
                        foldersMap.put(manifestUrl.toExternalForm(),
                            IoUtil.file2url(manifestFile.getParentFile()));
                    }
                }
            } catch (MalformedURLException mue) {
                throw new BuildException("can't create URL for file " //$NON-NLS-1$
                        + manifestFile, mue, getLocation());
            }
        }
        final Map<String, Identity> processedPlugins;
        try {
            processedPlugins = registry.register(
                    manifestUrls.toArray(new URL[manifestUrls.size()]));
        } catch (Exception e) {
            throw new BuildException("can't register URLs", e, getLocation()); //$NON-NLS-1$
        }
        log("Registry initialized, registered manifests: " //$NON-NLS-1$
                + processedPlugins.size() + " of " + manifestUrls.size(), //$NON-NLS-1$
                (processedPlugins.size() != manifestUrls.size())
                    ? Project.MSG_WARN : Project.MSG_INFO);
        if (usePathResolver) {
            pathResolver = objectFactory.createPathResolver();
            for (Entry<String, Identity> entry : processedPlugins.entrySet()) {
                pathResolver.registerContext(entry.getValue(),
                        foldersMap.get(entry.getKey()));
            }
            if (verbose) {
                log("Path resolver initialized"); //$NON-NLS-1$
            }
        }
    }
    
    protected File[] getIncludedFiles() {
        Set<File> result = new HashSet<File>();
        for (FileSet fs : fileSets) {
            for (String file
                    : fs.getDirectoryScanner(getProject()).getIncludedFiles()) {
                if (file != null) {
                    result.add(fileUtils.resolveFile(
                            fs.getDir(getProject()), file));
                }
            }
        }
        if (fileSets.isEmpty()) {
            for (String file
                    : getDirectoryScanner(baseDir).getIncludedFiles()) {
                if (file != null) {
                    result.add(fileUtils.resolveFile(baseDir, file));
                }
            }
        }
        return result.toArray(new File[result.size()]);
    }
    
    protected URL getManifestURL(final File file) throws MalformedURLException {
        if(file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) { //$NON-NLS-1$ //$NON-NLS-2$
            URL url = new URL("jar:" + IoUtil.file2url(file).toExternalForm() //$NON-NLS-1$
            + "!/plugin.xml"); //$NON-NLS-1$
            if (IoUtil.isResourceExists(url)) {
                return url;
            }
            url = new URL("jar:" + IoUtil.file2url(file).toExternalForm() //$NON-NLS-1$
                    + "!/plugin-fragment.xml"); //$NON-NLS-1$
            if (IoUtil.isResourceExists(url)) {
                return url;
            }
            url = new URL("jar:" + IoUtil.file2url(file).toExternalForm() //$NON-NLS-1$
                    + "!/META-INF/plugin.xml"); //$NON-NLS-1$
            if (IoUtil.isResourceExists(url)) {
                return url;
            }
            url = new URL("jar:" + IoUtil.file2url(file).toExternalForm() //$NON-NLS-1$
                    + "!/META-INF/plugin-fragment.xml"); //$NON-NLS-1$
            if (IoUtil.isResourceExists(url)) {
                return url;
            }
            return null;
        }
        return IoUtil.file2url(file);
    }
    
    protected boolean isManifestAccepted(final URL manifestUrl)
            throws ManifestProcessingException {
        if ((whiteList == null) && (blackList == null)) {
            return true;
        }
        ManifestInfo manifestInfo = registry.readManifestInfo(manifestUrl);
        if (whiteList != null) {
            if (isPluginInList(manifestInfo, whiteList)) {
                return true;
            }
        }
        if ((blackList != null) && isPluginInList(manifestInfo, blackList)) {
            return false;
        }
        return true;
    }
    
    private boolean isPluginInList(final ManifestInfo manifestInfo,
            final Set<String> list) {
        if (list.contains(manifestInfo.getId())) {
            return true;
        }
        return list.contains(registry.makeUniqueId(manifestInfo.getId(),
                manifestInfo.getVersion()));
    }
}
