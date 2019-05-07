/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2004-2006 Dmitry Olshansky
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
package org.java.plugin.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.java.plugin.ObjectFactory;
import org.java.plugin.PathResolver;
import org.java.plugin.registry.Identity;
import org.java.plugin.registry.ManifestProcessingException;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginFragment;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.registry.Version;
import org.java.plugin.util.IoUtil;

/**
 * Plug-ins archive support class.
 * @version $Id$
 */
public final class PluginArchiver {
    private static final String DESCRIPTOR_ENTRY_NAME = "JPF-DESCRIPTOR"; //$NON-NLS-1$
    
    /**
     * Packs given plug-in into single ZIP file. Resulting file may be used to
     * run plug-ins from.
     * @param descr plug-in descriptor
     * @param pathResolver path resolver instance
     * @param destFile target file
     * @throws IOException if an I/O error has occurred
     */
    public static void pack(final PluginDescriptor descr,
            final PathResolver pathResolver, final File destFile)
            throws IOException {
        pack(pathResolver.resolvePath(descr, "/"), //$NON-NLS-1$
                "JPF plug-in "+ descr.getId() //$NON-NLS-1$
                + " of version " + descr.getVersion(), destFile); //$NON-NLS-1$
    }
    
    /**
     * Packs given plug-in fragment into single ZIP file. Resulting file may be
     * used to run plug-ins from.
     * @param fragment plug-in fragment descriptor
     * @param pathResolver path resolver instance
     * @param destFile target file
     * @throws IOException if an I/O error has occurred
     */
    public static void pack(final PluginFragment fragment,
            final PathResolver pathResolver, final File destFile)
            throws IOException {
        pack(pathResolver.resolvePath(fragment, "/"), //$NON-NLS-1$
                "JPF plug-in fragment "+ fragment.getId() //$NON-NLS-1$
                + " of version " + fragment.getVersion(), destFile); //$NON-NLS-1$
    }
    
