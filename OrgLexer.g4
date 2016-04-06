lexer grammar OrgLexer;

fragment IL : [0-9]+ '. ' ;
fragment NL : '\r'? '\n' ;
fragment WS : [ \t]+ ;
fragment DT : [1-9][0-9][0-9][0-9] '-' [0-1][0-9] '-' [0-3][0-9] ;
fragment DW : 'Sun' | 'Mon' | 'Tue' | 'Wed' | 'Thu' | 'Fri' | 'Sat' ;
fragment TM : [0-2][0-9] ':' [0-5][0-9] ;

HEADING_LEVEL : '*'+ ' ' -> mode(HEADING_MODE) ;
INDENT : ' '+ -> mode(INDENT_MODE) ;
TABLE : '|' -> mode(TABLE_MODE) ;
ULIST : [\-+] ' ' -> mode(LINE_MODE) ;
ILIST : IL -> mode(LINE_MODE) ;
Line : . -> more, mode(LINE_MODE) ;
EMPTY : NL ;

mode HEADING_MODE;

TODO : ('TODO'|'DONE') ' ' -> mode(LINE_MODE) ;
HEADING : NL -> mode(DEFAULT_MODE) ;
HeaderMore : . -> more ;

mode INDENT_MODE;

SCHEDULE : ('SCHEDULED'|'DEADLINE'|'CLOSED') ':' -> mode(SCHEDULE_MODE) ;
PROPERTIES : ':PROPERTIES:' -> mode(PROPERTIES_MODE) ;
Table : '|' -> mode(TABLE_MODE), type(TABLE) ;
UList : [\-+*] ' ' -> mode(LINE_MODE), type(ULIST) ;
IList : IL -> mode(LINE_MODE), type(ILIST) ;
LineIndent : . -> more, mode(LINE_MODE) ;

mode TABLE_MODE;

END_TABLE : NL -> mode(DEFAULT_MODE) ;
TABLE_COL : ('\\|'|~[|\r\n])* ;
TABLE_SEP : '|' ;

mode LINE_MODE;

LINE : NL -> mode(DEFAULT_MODE) ;
LineMore : . -> more ;

mode SCHEDULE_MODE;

END_SCHEDULE : NL -> mode(DEFAULT_MODE) ;
TIMESTAMP : '<' -> pushMode(DATE_MODE) ;
B_timestamp : '[' -> pushMode(DATE_MODE), type(TIMESTAMP) ;
DEADLINE : 'DEADLINE:' ;
SCHEDULED : 'SCHEDULED:' ;
Schedule_WS : WS -> skip ;

mode PROPERTIES_MODE;

END_PROPERTIES : ':END:' -> mode(DEFAULT_MODE) ;
LAST_REPEAT : ':LAST_REPEAT:' [ \t]* '[' -> pushMode(DATE_MODE) ;
PROPERTY : ':' [_A-Z]+ ':' ;
VALUE : ~[ \t\r\n]+ ;
Properties_WS : [ \t\r\n]+ -> skip ;

mode DATE_MODE;

END_TIMESTAMP : ('>' | ']') -> popMode ;
DATE : DT ;
DOW : DW ;
TIME : TM ;
REPEAT : [+.]? '+' [0-9]+? [hdwmy] ;
Date_WS : WS -> skip ;

