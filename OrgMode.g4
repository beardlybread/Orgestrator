lexer grammar OrgMode;

fragment UL : [\-+] ' ' ;
fragment IL : [0-9]+ '. ' ;
fragment NL : '\r'? '\n' ;

HEADER_LEVEL : '*'+ ' ' -> mode(HEADER_MODE) ; // 1
INDENT : ' '+ -> mode(INDENT_MODE) ;           // 2
BEGIN_TABLE_ROW : '|' -> mode(TABLE_MODE) ;    // 3
ULIST : [\-+] ' ' -> mode(LINE_MODE) ;         // 4         
ILIST : IL -> mode(LINE_MODE) ;                // 5
Line : . -> more, mode(LINE_MODE) ;
EMPTY : NL ;                                   // 6

mode HEADER_MODE;

HEADER : '\n' -> mode(DEFAULT_MODE) ;          // 7
HeaderMore : . -> more ;

mode INDENT_MODE;

Table : '|' -> mode(TABLE_MODE), type(BEGIN_TABLE_ROW) ;
UList : [\-+*] ' ' -> mode(LINE_MODE), type(ULIST) ;
IList : IL -> mode(LINE_MODE), type(ILIST) ;
LineIndent : . -> more, mode(LINE_MODE) ;

mode TABLE_MODE;

END_TABLE_ROW : NL -> mode(DEFAULT_MODE) ; // 8
TABLE_COL : ('\\|' | ~[|\r\n])* ;          // 9
TABLE_SEP : '|' ;                          // 10

mode LINE_MODE;

LINE : NL -> mode(DEFAULT_MODE) ;          // 11
LineMore : . -> more ;

