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
import com.anotherbigidea.flash.interfaces.*;
import com.anotherbigidea.flash.structs.*;
import com.anotherbigidea.flash.SWFConstants;

/**
 * A Font Symbol.
 * 
 * The Font references a FontDefinition object from which it takes the glyph
 * definitions it needs.
 */
public class Font extends Symbol 
{
    public class NoGlyphException extends Exception
    {
        public int code;
        
        public NoGlyphException( int code )
        {
            super( "The font does not have a glyph definition for code " + code );
            this.code = code;
        }
    }
    
    /**
     * A set of contiguous characters in one font.
     */
    public class Chars
    {
        protected String chars;
        protected double size;
        protected int[] indices;
        protected int[] advances;
        
        protected double totalAdvance; //total advance
        protected double ascent;
        protected double descent;
        protected double leftMargin;
        protected double rightMargin;
        
        public String toString()        { return chars; }
        public Font   getFont()         { return Font.this; }
        public double getSize()         { return size; }
        public double getTotalAdvance() { return totalAdvance; }
        public double getAscent()       { return ascent; }
        public double getDescent()      { return descent; }
        
        /**
         * The left margin is the difference between the origin of the
         * first glyph and the left edge of its geometry
         */
        public double getLeftMargin()   { return leftMargin; }
        
        /**
         * The right margin is the different between the total advance and
         * the right edge of the geometry of the last glyph
         */
        public double getRightMargin()  { return rightMargin; }
        
        /**
         * @param chars the characters to display (displayable chars only - i.e. no newlines, tabs etc..)
         * @param font may be null if no change of font is required
         * @param size point-size - only relevant if font is not null
         * @param color may be null if no color change is required.  May be AlphaColor.
         * @param x new X position for text - only valid if hasX is true
         * @param y new Y position for text - only valid if hasY is true
         */
        protected Chars( String chars, double size ) throws NoGlyphException 
        {
            this.chars = chars;
            this.size  = size;
            init();
        }        
        
        protected void init() throws NoGlyphException 
        {            
            //--Figure out the indices and advances
            char[] codes = chars.toCharArray();
            indices  = new int[ codes.length ];
            advances = new int[ codes.length ];

            double maxAscent  = 0.0;
            double maxDescent = 0.0;
            
            double scale = size * SWFConstants.TWIPS / 1024.0;
        
            for( int i = 0; i < codes.length; i++ )
            {
                int code = (int)codes[i];
                
                int[] index = new int[1];
                FontDefinition.Glyph glyph = getGlyph( code, index );
                
                indices[i] = index[0];
                
                if( glyph != null )
                {
                    Shape shape = glyph.getShape();
                    
                    double[] outline = shape.getBoundingRectangle();
                    double x1 = outline[0] * scale;
                    double y1 = outline[1] * scale;
                    double x2 = outline[2] * scale;
                    double y2 = outline[3] * scale;      

                    if( maxAscent  < -y1 ) maxAscent  = -y1;
                    if( maxDescent <  y2 ) maxDescent =  y2;
                    
                    double advance = glyph.getAdvance() * scale;
                    if( advance == 0 ) advance = x2 - x1;

                    //Kerning adjustment
                    if( i < codes.length-1 )
                    {
                        advance += (fontDef.getKerningOffset( code, (int)codes[i+1] ) * scale );
                    }
                    
                    totalAdvance += advance;
                                    
                    advances[i] = (int)(advance * SWFConstants.TWIPS); 
                    
                    if( i == 0 ) leftMargin = -y1;
                    if( i == codes.length - 1 ) rightMargin = x2 - advance;
                }
            }

            ascent = fontDef.getAscent() * scale;
            if( ascent == 0.0 ) ascent = maxAscent;
            
            descent = fontDef.getDescent() * scale;
            if( descent == 0.0 ) descent = maxDescent;            
        }        
    }    
    
    protected Object font1Key = new Object(); //used in movie defined symbols lookup
    protected Object font2Key = new Object(); //used in movie defined symbols lookup
    
    protected FontDefinition fontDef;
    protected HashMap glyphs  = new HashMap(); //glyphs used by this font
    protected HashMap indices = new HashMap(); //glyph indices
    protected ArrayList glyphList = new ArrayList();
    
