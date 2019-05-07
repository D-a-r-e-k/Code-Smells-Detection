/****************************************************************
 * Copyright (c) 2001, David N. Main, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the 
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * 3. The name of the author may not be used to endorse or 
 * promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/
package com.anotherbigidea.flash.movie;

import java.io.*;
import java.util.*;
import com.anotherbigidea.flash.*;
import com.anotherbigidea.flash.interfaces.*;
import com.anotherbigidea.flash.structs.*;
import com.anotherbigidea.flash.sound.SoundStreamHead;

/**
 * A Movie or Movie Clip frame
 */
public class Frame
{
    protected int frameNumber;
    protected String label;
    protected Vector placements = new Vector();
    protected boolean stop;
    protected TimeLine timeline;
    protected Actions actions;
    protected SoundStreamHead soundHeader;
    protected byte[] soundData;
    protected Sound soundToStart;
    protected int    customTag = -1;
    protected byte[] customTagData;
    
    protected Frame( int number, TimeLine timeline )
    {
        frameNumber = number;
        this.timeline = timeline;
    }
    
    public SoundStreamHead getSoundHeader() { return soundHeader; }
    public void setSoundHeader( SoundStreamHead header ) { soundHeader = header; }

    public byte[] getSoundData() { return soundData; }
    public void   setSoundData( byte[] data ) { soundData = data; }
    
    /**
     * Set a custom tag to be written at the start of the frame
     */
    public void setCustomTag( int tagId, byte[] tagData )
    {
        this.customTag     = tagId;
        this.customTagData = tagData;
    }
    
    /**
     * @return number of frames required for the sound
     */
    public int startSound( Sound soundToStart, int framesPerSec )
    {
        this.soundToStart = soundToStart;
        
        int freq = soundToStart.getFrequency();
        switch( freq )
        {
            case SWFConstants.SOUND_FREQ_5_5KHZ: freq = 5500; break;
            case SWFConstants.SOUND_FREQ_11KHZ:  freq = 11000; break;
            case SWFConstants.SOUND_FREQ_22KHZ:  freq = 22000; break;
            case SWFConstants.SOUND_FREQ_44KHZ:  freq = 44000; break;
            default: freq = 22000; break;
        }
        
        int samples = soundToStart.getSampleCount();
        int length = samples / freq;
        return length * framesPerSec;
    }
    
    /**
     * Get the frame actions
     */
    public Actions getActions() { return actions; }
    
    /**
     * Set the frame actions (or null them out)
     */
    public void setActions( Actions actions ) { this.actions = actions; }
    
    /** 
     * Reset the frame actions (if any) and return the new empty Actions object
     */
    public Actions actions( int flashVersion ) 
    { 
        actions = new Actions( 0, flashVersion ); 
        return actions;
    }
    
    /**
     * Get the frame number
     */
    public int getFrameNumber() { return frameNumber; }

    /**
     * Get the placements in this frame
     */
    public Placement[] getPlacements()
    {
        Placement[] p= new Placement[ placements.size() ];
        placements.copyInto( p );
        return p;
    }

    /**
     * Get the frame label
     * @return null if the frame has no label
     */
    public String getLabel() { return label; }

    /**
     * Set the frame label - set to null to clear any label
     */
    public void setLabel( String label ) { this.label = label; }

    /**
     * Set the stop flag - if true then the movie will stop at this frame.
     * This can be set on the last frame to prevent the movie looping.
     */
    public void stop() { this.stop = true; }


    /**
     * Place a symbol at the given coordinates at the next available
     * depth.
     */
    public Instance placeSymbol( Symbol symbol, int x, int y )
    {
        return placeSymbol( symbol, new Transform( x, y ), null, -1, -1 );
    }

    /**
     * Place a symbol at the next available depth with the given
     * matrix transform and color transform.
     * 
     * @param matrix may be null to place the symbol at (0,0)
     * @param cxform may be null if no color transform is required
     */
    public Instance placeSymbol( Symbol symbol, Transform matrix, AlphaTransform cxform )
    {
        return placeSymbol( symbol, matrix, cxform, -1, -1 );
    }

    /**
     * Place a symbol at the next available depth with the given properties.
     * 
     * @param matrix may be null to place the symbol at (0,0)
     * @param cxform may be null if no color transform is required
     * @param ratio  only for a MorphShape - the morph ratio from 0 to 65535,
     *               should be -1 for a non-MorphShape
     * @param clipDepth the top depth that will be clipped by the symbol, should
     *               be -1 if this is not a clipping symbol
     */
    public Instance placeSymbol( Symbol symbol, Transform matrix, AlphaTransform cxform,
                                 int ratio, int clipDepth )
    {
        int depth = timeline.getAvailableDepth();
        Instance inst = new Instance( symbol, depth );
        timeline.setAvailableDepth( depth+1 );
        
        if( matrix == null ) matrix = new Transform();
        
        Placement placement = new Placement( inst, matrix, cxform, null, 
                                             ratio, clipDepth, frameNumber, false,
                                             false, null );
        
        placements.add( placement );
        return inst;
    }
    
    /**
     * Replace the symbol at the given depth with the new symbol
     * 
     * @param matrix may be null to place the symbol at (0,0)
     * @param cxform may be null if no color transform is required
     * @param ratio  only for a MorphShape - the morph ratio from 0 to 65535,
     *               should be -1 for a non-MorphShape
     * @param clipDepth the top depth that will be clipped by the symbol, should
     *               be -1 if this is not a clipping symbol
     */
    public Instance replaceSymbol( Symbol symbol, int depth,
                                   Transform matrix, AlphaTransform cxform,
                                   int ratio, int clipDepth )
    {
        Instance inst = new Instance( symbol, depth );
        
        if( matrix == null ) matrix = new Transform();
        
        Placement placement = new Placement( inst, matrix, cxform, null, 
                                             ratio, clipDepth, frameNumber,
                                             false, true, null );
        
        placements.add( placement );
        return inst;
    }    
    
