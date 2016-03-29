lexer grammar OrgMode;

fragment UL : [\-+] ' ' ;
fragment IL : [0-9]+ '. ' ;
fragment NL : '\r'? '\n' ;

HEADER_LEVEL : '*'+ ' ' -> mode(HEADER_MODE) ;
INDENT : ' '+ -> mode(INDENT_MODE) ;
BEGIN_TABLE_ROW : '|' -> mode(TABLE_MODE) ;
ULIST : UL -> mode(LINE_MODE) ;
ILIST : IL -> mode(LINE_MODE) ;
Line : . -> more, mode(LINE_MODE) ;
EMPTY : NL ;

mode HEADER_MODE;

HEADER : '\n' -> mode(DEFAULT_MODE) ;
HeaderMore : . -> more ;

mode INDENT_MODE;

Table : '|' -> mode(TABLE_MODE), type(BEGIN_TABLE_ROW) ;
UList : UL -> mode(LINE_MODE), type(ULIST) ;
IList : IL -> mode(LINE_MODE), type(ILIST) ;
LineIndent : . -> more, mode(LINE_MODE) ;

mode TABLE_MODE;

END_TABLE_ROW : NL -> mode(DEFAULT_MODE) ;
TABLE_COL : ('\\|' | ~[|\r\n])* ;
TABLE_SEP : '|' ;

mode LINE_MODE;

LINE : NL -> mode(DEFAULT_MODE) ;
LineMore : . -> more ;

