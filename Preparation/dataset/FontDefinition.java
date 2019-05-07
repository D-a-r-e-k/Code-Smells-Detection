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

import java.util.*;

/**
 * A Font Definition that can referenced by Font symbols.
 * If read in from an existing Flash movie the font definition may only contain
 * a subset of the glyphs in the font.
 * 
 * To use a system font set the hasMetrics flag to false.
 */
public class FontDefinition
{
    /**
     * A Glyph within the font.
     */
    public static class Glyph
    {
        protected int code;
        protected double advance;
        protected Shape  shape;
        
        public Shape  getShape() { return shape; }
        public int    getCode() { return code; }
        public double getAdvance() { return advance; }
        
        public void setShape( Shape shape ) { this.shape = shape; }
        public void setCode( int code ) { this.code = code; }
        public void setAdvance( double advance ) { this.advance = advance; }
                
        public Glyph( Shape shape, double advance, int code )
        {
            this.shape   = shape;
            this.advance = advance;
            this.code    = code;
        }
    }
    
    /**
     * A Kerning Pair is an adjustment to the advance between two particular glyphs.
     */
    public static class KerningPair
    {
        protected int code1, code2;
        protected double adjustment;
        
        public int getCode1() { return code1; }
        public int getCode2() { return code2; }
        public double getAdjustment() { return adjustment; }
        
        public void setCode1( int code ) { code1 = code; }
        public void setCode2( int code ) { code2 = code; }
        public void setAdjustment( double offset ) { adjustment = offset; }
        
        public KerningPair( int code1, int code2, double adjustment )
        {
            this.code1 = code1;
            this.code2 = code2;
            this.adjustment = adjustment;
        }
    }
    
    protected String name;
    protected double ascent;
    protected double descent;
    protected double leading;
    
    protected boolean isUnicode;
    protected boolean isShiftJIS;
    protected boolean isAnsi;
    protected boolean isItalic;
    protected boolean isBold;
    protected boolean hasMetrics;
        
    protected ArrayList glyphs  = new ArrayList();
    protected ArrayList kerning = new ArrayList();
    
    protected HashMap glyphLookup;
    protected HashMap kernLookup;
    
    public String getName() { return name; }
    public double getAscent() { return ascent; }
    public double getDescent() { return descent; }
    public double getLeading() { return leading; }
    
    public boolean isUnicode()  { return isUnicode; }
    public boolean isShiftJIS() { return isShiftJIS; }
    public boolean isAnsi()     { return isAnsi; }
    public boolean isItalic()   { return isItalic; }  
    public boolean isBold()     { return isBold; }
    public boolean hasMetrics() { return hasMetrics; }

    /**
     * Get the List of Glyph objects
     */
    public ArrayList getGlyphList() { return glyphs; }
    
    /**
     * Get the List of KerningPair objects
     */
    public ArrayList getKerningPairList() { return kerning; }
    
    public void setName( String name ) { this.name = name; }
    public void setAscent ( double ascent ) { this.ascent = ascent; }
    public void setDescent( double descent ) { this.descent = descent; }
    public void setLeading( double leading ) { this.leading = leading; }
    
    public void setFontFlags( boolean isUnicode, boolean isShiftJIS, boolean isAnsi,
                              boolean isItalic, boolean isBold, boolean hasMetrics )
    {
        this.isUnicode  = isUnicode;
        this.isShiftJIS = isShiftJIS;
        this.isAnsi     = isAnsi;
        this.isItalic   = isItalic;
        this.isBold     = isBold;
        this.hasMetrics = hasMetrics;
    }
    
    public FontDefinition(){}
    
    public FontDefinition( String name, double ascent, double descent, double leading,
                            boolean isUnicode, boolean isShiftJIS, boolean isAnsi,
                            boolean isItalic, boolean isBold, boolean hasMetrics )
    {
        this.name    = name;
        this.ascent  = ascent;
        this.descent = descent;
        this.leading = leading;
        
        this.isUnicode  = isUnicode;
        this.isShiftJIS = isShiftJIS;
        this.isAnsi     = isAnsi;
        this.isItalic   = isItalic;
        this.isBold     = isBold;
        this.hasMetrics = hasMetrics;        
    }    
    
    /**
     * Look up a glyph by code
     * @return null if the code has no glyph
     */
    public Glyph getGlyph( int code )
    {        
        if( glyphLookup == null )
        {
            glyphLookup = new HashMap();
            
            for( Iterator it = glyphs.iterator(); it.hasNext(); )
            {
                Glyph g = (Glyph)it.next();
                
                glyphLookup.put( new Integer( g.code ), g );
            }
        }
        
        Glyph g = (Glyph)glyphLookup.get( new Integer( code ) );
        
        return g;
    }
    
    /**
     * Get the kerning adjustment required between the two given codes
     */
    public double getKerningOffset( int code1, int code2 )
    {        
        if( kernLookup == null )
        {
            kernLookup = new HashMap();
            
            for( Iterator it = kerning.iterator(); it.hasNext(); )
            {
                KerningPair pair = (KerningPair)it.next();
                Integer i1 = new Integer( pair.code1 );
                Integer i2 = new Integer( pair.code2 );
                
                HashMap kerns = (HashMap)kernLookup.get( i1 );
                
                if( kerns == null )
                {
                    kerns = new HashMap();
                    kernLookup.put( i1, kerns );
                }
                
                kerns.put( i2, pair );
            }
        }

        Integer i1 = new Integer( code1 );
        Integer i2 = new Integer( code2 );
        
        HashMap kerns = (HashMap)kernLookup.get( i1 );
        if( kerns == null ) return 0.0;
        
        KerningPair pair = (KerningPair)kerns.get( i2 );
        if( pair == null ) return 0.0;
        
        return pair.adjustment;
    }
}
