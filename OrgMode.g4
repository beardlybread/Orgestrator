lexer grammar OrgMode;

fragment IL : [0-9]+ '. ' ;
fragment NL : '\r'? '\n' ;
fragment DOW : 'Sun' | 'Mon' | 'Tue' | 'Wed' | 'Thu' | 'Fri' | 'Sat' ;
fragment DATE : [1-9][0-9][0-9][0-9] '-' [0-1][0-9] '-' [0-3][0-9] ;

HEADING_LEVEL : '*'+ ' ' -> mode(HEADING_MODE) ; // 1
INDENT : ' '+ -> mode(INDENT_MODE) ;             // 2
TABLE : '|' -> mode(TABLE_MODE) ;                // 3
ULIST : [\-+] ' ' -> mode(LINE_MODE) ;           // 4         
ILIST : IL -> mode(LINE_MODE) ;                  // 5
Line : . -> more, mode(LINE_MODE) ;
EMPTY : NL ;                                     // 6

mode HEADING_MODE;

TODO : ('TODO'|'DONE') ' ' -> mode(LINE_MODE) ; // 7
HEADING : NL -> mode(DEFAULT_MODE) ;            // 8
HeaderMore : . -> more ;

mode INDENT_MODE;

SCHEDULE : ('SCHEDULED'|'DEADLINE'|'CLOSED') ':' -> mode(SCHEDULE_MODE) ; // 9
PROPERTIES : ':PROPERTIES:' -> mode(PROPERTIES_MODE) ;                    // 10
Table : '|' -> mode(TABLE_MODE), type(TABLE) ;
UList : [\-+*] ' ' -> mode(LINE_MODE), type(ULIST) ;
IList : IL -> mode(LINE_MODE), type(ILIST) ;
LineIndent : . -> more, mode(LINE_MODE) ;

mode TABLE_MODE;

END_TABLE : NL -> mode(DEFAULT_MODE) ; // 11
TABLE_COL : ('\\|'|~[|\r\n])* ;        // 12
TABLE_SEP : '|' ;                      // 13

mode LINE_MODE;

LINE : NL -> mode(DEFAULT_MODE) ; // 14
LineMore : . -> more ;

mode SCHEDULE_MODE;

END_SCHEDULE : NL -> mode(DEFAULT_MODE) ;  // 15
ANGLE_TS : '<' DATE ' ' DOW ' '? .*? '>' ; // 16
BRACK_TS : '[' DATE ' ' DOW ' '? .*? ']' ; // 17
DEADLINE : 'DEADLINE:' ;                   // 18
SCHEDULED : 'SCHEDULED:' ;                 // 19
Schedule_WS : [ \t]+ -> skip ;

mode PROPERTIES_MODE;

END_PROPERTIES : ':END:' .*? NL -> mode(DEFAULT_MODE) ; // 20
PROPERTY : ':' [_A-Z]+ ':' .*? NL ;                     // 21

