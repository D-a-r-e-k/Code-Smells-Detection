/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.oscache.extra;

import com.opensymphony.oscache.base.events.*;

/**
 * Implementation of a CacheEntryEventListener. It use the events to count
 * the operations performed on the cache.
 * <p>
 * We are not using any synchronized so that this does not become a bottleneck.
 * The consequence is that on retrieving values, the operations that are
 * currently being done won't be counted.
 *
 * @version        $Revision: 254 $
 * @author <a href="mailto:abergevin@pyxis-tech.com">Alain Bergevin</a>
 * @author <a href="&#109;a&#105;&#108;&#116;&#111;:chris&#64;swebtec.&#99;&#111;&#109;">Chris Miller</a>
 */
public class CacheEntryEventListenerImpl implements CacheEntryEventListener {
    /**
     * Counter for the cache flushes
     */
    private int cacheFlushedCount = 0;

    /**
     * Counter for the added entries
     */
    private int entryAddedCount = 0;

    /**
     * Counter for the flushed entries
     */
    private int entryFlushedCount = 0;

    /**
     * Counter for the removed entries
     */
    private int entryRemovedCount = 0;

    /**
     * Counter for the updated entries
     */
    private int entryUpdatedCount = 0;

    /**
     * Counter for the flushed groups
     */
    private int groupFlushedCount = 0;

    /**
     * Counter for the pattern flushes
     */
    private int patternFlushedCount = 0;

    /**
     * Constructor, empty for us
     */
    public CacheEntryEventListenerImpl() {
    }

    /**
     * Gets the add counter
     *
     * @return The added counter
     */
    public int getEntryAddedCount() {
        return entryAddedCount;
    }

    /**
     * Gets the flushed counter
     *
     * @return The flushed counter
     */
    public int getEntryFlushedCount() {
        return entryFlushedCount;
    }

    /**
     * Gets the removed counter
     *
     * @return The removed counter
     */
    public int getEntryRemovedCount() {
        return entryRemovedCount;
    }

    /**
     * Gets the updated counter
     *
     * @return The updated counter
     */
    public int getEntryUpdatedCount() {
        return entryUpdatedCount;
    }

    /**
     * Gets the group flush counter
     *
     * @return The number of group flush calls that have occurred
     */
    public int getGroupFlushedCount() {
        return groupFlushedCount;
    }

    /**
     * Gets the pattern flush counter
     *
     * @return The number of pattern flush calls that have occurred
     */
    public int getPatternFlushedCount() {
        return patternFlushedCount;
    }

    /**
     * Gets the cache flush counter
     *
     * @return The number of times the entire cache has been flushed
     */
    public int getCacheFlushedCount() {
        return cacheFlushedCount;
    }

    /**
     * Handles the event fired when an entry is added in the cache.
     *
     * @param event The event triggered when a cache entry has been added
     */
    public void cacheEntryAdded(CacheEntryEvent event) {
        entryAddedCount++;
    }

    /**
     * Handles the event fired when an entry is flushed from the cache.
     *
     * @param event The event triggered when a cache entry has been flushed
     */
    public void cacheEntryFlushed(CacheEntryEvent event) {
        entryFlushedCount++;
    }

    /**
     * Handles the event fired when an entry is removed from the cache.
     *
     * @param event The event triggered when a cache entry has been removed
     */
    public void cacheEntryRemoved(CacheEntryEvent event) {
        entryRemovedCount++;
    }

    /**
     * Handles the event fired when an entry is updated in the cache.
     *
     * @param event The event triggered when a cache entry has been updated
     */
    public void cacheEntryUpdated(CacheEntryEvent event) {
        entryUpdatedCount++;
    }

    /**
     * Handles the event fired when a group is flushed from the cache.
     *
     * @param event The event triggered when a cache group has been flushed
     */
    public void cacheGroupFlushed(CacheGroupEvent event) {
        groupFlushedCount++;
    }

    /**
     * Handles the event fired when a pattern is flushed from the cache.
     *
     * @param event The event triggered when a cache pattern has been flushed
     */
    public void cachePatternFlushed(CachePatternEvent event) {
        patternFlushedCount++;
    }

    /**
     * Handles the event fired when a cache flush occurs.
     *
     * @param event The event triggered when an entire cache is flushed
     */
    public void cacheFlushed(CachewideEvent event) {
        cacheFlushedCount++;
    }

    /**
     * Returns the internal values in a string form
     */
    public String toString() {
        return ("Added " + entryAddedCount + ", Updated " + entryUpdatedCount + ", Flushed " + entryFlushedCount + ", Removed " + entryRemovedCount + ", Groups Flushed " + groupFlushedCount + ", Patterns Flushed " + patternFlushedCount + ", Cache Flushed " + cacheFlushedCount);
    }
}
