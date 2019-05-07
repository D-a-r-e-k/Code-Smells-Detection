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
package com.anotherbigidea.flash.sound;

import java.io.*;
import java.util.*;
import com.anotherbigidea.flash.*;
import com.anotherbigidea.flash.movie.*;

/**
 * MP3 Utilities
 */
public class MP3Helper
{
    public static Sound getSoundDefinition( InputStream mp3 ) throws IOException
    {
        MP3Frame frame = MP3Frame.readFrame( mp3 );
        
        int samplesPerFrame = frame.getSamplesPerFrame();
        int sampleRate = frame.getSampleRate();
        
        boolean isStereo = frame.isStereo();
        
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if     ( sampleRate >= 44000 ) rate = SWFConstants.SOUND_FREQ_44KHZ;
        else if( sampleRate >= 22000 ) rate = SWFConstants.SOUND_FREQ_22KHZ;
        else if( sampleRate >= 11000 ) rate = SWFConstants.SOUND_FREQ_11KHZ;        
        
        int sampleCount = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        while( true && frame != null )
        {        
            //--Write DelaySeek of zero
            bout.write(0);
            bout.write(0);
                    
            while( frame != null )
            {
                sampleCount += frame.getSamplesPerFrame();
                frame.write( bout );
                frame = MP3Frame.readFrame( mp3 );
            }
            
            bout.flush();
        }
        
        mp3.close();     
        
        byte[] data = bout.toByteArray();        
        
        return new Sound( SWFConstants.SOUND_FORMAT_MP3, rate, true, isStereo, sampleCount, data );
    }
    
    /**
     * Read an MP3 input file.
     * Write the Sound Stream Header to the SWFTagTypes interface.
     * Return a list of byte[] - one for each Streaming Sound Block
     */
    public static SoundStreamHead streamingBlocks( InputStream mp3, int framesPerSecond, ArrayList blocks ) 
        throws IOException
    {
        MP3Frame frame = MP3Frame.readFrame( mp3 );
        
        int samplesPerFrame = frame.getSamplesPerFrame();
        int sampleRate = frame.getSampleRate();
        int totalSamples = 0;
        
        int samplesPerSWFFrame = sampleRate / framesPerSecond;
                
        boolean isStereo = frame.isStereo();
        
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if     ( sampleRate >= 44000 ) rate = SWFConstants.SOUND_FREQ_44KHZ;
        else if( sampleRate >= 22000 ) rate = SWFConstants.SOUND_FREQ_22KHZ;
        else if( sampleRate >= 11000 ) rate = SWFConstants.SOUND_FREQ_11KHZ;
        
        SoundStreamHead head = new SoundStreamHead( rate, true, isStereo, SWFConstants.SOUND_FORMAT_MP3,
                                                    rate, true, isStereo, samplesPerSWFFrame );
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        while( frame != null )
        {        
            //--Write dummy sample count
            bout.write(0);
            bout.write(0);
            
            //--Write DelaySeek of zero
            bout.write(0);
            bout.write(0);
        
            int sampleCount = 0;
            int targetSampleCount = samplesPerSWFFrame * (blocks.size() + 1);
            
            while( frame != null && ( totalSamples + sampleCount < targetSampleCount ) )
            {
                sampleCount += frame.getSamplesPerFrame();
                frame.write( bout );
                frame = MP3Frame.readFrame( mp3 );
            }
            
            bout.flush();
            byte[] bytes = bout.toByteArray();
            bytes[0] = (byte)(sampleCount & 0xFF);
            bytes[1] = (byte)(sampleCount >> 8);
            
            totalSamples += sampleCount;
            
            blocks.add( bytes );
            bout.reset();            
        }
        
        mp3.close();
        
        double soundLength = ((double)totalSamples) / ((double)sampleRate);
        int requiredFrames = (int)(soundLength * framesPerSecond);
        
        System.out.println( "Required=" + requiredFrames + " actual=" + blocks.size() );
        
        //--Add null blocks to the end to make up the correct number of SWF frames
        while( blocks.size() < requiredFrames ) blocks.add( null );
        
        return head;
    }
    
    /**
     * Makes a streaming SWF from an MP3.
     * args[0] = MP3 in filename
     * args[1] = SWF out filename
     */
    public static void main( String[] args ) throws IOException
    {
        FileInputStream mp3 = new FileInputStream( args[0] );
        
        ArrayList blocks = new ArrayList();
        SoundStreamHead head = MP3Helper.streamingBlocks( mp3, 30, blocks );
        
        Movie movie = new Movie();
        movie.setFrameRate( 30 );

        Frame f = movie.appendFrame();
        f.setSoundHeader( head );
        
        for( Iterator i = blocks.iterator(); i.hasNext(); )
        {
            byte[] data = (byte[])i.next();
            f.setSoundData( data );
            f = movie.appendFrame();
        }
        
        movie.write( args[1] );
    }
}
