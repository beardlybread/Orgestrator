parser grammar OrgParser;

options { tokenVocab = OrgLexer; }

file : thing* EOF ;
thing : line
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
timestamp : TIMESTAMP DATE DOW TIME? REPEAT? END_TIMESTAMP ; 

propertyList : INDENT PROPERTIES (property)* END_PROPERTIES ;
property : propertyPair | lastRepeat ;
propertyPair : PROPERTY VALUE? ;
lastRepeat : LAST_REPEAT DATE DOW TIME END_TIMESTAMP ;

