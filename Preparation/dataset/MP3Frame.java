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

/**
 * An MP3 sound data frame.
 */
public class MP3Frame
{
    public static final int MPEG_Version_2_5 = 0;
    public static final int MPEG_Version_2   = 2;
    public static final int MPEG_Version_1   = 3;
    
    public static final int MPEG_Layer_3 = 1;
    public static final int MPEG_Layer_2 = 2;
    public static final int MPEG_Layer_1 = 3;

    public static final int CHANNEL_MODE_STEREO       = 0;
    public static final int CHANNEL_MODE_JOINT_STEREO = 1;
    public static final int CHANNEL_MODE_DUAL_CHANNEL = 2;
    public static final int CHANNEL_MODE_MONO         = 3;
    
    public static final int EMPHASIS_NONE     = 0;
    public static final int EMPHASIS_50_15_MS = 1;
    public static final int EMPHASIS_RESERVED = 2;
    public static final int EMPHASIS_CCIT_J17 = 3;
    
    protected static final int[] MPEG1BitRates = { 0, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 0 }; 
    protected static final int[] MPEG2BitRates = { 0,  8000, 16000, 24000, 32000, 40000, 48000, 56000,  64000,  80000,  96000, 112000, 128000, 144000, 160000, 0 };
        
    protected static final int[] MPEG10SampleRates = { 44100, 48000, 32000, 0 };
    protected static final int[] MPEG20SampleRates = { 22050, 24000, 16000, 0 };
    protected static final int[] MPEG25SampleRates = { 11025, 12000,  8000, 0 };
    
    private static int FRAME_SAMPLES_MPEG_1 = 1152;
    private static int FRAME_SAMPLES_MPEG_2 = 576;    
    
    protected int     mpegVersion;
    protected int     mpegLayer;
    protected boolean isProtected;
    protected int     bitRate;
    protected int     sampleRate;
    protected boolean padded;
    protected int     channelMode;
    protected int     modeExtension;
    protected boolean copyrighted;
    protected boolean original;
    protected int     emphasis;
    protected byte[]  data;
    
    protected int bit_rate;
    protected int sample_rate;
    
    public int getBitRate()    { return bitRate; }
    public int getSampleRate() { return sampleRate; }
    public boolean isStereo()  { return channelMode != CHANNEL_MODE_MONO; }
    public int getDataLength() { return data.length; }
    
    public int getSamplesPerFrame() 
    { 
        if( mpegVersion == MPEG_Version_1 ) return FRAME_SAMPLES_MPEG_1;
        else return FRAME_SAMPLES_MPEG_2;
    }
    
    /**
     * Read the next MP3 frame from the stream - return null if no more
     */
    public static MP3Frame readFrame( InputStream in ) throws IOException
    {
        MP3Frame frame = new MP3Frame();
        
        while( true )
        {
            int b = in.read();
            
            if( b < 0 ) return null;
            if( b == 0xFF )
            {
                b = in.read();
 
                if( b < 0 ) return null;
                if( (b & 0xE0) != 0xE0 ) continue;

                frame.mpegVersion = ( b & 0x18 ) >> 3;
                frame.mpegLayer   = ( b & 0x06 ) >> 1;        
                frame.isProtected = ( b & 1 ) == 0;
 
                if( frame.mpegLayer != MPEG_Layer_3 ) continue;
                break;
            }
        }
 
        //skip the CRC
        if( frame.isProtected )
        {
            in.read();
            in.read();
        }
        
        int b = in.read();
        if( b < 0 ) return null;
        
        frame.bit_rate = ( b & 0xF0 ) >> 4;
         
        if( frame.mpegVersion == MPEG_Version_1 ) frame.bitRate = MPEG1BitRates[frame.bit_rate];
        else                                      frame.bitRate = MPEG2BitRates[frame.bit_rate];
        
        frame.sample_rate = ( b & 0x0C ) >> 2;
        if     ( frame.mpegVersion == MPEG_Version_1 ) frame.sampleRate = MPEG10SampleRates[frame.sample_rate];
        else if( frame.mpegVersion == MPEG_Version_2 ) frame.sampleRate = MPEG20SampleRates[frame.sample_rate];
        else                                           frame.sampleRate = MPEG25SampleRates[frame.sample_rate];
        
        frame.padded = ( b & 2 ) != 0;
        
        b = in.read();
        if( b < 0 ) return null;

        frame.channelMode   = ( b & 0xC0 ) >> 6;
        frame.modeExtension = ( b & 0x30 ) >> 4;
        
        frame.copyrighted = (( b & 0x80 ) >> 3) != 0;
        frame.original    = (( b & 0x40 ) >> 2) != 0;
        
        frame.emphasis = b & 0x02;
        
        int size = (((frame.mpegVersion == MPEG_Version_1 ? 144 : 72) 
                     * frame.bitRate ) / frame.sampleRate ) + 
                       (frame.padded ? 1 : 0) - 4;
        
        byte[] data = new byte[size];
        int read = 0;
        int r;
        
        while( (r = in.read( data, read, size - read )) >= 0 && read < size )
        {
            read += r;
        }
        
        if( read != size ) throw new IOException( "Unexpected end of MP3 data" );
        
        frame.data = data;   
       
        //System.out.print( "." );
        return frame;
    }
    
    public MP3Frame( ) {}
    
    public void write( OutputStream out ) throws IOException
    {
        out.write( 0xff );
                
        int b = 0xE1;
        b |= this.mpegVersion << 3;
        b |= this.mpegLayer   << 1;
        out.write( b );
        
        b  = bit_rate << 4;
        b |= sample_rate << 2;
        if( padded ) b |= 2;
        out.write( b );
        
        b  = channelMode   << 6;
        b |= modeExtension << 4;
        b |= emphasis;
        out.write( b );
        
        out.write( data );
    }
    
    public String toString()
    {
        String version = null;
        if     ( mpegVersion == MPEG_Version_1   ) version = "1";
        else if( mpegVersion == MPEG_Version_2   ) version = "2";
        else if( mpegVersion == MPEG_Version_2_5 ) version = "2.5";
        
        return "MP3 Frame: " +                
               " version=" + version +
               " bit-rate=" + bitRate +   
               " sample-rate=" + sampleRate +
               " stereo=" +  (channelMode != CHANNEL_MODE_MONO);
    }
}
