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

/** This grammar parses tokens generated from OrgLexer.
 *
 * If the parser is being used with Orgestrator, its output's package declaration must be adjusted
 * to com.github.beardlybread.orgestrator.org.antlr for it to work correctly in that context.
 */
parser grammar OrgParser;

options { tokenVocab = OrgLexer; }

file : thing* EOF ;
thing : empty
      | line
      | headingLine
      | todoLine
      | table
      | unenumeratedLine
      | enumeratedLine
      | event
      | propertyList
      ;

line : INDENT? LINE | EMPTY ;

headingLine : HEADING_LEVEL HEADING ;
todoLine : HEADING_LEVEL TODO HEADING ;

table : tableRow+ ;
tableRow : INDENT? TABLE (tableCol)+ END_TABLE ;
tableCol : TABLE_COL? TABLE_SEP ;

unenumeratedLine : INDENT? ULIST LINE ;
enumeratedLine : INDENT? ILIST LINE ;

event : scheduled | deadline | closed ;
scheduled : INDENT SCHEDULED timestamp END_SCHEDULE ;
deadline : INDENT DEADLINE timestamp END_SCHEDULE ;
closed : INDENT CLOSED timestamp
  ((CLOSED_DEADLINE | CLOSED_SCHEDULED) timestamp)? END_SCHEDULE ;
timestamp : TIMESTAMP date END_TIMESTAMP ;
date : DATE DOW TIME? REPEAT? ;

propertyList : INDENT PROPERTIES (property)* END_PROPERTIES ;
property : propertyPair | lastRepeat ;
propertyPair : PROPERTY VALUE? ;
lastRepeat : LAST_REPEAT date END_TIMESTAMP ;

empty : EMPTY ;
