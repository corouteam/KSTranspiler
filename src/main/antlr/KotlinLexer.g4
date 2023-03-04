lexer grammar KotlinLexer;


// Whitespace
NL                  : '\r\n' | 'r' | '\n' ;
WS                 : [\t ]+ -> skip ;

// Keywords
VAR                : 'var' ;
VAL                : 'val' ;
PRINT              : 'print';
AS                 : 'as';
INT                : 'Int';
DOUBLE             : 'Double';
BOOL               : 'Boolean';
STRING             : 'String';

INT_LIT
    : DecDigitNoZero DecDigit*
    | DecDigit
     ;

BOOL_LIT : 'true' | 'false';

DOUBLE_LIT
     : DecDigits '.' DecDigits
     | DecDigits
     ;

FLOAT_LIT
    : DOUBLE_LIT [fF]
    | DecDigits [fF]
    ;

// Operators
PLUS               : '+' ;
MINUS              : '-' ;
ASTERISK           : '*' ;
DIVISION           : '/' ;
ASSIGN             : '=' ;
LPAREN             : '(' ;
RPAREN             : ')' ;
LBRAK              : '[' ;
RBRAK              : ']' ;
COLON              : ':' ;
LCURL              : '{' ;
RCURL              : '}' ;
SEMICOLON          : ';' ;
COMMA              : ',' ;
RANGE              : '..';
LANGLE             : '<' ;
RANGLE             : '>' ;
AT                 : '@' ;
DOT                : '.' ;

IF: 'if';
ELSE: 'else';
FOR: 'for';
IN: 'in';
FUN: 'fun';
PACKAGE: 'package';
RETURN                  : 'return' ;
LISTOF: 'listOf';
ARRAYOF: 'arrayOf';
WHILE              : 'while';
BREAK              : 'break';
CONTINUE           : 'continue';

//Colors
COLOR_LITERAL: '0'[xX][0-9a-fA-F]+;


// SECTION: strings

QUOTE_OPEN: '"' -> pushMode(LineString);

TEXT_COMPOSE: 'Text';

COLOR: 'Color';
COLOR_BLUE: 'Blue';

FONT_WEIGHT: 'FontWeight';
FONT_WEIGHT_BOLD: 'Bold';

FONT_WEIGHT_PARAM: 'fontWeight';
COLOR_PARAM: 'color';


// Identifiers
ID                 : [_]*[A-Za-z][A-Za-z0-9_]* ;

mode LineString;

QUOTE_CLOSE
    : '"' -> popMode
    ;

LineStrText
    : ~('\\' | '"' | '$')+ | '$'
    ;

mode Inside;
Inside_QUOTE_OPEN: QUOTE_OPEN -> pushMode(LineString), type(QUOTE_OPEN);

// Literals
fragment DecDigit: '0'..'9';
fragment DecDigitNoZero: '1'..'9';
fragment DecDigitOrSeparator: DecDigit | '_';

fragment DecDigits
    : DecDigit DecDigitOrSeparator* DecDigit
    | DecDigit
    ;