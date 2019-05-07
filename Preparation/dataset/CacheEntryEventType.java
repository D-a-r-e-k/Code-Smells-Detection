/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.oscache.base.events;


/**
 * This is all the possible events that may occur on a cache entry or
 * collection of cache entries.<p>
 * There is a corresponding interface {@link CacheEntryEventListener} for
 * handling these events.
 *
 * @version        $Revision: 387 $
 * @author <a href="mailto:fbeauregard@pyxis-tech.com">Francois Beauregard</a>
 */
public final class CacheEntryEventType {
    /**
     * Get an event type for an entry added.
     */
    public static final CacheEntryEventType ENTRY_ADDED = new CacheEntryEventType();

    /**
     * Get an event type for an entry updated.
     */
    public static final CacheEntryEventType ENTRY_UPDATED = new CacheEntryEventType();

    /**
     * Get an event type for an entry flushed.
     */
    public static final CacheEntryEventType ENTRY_FLUSHED = new CacheEntryEventType();

    /**
     * Get an event type for an entry removed.
     */
    public static final CacheEntryEventType ENTRY_REMOVED = new CacheEntryEventType();

    /**
     * Get an event type for a group flush event.
     */
    public static final CacheEntryEventType GROUP_FLUSHED = new CacheEntryEventType();

    /**
     * Get an event type for a pattern flush event.
     */
    public static final CacheEntryEventType PATTERN_FLUSHED = new CacheEntryEventType();

    /**
     * Private constructor to ensure that no object of that type are
     * created externally.
     */
    private CacheEntryEventType() {
    }
}
