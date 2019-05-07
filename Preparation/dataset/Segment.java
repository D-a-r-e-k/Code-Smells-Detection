/* Segment.java */

package org.quilt.cover.seg;

import java.util.Iterator;
import java.util.List;

/** 
 * A segment for coverage purposes.  It has an index and visit count, 
 * and 'from' and 'to', which will typically be line numbers or indices
 * of some sort.
 */

public class Segment {
   
    /** ideally unique */
    private int index  = -1;
    /** visit count */
    private int visits = 0;
    /** index, line number, etc */
    private int from   = -1;
    private int to     = -1;

    /** No-arg constructor; creates a segment with -1 index. */
    public Segment() { 
    }
    /** 
     * Constructor specifying a segment index. 
     * 
     * @param n The index; should be non-negative.
     */
    
    public Segment (int n) {
        index = n;
    }
    // GET/SET METHODS //////////////////////////////////////////////
    /** Get the 'from' value. */
    public int getFrom() {
        return from;
    }
    /** Set the 'from' value. */
    public void setFrom (int n) {
        from = n;
    }
    
    /** Get the (ideally unique) index of the segment. */
    public int getIndex() {
        return index;
    }
    /** Set the segment index. */
    public void setIndex (int n) {
        index = n;
    }
    
    /** Get the 'to' value. */
    public int getTo() {
        return to;
    }
    /** Set the 'to' value. */
    public void setTo (int n) {
        to = n;
    }
    
    /** Get the number of visits so far. */
    public int getVisits() {
        return visits;
    }
    /** Set the number of visits to zero. */
    public void reset() {
        visits = 0;
    }
    /** Set the number of visits; may never be used. */
    public void setVisits(int n) {
        visits = n;
    }
    // OTHER METHODS ////////////////////////////////////////////////
    /** 
     * Add the visit count from another segment. 
     *
     * @param seg Reference to the other segment.
     */
    protected Segment add( final Segment seg ) {
        visits += seg.getVisits();
        return this;
    }
    /** Step the visit count. */
    public void visit () {
        visits++;
    }
    /** 
     * Format the segment for XML output.  
     *
     * @param type String whose value is appropriate for an XML element name.
     */
    public String toXML(final String type) {
        return "<" + type + " index=\""  + index 
            + "\" from=\"" + from 
            + "\" to=\"" + to + "\">\n"
            
            + "  <visits>" + visits + "</visits>\n"

            + "</" + type + ">\n";
    }
    /** Format the segment for output as a String. */
    public String toString () {
        return "segment " + index + ": " + from + " --> " + to 
            + " : " + visits + " visits";
    }
}
