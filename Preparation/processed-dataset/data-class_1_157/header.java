void method0() { 
/**
     *
     */
protected static final String VM_EXT = ".vm";
/**
     *
     */
protected static final String WM_EXT = ".wm";
/**
     * The regexes to use for line by line substition. The regexes
     * come in pairs. The first is the string to match, the second is
     * the substitution to make.
     */
protected static String[] perLineREs = { // Make #if directive match the Velocity directive style. 
"#if\\s*[(]\\s*(.*\\S)\\s*[)]\\s*(#begin|{)[ \\t]?", "#if( $1 )", // Remove the WM #end #else #begin usage. 
"[ \\t]?(#end|})[ \\t]*\n(\\s*)#else\\s*(#begin|{)[ \\t]?(\\w)", "$2#else#**#$4", // avoid touching followup word with embedded comment 
"[ \\t]?(#end|})[ \\t]*\n(\\s*)#else\\s*(#begin|{)[ \\t]?", "$2#else", "(#end|})(\\s*#else)\\s*(#begin|{)[ \\t]?", "$1\n$2", // Convert WM style #foreach to Velocity directive style. 
"#foreach\\s+(\\$\\w+)\\s+in\\s+(\\$[^\\s#]+)\\s*(#begin|{)[ \\t]?", "#foreach( $1 in $2 )", // Convert WM style #set to Velocity directive style. 
"#set\\s+(\\$[^\\s=]+)\\s*=\\s*([\\S \\t]+)", "#set( $1 = $2 )", "(##[# \\t\\w]*)\\)", // fix comments included at end of line 
")$1", // Convert WM style #parse to Velocity directive style. 
"#parse\\s+([^\\s#]+)[ \\t]?", "#parse( $1 )", // Convert WM style #include to Velocity directive style. 
"#include\\s+([^\\s#]+)[ \\t]?", "#include( $1 )", // Convert WM formal reference to VTL syntax. 
"\\$\\(([^\\)]+)\\)", "${$1}", "\\${([^}\\(]+)\\(([^}]+)}\\)", // fix encapsulated brakets: {(}) 
"${$1($2)}", // Velocity currently does not permit leading underscore. 
"\\$_", "$l_", "\\${(_[^}]+)}", // within a formal reference 
"${l$1}", // Eat semi-colons in (converted) VTL #set directives. 
"(#set\\s*\\([^;]+);(\\s*\\))", "$1$2", // Convert explicitly terminated WM statements to VTL syntax. 
"(^|[^\\\\])\\$(\\w[^=\n;'\"]*);", "$1${$2}", // Change extensions when seen. 
"\\.wm", ".vm" };
}