    /**
     * Free up the given symbol so that it no longer takes up memory.
     * All instances of the symbol must be removed first.
     * This is useful with large images when they are no longer required.
     */
    public void undefineSymbol( Symbol symbol )
    {
        placements.add( new Placement( symbol ) );
    }
    
    /**
     * Place a Movie Clip at the next available depth with the given properties.
     * 
     * @param matrix may be null to place the symbol at (0,0)
     * @param cxform may be null if no color transform is required
     * @param name   the instance name of a MovieClip - should be null if this is
     *               not a MovieClip
     * @param clipAction an array of Actions (with clipAction conditions)
     */
    public Instance placeMovieClip( Symbol symbol, Transform matrix, 
                                    AlphaTransform cxform, String name,
                                    Actions[] clipActions )
    {
        int depth = timeline.getAvailableDepth();
        Instance inst = new Instance( symbol, depth );
        timeline.setAvailableDepth( depth+1 );
        
        if( matrix == null ) matrix = new Transform();
        
        Placement placement = new Placement( inst, matrix, cxform, name, 
                                             -1, -1, frameNumber, false, false,
                                             clipActions );
        
        placements.add( placement );
        return inst;
    }

    /**
     * Replace the Symbol at the given depth with the new MovieClip
     * 
     * @param matrix may be null to place the symbol at (0,0)
     * @param cxform may be null if no color transform is required
     * @param name   the instance name of a MovieClip - should be null if this is
     *               not a MovieClip
     * @param clipAction an array of Actions (with clipAction conditions)
     */
    public Instance replaceMovieClip( Symbol symbol, int depth,
                                      Transform matrix, 
                                      AlphaTransform cxform, String name,
                                      Actions[] clipActions )
    {
        Instance inst = new Instance( symbol, depth );
        
        if( matrix == null ) matrix = new Transform();
        
        Placement placement = new Placement( inst, matrix, cxform, name, 
                                             -1, -1, frameNumber, false, true,
                                             clipActions );
        
        placements.add( placement );
        return inst;
    }    
    
    /**
     * Remove the symbol instance from the stage
     */
    public void remove( Instance instance )
    {
        placements.add( new Placement( instance, frameNumber ) );
    }
    
    
    /**
     * Alter the symbol instance by moving it to the new coordinates.
     * Only one alteration may be made to an Instance in any given frame.
     */
    public void alter( Instance instance, int x, int y )
    {
        alter( instance, new Transform( x, y ), null, -1 );
    }

    /**
     * Alter the symbol instance by applying the given transform and/or
     * color transform.
     * Only one alteration may be made to an Instance in any given frame.
     * 
     * @param matrix may be null if no positional change is to be made.
     * @param cxform may be null if no color change is required.
     */
    public void alter( Instance instance, Transform matrix, AlphaTransform cxform )
    {
        alter( instance, matrix, cxform, -1 );
    }
    
    /**
     * Alter the symbol instance by applying the given properties.
     * Only one alteration may be made to an Instance in any given frame.
     * 
     * @param matrix may be null if no positional change is to be made.
     * @param cxform may be null if no color change is required.
     * @param ratio  only for a MorphShape - the morph ratio from 0 to 65535,
     *               should be -1 for a non-MorphShape
     */
    public void alter( Instance instance, Transform matrix, 
                       AlphaTransform cxform, int ratio )
    {
        Placement placement = new Placement( instance, matrix, cxform, null, 
                                             ratio, -1, frameNumber, true, false, null );
        
        placements.add( placement );
    }
    
    protected void flushDefinitions( Movie movie, 
                                     SWFTagTypes timelineWriter,
                                     SWFTagTypes definitionWriter )
        throws IOException 
    {
        for( Enumeration enum = placements.elements(); enum.hasMoreElements(); )
        {
            Placement placement = (Placement)enum.nextElement();
            
            placement.flushDefinitions( movie, timelineWriter, definitionWriter );
        }
    }
    
    /**
     * Write the frame
     */
    protected void write( Movie movie, 
                          SWFTagTypes movieTagWriter,
                          SWFTagTypes timelineTagWriter )
        throws IOException 
    {
        if( customTag >= 0 )
        {
            timelineTagWriter.tag( customTag, false, customTagData );
        }
        
        if( actions != null )
        {
            SWFActions acts = timelineTagWriter.tagDoAction();
            acts.start(0);
            acts.blob( actions.bytes );
            acts.done();
        }
        
        if( stop )
        {
            SWFActions actions = timelineTagWriter.tagDoAction();
            
            actions.start(0);
            actions.stop();
            actions.end();
            actions.done();
        }

        if( soundHeader  != null ) soundHeader.write( timelineTagWriter );
        if( soundData    != null ) timelineTagWriter.tagSoundStreamBlock( soundData );
        if( soundToStart != null ) timelineTagWriter.tagStartSound( 
                                       soundToStart.define( movie, movieTagWriter, timelineTagWriter ),
                                       new SoundInfo( true, false, null, -1, -1, 0 ));
        
        for( Enumeration enum = placements.elements(); enum.hasMoreElements(); )
        {
            Placement placement = (Placement)enum.nextElement();
            
            placement.write( movie, movieTagWriter, timelineTagWriter );
        }

        if( label != null ) timelineTagWriter.tagFrameLabel( label );
        timelineTagWriter.tagShowFrame();
    }    
}