    public FontDefinition getDefinition() { return fontDef; }
    
    public Font( FontDefinition fontDef )
    {
        this.fontDef = fontDef;
    }
    
    public List getGlyphList() { return glyphList; }
    
    /**
     * Load the glyphs for the characters in the given string from the
     * FontDefinition into this font.
     * @exception NoGlyphException
     */
    public void loadGlyphs( String chars ) throws NoGlyphException 
    {
        char[] chs = chars.toCharArray();
        for( int i = 0; i < chs.length; i++ )
        {
            getGlyph( chs[i], null );
        }
    }

    /**
     * Load all glyphs from the font definition
     */
    public void loadAllGlyphs()
    {
        ArrayList list = fontDef.getGlyphList();
        
        for( Iterator it = list.iterator(); it.hasNext(); )
        {
            FontDefinition.Glyph g = (FontDefinition.Glyph)it.next();
            
            addGlyph( g );
        }
    }
    
    /**
     * Get the Chars instance for the given string at the given font size
     */
    public Chars chars( String chars, double fontSize )
        throws NoGlyphException 
    {
        return new Chars( chars, fontSize );
    }
    
    protected FontDefinition.Glyph getGlyph( int code, int[] index )
        throws NoGlyphException 
    {
        Integer codeI = new Integer(code);
        FontDefinition.Glyph g = (FontDefinition.Glyph)glyphs.get( codeI );
        
        if( g != null ) 
        {
            if( index != null )
            {
                Integer idx = (Integer)indices.get( codeI );
                index[0] = idx.intValue();
            }
            
            return g;
        }
        
        g = fontDef.getGlyph( code );
        
        if( g == null ) throw new NoGlyphException( code );
        
        int idx = addGlyph( g );
        if( index != null ) index[0] = idx;

        return g;
    }

    /**
     * Add a glyph and return the index
     */
    public int addGlyph( FontDefinition.Glyph glyph )
    {
        int idx = glyphs.size();
        
        if( glyph.getCode() > 0 )
        {
            Integer codeI = new Integer( glyph.getCode() );
            indices.put( codeI, new Integer( idx ));
            glyphs.put( codeI, glyph );
        }
        
        glyphList.add( glyph );
        
        return idx;
    }
    
    /**
     * Set the code for the glyph at the given index
     */
    public void setCode( int index, int code )
    {
        if( index >= glyphList.size() ) return;

        FontDefinition.Glyph g = (FontDefinition.Glyph)glyphList.get( index );
        g.setCode( code );
        
        Integer codeI = new Integer( code );
        indices.put( codeI, new Integer( index ));
        glyphs.put( codeI, g );        
    }
    
    protected int define( boolean textFont, Movie movie, SWFTagTypes tagwriter )
        throws IOException
    {
        Integer integerId = textFont ? 
                                (Integer)movie.definedSymbols.get( font1Key ) :
                                (Integer)movie.definedSymbols.get( font2Key );
                
        if( integerId == null )
        {
            if( textFont )
            {
                integerId = new Integer( defineFont1( movie, tagwriter ) );
                movie.definedSymbols.put( font1Key, integerId );
            }
            else
            {
                integerId = new Integer( defineFont2( movie, tagwriter ) );
                movie.definedSymbols.put( font2Key, integerId );
            }
        }

        id = integerId.intValue();
        return id;        
    }

    protected int defineFont1( Movie movie, SWFTagTypes tagwriter ) 
        throws IOException
    {
        int id = getNextId( movie );
        
        SWFVectors vecs = tagwriter.tagDefineFont( id, glyphList.size() );
        
        for( Iterator it = glyphList.iterator(); it.hasNext(); )
        {
            FontDefinition.Glyph g = (FontDefinition.Glyph)it.next();
            
            Shape s = g.getShape();
            
            s.writeGlyph( vecs );
        }
      
        if( fontDef.getName() != null )
        {
            int flags = 0;

            if( fontDef.isUnicode()  ) flags |= SWFConstants.FONT_UNICODE;
            if( fontDef.isShiftJIS() ) flags |= SWFConstants.FONT_SHIFTJIS;
            if( fontDef.isAnsi()     ) flags |= SWFConstants.FONT_ANSI;
            if( fontDef.isItalic()   ) flags |= SWFConstants.FONT_ITALIC;
            if( fontDef.isBold()     ) flags |= SWFConstants.FONT_BOLD;
            
            tagwriter.tagDefineFontInfo( id, fontDef.getName(), flags, getCodes() );
        }
        
        return id;
    }
    