    private static void pack(final URL url, final String comment,
            final File destFile) throws IOException {
        ZipOutputStream zipStrm = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(
                        destFile, false)));
        try {
            zipStrm.setComment(comment);
            File file = IoUtil.url2file(url);
            if (file == null) {
                throw new IOException("resolved URL " + url //$NON-NLS-1$
                        + " is not local file system location pointer"); //$NON-NLS-1$
            }
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                packEntry(zipStrm, null, files[i]);
            }
        } finally {
            zipStrm.close();
        }
    }
    
    /**
     * Packs all plug-ins from given registry as one archive file.
     * @param registry plug-ins registry
     * @param pathResolver path resolver (only local file URLs are supported)
     * @param destFile target archive file (will be overridden if any exists)
     * @return set of UID's of all packed plug-ins
     * @throws IOException if an I/O error has occurred
     */
    public static Set<String> pack(final PluginRegistry registry,
            final PathResolver pathResolver, final File destFile)
            throws IOException {
        return pack(registry, pathResolver, destFile, new Filter() {
            public boolean accept(final String id, final Version version,
                    final boolean isFragment) {
                return true;
            }
        });
    }

    /**
     * Packs plug-ins from given registry as one archive file according to
     * given filter.
     * @param registry plug-ins registry
     * @param pathResolver path resolver (only local file URLs are supported)
     * @param destFile target archive file (will be overridden if any exists)
     * @param filter filter to be used when packing plug-ins
     * @return set of UID's of all packed plug-ins
     * @throws IOException if an I/O error has occurred
     */
    public static Set<String> pack(final PluginRegistry registry,
            final PathResolver pathResolver, final File destFile,
            final Filter filter) throws IOException {
        Set<String> result;
        ZipOutputStream zipStrm = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(
                        destFile, false)));
        try {
            zipStrm.setComment("JPF plug-ins archive"); //$NON-NLS-1$
            ZipEntry entry = new ZipEntry(DESCRIPTOR_ENTRY_NAME);
            entry.setComment("JPF plug-ins archive descriptor"); //$NON-NLS-1$
            zipStrm.putNextEntry(entry);
            result = writeDescripor(registry, filter,
                    new ObjectOutputStream(zipStrm));
            zipStrm.closeEntry();
            for (PluginDescriptor descr : registry.getPluginDescriptors()) {
                if (!result.contains(descr.getUniqueId())) {
                    continue;
                }
                URL url = pathResolver.resolvePath(descr, "/"); //$NON-NLS-1$
                File file = IoUtil.url2file(url);
                if (file == null) {
                    throw new IOException("resolved URL " + url //$NON-NLS-1$
                            + " is not local file system location pointer"); //$NON-NLS-1$
                }
                entry = new ZipEntry(descr.getUniqueId() + "/"); //$NON-NLS-1$
                entry.setComment("Content for JPF plug-in " //$NON-NLS-1$
                        + descr.getId() + " version " + descr.getVersion()); //$NON-NLS-1$
                entry.setTime(file.lastModified());
                zipStrm.putNextEntry(entry);
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    packEntry(zipStrm, entry, files[i]);
                }
            }
            for (PluginFragment fragment : registry.getPluginFragments()) {
                if (!result.contains(fragment.getUniqueId())) {
                    continue;
                }
                URL url = pathResolver.resolvePath(fragment, "/"); //$NON-NLS-1$
                File file = IoUtil.url2file(url);
                if (file == null) {
                    throw new IOException("resolved URL " + url //$NON-NLS-1$
                            + " is not local file system location pointer"); //$NON-NLS-1$
                }
                entry = new ZipEntry(fragment.getUniqueId() + "/"); //$NON-NLS-1$
                entry.setComment("Content for JPF plug-in fragment " //$NON-NLS-1$
                        + fragment.getId() + " version " //$NON-NLS-1$
                        + fragment.getVersion());
                entry.setTime(file.lastModified());
                zipStrm.putNextEntry(entry);
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    packEntry(zipStrm, entry, files[i]);
                }
            }
        } finally {
            zipStrm.close();
        }
        return result;
    }
    
    private static void packEntry(final ZipOutputStream zipStrm,
            final ZipEntry parentEntry, final File file) throws IOException {
        String parentEntryName = (parentEntry == null) ? "" //$NON-NLS-1$
                : parentEntry.getName();
        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(parentEntryName + file.getName());
            entry.setTime(file.lastModified());
            zipStrm.putNextEntry(entry);
            BufferedInputStream fileStrm = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                IoUtil.copyStream(fileStrm, zipStrm, 1024);
            } finally {
                fileStrm.close();
            }
            return;
        }
        ZipEntry entry = new ZipEntry(parentEntryName + file.getName()
                + "/"); //$NON-NLS-1$
        entry.setTime(file.lastModified());
        zipStrm.putNextEntry(entry);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            packEntry(zipStrm, entry, files[i]);
        }
    }

    /**
     * Extracts plug-ins from the given archive file.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register manifests for
     *                 unpacked plug-ins
     * @param destFolder target folder
     * @return set of UID's of all un-packed (and registered) plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     * 
     * @see #unpack(URL, PluginRegistry, File, PluginArchiver.Filter)
     */
    public static Set<String> unpack(final URL archiveFile,
            final PluginRegistry registry, final File destFolder)
            throws ManifestProcessingException, IOException,
            ClassNotFoundException {
        return unpack(archiveFile, registry, destFolder, new Filter() {
            public boolean accept(final String id, final Version version,
                    final boolean isFragment) {
                return true;
            }
        });
    }


    /**
     * Extracts plug-ins from the given archive file.
     * <br>
     * <b>Note:</b>
     * <br>
     * In the current implementation all plug-in manifests are extracted to
     * temporary local storage and deleted immediately after their registration
     * with plug-in registry. So manifest URL's are actually point to "fake"
     * locations.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register manifests for
     *                 unpacked plug-ins
     * @param destFolder target folder
     * @param filter filter to be used when un-packing plug-ins
     * @return set of UID's of all un-packed (and registered) plug-ins
     * @throws ClassNotFoundException if plug-ins archive descriptor can't be
     *         de-serialized
     * @throws ManifestProcessingException if plug-in manifests can't be
     *         registered
     * @throws IOException if archive damaged or I/O error has occurred
     */
    public static Set<String> unpack(final URL archiveFile,
            final PluginRegistry registry, final File destFolder,
            final Filter filter) throws IOException,
            ManifestProcessingException, ClassNotFoundException {
        Set<String> result;
        int count = 0;
        ZipInputStream zipStrm = new ZipInputStream(new BufferedInputStream(
                archiveFile.openStream()));
        try {
            ZipEntry entry = zipStrm.getNextEntry();
            //NB: we are expecting that descriptor is in the first ZIP entry
            if (entry == null) {
                throw new IOException(
                        "invalid plug-ins archive, no entries found"); //$NON-NLS-1$
            }
            if (!DESCRIPTOR_ENTRY_NAME.equals(entry.getName())) {
                throw new IOException("invalid plug-ins archive " + archiveFile //$NON-NLS-1$
                        + ", entry " + DESCRIPTOR_ENTRY_NAME //$NON-NLS-1$
                        + " not found as first ZIP entry in the archive file"); //$NON-NLS-1$
            }
            ObjectInputStream strm = new ObjectInputStream(zipStrm);
            result = readDescriptor(strm, registry, destFolder, filter);
            entry = zipStrm.getNextEntry();
            while (entry != null) {
                String name = entry.getName();
                if (name.endsWith("/") //$NON-NLS-1$
                        && (name.lastIndexOf('/', name.length() - 2) == -1)) {
                    String uid = name.substring(0, name.length() - 1);
                    if (!result.contains(uid)) {
                        entry = zipStrm.getNextEntry();
                        continue;
                    }
                    count++;
                } else {
                    int p = name.indexOf('/');
                    if ((p == -1) || (p == 0)
                            || !result.contains(name.substring(0, p))) {
                        entry = zipStrm.getNextEntry();
                        continue;
                    }
                }
                unpackEntry(zipStrm, entry, destFolder);
                entry = zipStrm.getNextEntry();
            }
        } finally {
            zipStrm.close();
        }
        if (result.size() != count) {
            throw new IOException("invalid plug-ins number (" + count //$NON-NLS-1$
                    + ") found in the archive, expected number according to " //$NON-NLS-1$
                    + "the archive descriptor is " + result.size()); //$NON-NLS-1$
        }
        return result;
    }
    
    /**
     * Extracts all plug-ins from the given archive file.
     * <br>
     * <b>Note:</b>
     * <br>
     * {@link ObjectFactory#createRegistry() Standard plug-in registry}
     * implementation will be used internally to read plug-in manifests.
     * @param archiveFile plug-in archive file
     * @param destFolder target folder
     * @return set of UID's of all un-packed plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     * 
     * @see ObjectFactory#createRegistry()
     */
    public static Set<String> unpack(final URL archiveFile,
            final File destFolder) throws ManifestProcessingException,
            IOException, ClassNotFoundException {
        return unpack(archiveFile, ObjectFactory.newInstance().createRegistry(),
                destFolder);
    }
    
    /**
     * Extracts plug-ins from the given archive file according to given filter.
     * <br>
     * <b>Note:</b>
     * <br>
     * {@link ObjectFactory#createRegistry() Standard plug-in registry}
     * implementation will be used internally to read plug-in manifests.
     * @param archiveFile plug-in archive file
     * @param destFolder target folder
     * @param filter filter to be used when un-packing plug-ins
     * @return set of UID's of all un-packed plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     */
    public static Set<String> unpack(final URL archiveFile,
            final File destFolder, final Filter filter)
            throws ManifestProcessingException, IOException,
            ClassNotFoundException {
        return unpack(archiveFile, ObjectFactory.newInstance().createRegistry(),
                destFolder, filter);
    }
    
    private static void unpackEntry(final ZipInputStream zipStrm,
            final ZipEntry entry, final File destFolder) throws IOException {
        String name = entry.getName();
        if (name.endsWith("/")) { //$NON-NLS-1$
            File folder = new File(destFolder.getCanonicalPath() + "/" + name); //$NON-NLS-1$
            if (!folder.exists() && !folder.mkdirs()) {
                throw new IOException("can't create folder " + folder); //$NON-NLS-1$
            }
            folder.setLastModified(entry.getTime());
            return;
        }
        File file = new File(destFolder.getCanonicalPath() + "/" + name); //$NON-NLS-1$
        File folder = file.getParentFile();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("can't create folder " + folder); //$NON-NLS-1$
        }
        OutputStream strm = new BufferedOutputStream(
                new FileOutputStream(file, false));
        try {
            IoUtil.copyStream(zipStrm, strm, 1024);
        } finally {
            strm.close();
        }
        file.setLastModified(entry.getTime());
    }

    /**
     * Reads meta-information from plug-ins archive file and registers found
     * plug-in manifest data with given registry for future analysis.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register discovered manifests
     *                 for archived plug-ins
     * @return set of UID's of all registered plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     * 
     * @see #readDescriptor(URL, PluginRegistry, PluginArchiver.Filter)
     */
    public static Set<String> readDescriptor(final URL archiveFile,
            final PluginRegistry registry)
            throws IOException, ClassNotFoundException,
            ManifestProcessingException {
        return readDescriptor(archiveFile, registry, new Filter() {
            public boolean accept(final String id, final Version version,
                    final boolean isFragment) {
                return true;
            }
        });
    }

    /**
     * Reads meta-information from plug-ins archive file and registers found
     * plug-in manifest data with given registry for future analysis.
     * <br>
     * <b>Note:</b>
     * <br>
     * In the current implementation all plug-in manifests are extracted to
     * temporary local storage and deleted immediately after their registration
     * with plug-in registry. So manifest URL's are actually point to "fake"
     * locations and main purpose of this method is to allow you to analyze
     * plug-ins archive without needing to download and unpack it.
     * @param archiveFile plug-in archive file
     * @param registry plug-in registry where to register discovered manifests
     *                 for archived plug-ins
     * @param filter filter to be used when un-packing plug-ins
     * @return set of UID's of all registered plug-ins
     * @throws IOException if an I/O error has occurred
     * @throws ClassNotFoundException if descriptor can't be read
     * @throws ManifestProcessingException if manifest can't be registered
     *         (optional behavior)
     */
    public static Set<String> readDescriptor(final URL archiveFile,
            final PluginRegistry registry, final Filter filter)
            throws IOException, ClassNotFoundException,
            ManifestProcessingException {
        ZipInputStream zipStrm = new ZipInputStream(new BufferedInputStream(
                archiveFile.openStream()));
        try {
            ZipEntry entry = zipStrm.getNextEntry();
            //NB: we are expecting that descriptor is in the first ZIP entry
            if (entry == null) {
                throw new IOException(
                        "invalid plug-ins archive, no entries found"); //$NON-NLS-1$
            }
            if (!DESCRIPTOR_ENTRY_NAME.equals(entry.getName())) {
                throw new IOException("invalid plug-ins archive " + archiveFile //$NON-NLS-1$
                        + ", entry " + DESCRIPTOR_ENTRY_NAME //$NON-NLS-1$
                        + " not found as first ZIP entry in the archive file"); //$NON-NLS-1$
            }
            ObjectInputStream strm = new ObjectInputStream(zipStrm);
            return readDescriptor(strm, registry, Util.getTempFolder(), filter);
        } finally {
            zipStrm.close();
        }
    }
    
    private static Set<String> writeDescripor(final PluginRegistry registry,
            final Filter filter, final ObjectOutputStream strm)
            throws IOException {
        final Map<String, ArchiveDescriptorEntry> result = new HashMap<String, ArchiveDescriptorEntry>();
        for (PluginDescriptor descr : registry.getPluginDescriptors()) {
            if (!filter.accept(descr.getId(), descr.getVersion(), false)) {
                continue;
            }
            result.put(descr.getUniqueId(),
                    new ArchiveDescriptorEntry(descr.getId(),
                            descr.getVersion(), false,
                            Util.readUrlContent(descr.getLocation())));
        }
        for (PluginFragment fragment : registry.getPluginFragments()) {
            if (!filter.accept(fragment.getId(), fragment.getVersion(), true)) {
                continue;
            }
            result.put(fragment.getUniqueId(),
                    new ArchiveDescriptorEntry(fragment.getId(),
                            fragment.getVersion(), true,
                            Util.readUrlContent(fragment.getLocation())));
        }
        strm.writeObject(result.values().toArray(
                new ArchiveDescriptorEntry[result.size()]));
        return result.keySet();
    }
    
    private static Set<String> readDescriptor(final ObjectInputStream strm,
            final PluginRegistry registry, final File tempFolder,
            final Filter filter) throws IOException, ClassNotFoundException,
            ManifestProcessingException {
        ArchiveDescriptorEntry[] data =
            (ArchiveDescriptorEntry[]) strm.readObject();
        // For simplicity we'll store manifests to a temporary files rather than
        // create special URL's and provide special URL handler for them.
        // More powerful approach will be possibly implemented in the future.
        Set<URL> urls = new HashSet<URL>();
        Set<File> files = new HashSet<File>();
        for (int i = 0; i < data.length; i++) {
            if (!filter.accept(data[i].getId(), data[i].getVersion(),
                    data[i].isFragment())) {
                continue;
            }
            File file = File.createTempFile("manifest.", null, tempFolder); //$NON-NLS-1$
            file.deleteOnExit();
            OutputStream fileStrm = new BufferedOutputStream(
                    new FileOutputStream(file, false));
            try {
                fileStrm.write(data[i].getData());
            } finally {
                fileStrm.close();
            }
            files.add(file);
            urls.add(IoUtil.file2url(file));
        }
        Set<String> result = new HashSet<String>();
        try {
            for (Identity obj : registry.register(urls.toArray(
                    new URL[urls.size()])).values()) {
                if (obj instanceof PluginDescriptor) {
                    result.add(((PluginDescriptor) obj).getUniqueId());
                } else if (obj instanceof PluginFragment) {
                    result.add(((PluginFragment) obj).getUniqueId());
                } else {
                    //NB: ignore all other elements
                }
            }
        } finally {
            for (File file : files) {
                file.delete();
            }
        }
        return result;
    }
    
    private PluginArchiver() {
        // no-op
    }
    
    /**
     * Callback interface to filter plug-ins being processed.
     * @version $Id$
     */
    public static interface Filter {
        /**
         * @param id plug-in or plug-in fragment identifier
         * @param version plug-in or plug-in fragment version
         * @param isFragment <code>true</code> if given identity data
         *                   corresponds to plug-in fragment
         * @return <code>true</code> if plug-in or plug-in fragment with given
         *         identity should be taken into account
         */
        boolean accept(String id, Version version, boolean isFragment);
    }
    
    private static class ArchiveDescriptorEntry implements Serializable {
        private static final long serialVersionUID = 8749937247555974932L;
        
        private final String id;
        private final Version version;
        private final boolean isFragment;
        private final byte[] data;
        
        protected ArchiveDescriptorEntry(final String anId,
                final Version aVersion, final boolean fragment,
                final byte[] aData) {
            id = anId;
            version = aVersion;
            isFragment = fragment;
            data = aData;
        }

        protected String getId() {
            return id;
        }

        protected Version getVersion() {
            return version;
        }
        
        protected boolean isFragment() {
            return isFragment;
        }

        protected byte[] getData() {
            return data;
        }
    }
}
