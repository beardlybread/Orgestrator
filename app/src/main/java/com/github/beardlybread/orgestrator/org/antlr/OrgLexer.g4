/*
The MIT License (MIT)

Copyright (c) 2016 Bradley Steinbacher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

/** This grammar generates tokens from Org-mode files.
 *
 * If the lexer is being used with Orgestrator, its output's package declaration must be adjusted
 * to com.github.beardlybread.orgestrator.org.antlr for it to work correctly in that context.
 */
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
EMPTY : NL ;
Line : . -> more, mode(LINE_MODE) ;

mode HEADING_MODE;

TODO : ('TODO'|'DONE') ' ' -> mode(HEADING_MODE) ;
HEADING : NL -> mode(DEFAULT_MODE) ;
HeaderMore : . -> more ;

mode INDENT_MODE;

SCHEDULED : 'SCHEDULED:' -> mode(SCHEDULE_MODE) ;
DEADLINE : 'DEADLINE:' -> mode(SCHEDULE_MODE) ;
CLOSED : 'CLOSED:' -> mode(SCHEDULE_MODE) ;
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
CLOSED_DEADLINE : 'DEADLINE:' ;
CLOSED_SCHEDULED : 'SCHEDULED:' ;
Schedule_WS : WS -> type(EMPTY), skip ;

mode PROPERTIES_MODE;

END_PROPERTIES : ':END:' [ \t]* '\n' -> mode(DEFAULT_MODE) ;
LAST_REPEAT : ':LAST_REPEAT:' [ \t]* '[' -> pushMode(DATE_MODE) ;
PROPERTY : ':' [_A-Z]+ ':' ;
VALUE : ~[ \t\r\n]+ ;
Properties_WS : [ \t\r\n]+ -> type(EMPTY), skip ;

mode DATE_MODE;

END_TIMESTAMP : ('>' | ']') -> popMode ;
DATE : DT ;
DOW : DW ;
TIME : TM ;
REPEAT : [+.]? '+' [0-9]+? [hdwmy] ;
Date_WS : WS -> type(EMPTY), skip ;