    protected int defineFont2( Movie movie, SWFTagTypes tagwriter ) 
        throws IOException
    {
        int id = getNextId( movie );

        int glyphCount  = glyphList.size();
        int[]  codes    = new int [ glyphCount ];
        Rect[] bounds   = new Rect[ glyphCount ];
        int[]  advances = new int [ glyphCount ];
        
        //--Gather glyph info
        int i = 0;
        for( Iterator it = glyphList.iterator(); it.hasNext(); )
        {
            FontDefinition.Glyph g = (FontDefinition.Glyph)it.next();
            
            codes[i]    = g.getCode();
            advances[i] = (int)(g.getAdvance() * SWFConstants.TWIPS);
            
            double[] bound = g.getShape().getBoundingRectangle();
            
            bounds[i] = new Rect( (int)(bound[0] * SWFConstants.TWIPS),
                                  (int)(bound[1] * SWFConstants.TWIPS),
                                  (int)(bound[2] * SWFConstants.TWIPS),
                                  (int)(bound[3] * SWFConstants.TWIPS) );
            
            i++;
        }
        
        //--Gather kerning info
        ArrayList kerns = fontDef.getKerningPairList();
        int kernCount = kerns.size();
        int[] kern1   = new int[ kernCount ];
        int[] kern2   = new int[ kernCount ];
        int[] kernOff = new int[ kernCount ];
        
        i = 0;
        for( Iterator it = kerns.iterator(); it.hasNext(); )
        {
            FontDefinition.KerningPair pair = (FontDefinition.KerningPair)it.next();
            
            kern1[i]   = pair.getCode1();
            kern2[i]   = pair.getCode2();
            kernOff[i] = (int)(pair.getAdjustment() * SWFConstants.TWIPS );
                
            i++;
        }
        
        int flags = 0;
        if( fontDef.hasMetrics() ) flags |= SWFConstants.FONT2_HAS_LAYOUT;
        if( fontDef.isShiftJIS() ) flags |= SWFConstants.FONT2_SHIFTJIS;
        if( fontDef.isUnicode()  ) flags |= SWFConstants.FONT2_UNICODE;
        if( fontDef.isAnsi()     ) flags |= SWFConstants.FONT2_ANSI;
        if( fontDef.isItalic()   ) flags |= SWFConstants.FONT2_ITALIC;
        if( fontDef.isBold()     ) flags |= SWFConstants.FONT2_BOLD; 
        
        SWFVectors vecs = tagwriter.tagDefineFont2( 
                                  id, flags, fontDef.getName(), glyphCount,
                                  (int)(fontDef.getAscent() * SWFConstants.TWIPS),
                                  (int)(fontDef.getDescent() * SWFConstants.TWIPS),
                                  (int)(fontDef.getLeading() * SWFConstants.TWIPS),
                                  codes, advances, bounds, kern1, kern2, kernOff );

        for( Iterator it = glyphList.iterator(); it.hasNext(); )
        {
            FontDefinition.Glyph g = (FontDefinition.Glyph)it.next();
            
            Shape s = g.getShape();
            
            s.writeGlyph( vecs );
        }
        
        return id;
    }
    
    /**
     * Get the codes of the current set of glyphs
     */
    protected int[] getCodes()
    {
        int[] codes = new int[glyphList.size()];
        
        for( int i = 0; i < codes.length; i++ )
        {
            FontDefinition.Glyph g = (FontDefinition.Glyph)glyphList.get(i);
            codes[i] = g.getCode();
        }
        
        return codes;
    }
    
    protected int defineSymbol( Movie movie, 
                                SWFTagTypes timelineWriter,
                                SWFTagTypes definitionwriter ) throws IOException
    {
        return id;
    }
}
