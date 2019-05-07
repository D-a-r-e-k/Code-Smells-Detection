void method0() { 
// transformation table for characters 128 to 255. These actually fall into two  
// groups, put together for efficiency: "Windows" chacacters 128-159 such as  
// "smart quotes", which are encoded to valid Unicode entities, and  
// valid ISO-8859 caracters 160-255, which are encoded to the symbolic HTML  
// entity. Everything >= 256 is encoded to a numeric entity.  
//  
// for mor on HTML entities see http://www.pemberley.com/janeinfo/latin1.html  and  
// ftp://ftp.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1252.TXT  
//  
static final String[] transform = { "&euro;", // 128  
"", // empty string means character is undefined in unicode  
"&#8218;", "&#402;", "&#8222;", "&#8230;", "&#8224;", "&#8225;", "&#710;", "&#8240;", "&#352;", "&#8249;", "&#338;", "", "&#381;", "", "", "&#8216;", "&#8217;", "&#8220;", "&#8221;", "&#8226;", "&#8211;", "&#8212;", "&#732;", "&#8482;", "&#353;", "&#8250;", "&#339;", "", "&#382;", "&#376;", // 159  
"&nbsp;", // 160  
"&iexcl;", "&cent;", "&pound;", "&curren;", "&yen;", "&brvbar;", "&sect;", "&uml;", "&copy;", "&ordf;", "&laquo;", "&not;", "&shy;", "&reg;", "&macr;", "&deg;", "&plusmn;", "&sup2;", "&sup3;", "&acute;", "&micro;", "&para;", "&middot;", "&cedil;", "&sup1;", "&ordm;", "&raquo;", "&frac14;", "&frac12;", "&frac34;", "&iquest;", "&Agrave;", "&Aacute;", "&Acirc;", "&Atilde;", "&Auml;", "&Aring;", "&AElig;", "&Ccedil;", "&Egrave;", "&Eacute;", "&Ecirc;", "&Euml;", "&Igrave;", "&Iacute;", "&Icirc;", "&Iuml;", "&ETH;", "&Ntilde;", "&Ograve;", "&Oacute;", "&Ocirc;", "&Otilde;", "&Ouml;", "&times;", "&Oslash;", "&Ugrave;", "&Uacute;", "&Ucirc;", "&Uuml;", "&Yacute;", "&THORN;", "&szlig;", "&agrave;", "&aacute;", "&acirc;", "&atilde;", "&auml;", "&aring;", "&aelig;", "&ccedil;", "&egrave;", "&eacute;", "&ecirc;", "&euml;", "&igrave;", "&iacute;", "&icirc;", "&iuml;", "&eth;", "&ntilde;", "&ograve;", "&oacute;", "&ocirc;", "&otilde;", "&ouml;", "&divide;", "&oslash;", "&ugrave;", "&uacute;", "&ucirc;", "&uuml;", "&yacute;", "&thorn;", "&yuml;" };
static final HashSet allTags = new HashSet();
// HTML block tags need to suppress automatic newline to <br>  
// conversion around them to look good. However, they differ  
// in how many newlines around them should ignored. These sets  
// help to treat each tag right in newline conversion.  
static final HashSet internalTags = new HashSet();
static final HashSet blockTags = new HashSet();
static final HashSet semiBlockTags = new HashSet();
// set of tags that are always empty  
static final HashSet emptyTags = new HashSet();
static final byte TAG_NAME = 0;
static final byte TAG_SPACE = 1;
static final byte TAG_ATT_NAME = 2;
static final byte TAG_ATT_VAL = 3;
static final byte TEXT = 0;
static final byte SEMIBLOCK = 1;
static final byte BLOCK = 2;
static final byte INTERNAL = 3;
static final String newLine = System.getProperty("line.separator");
}
