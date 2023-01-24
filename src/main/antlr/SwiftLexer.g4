lexer grammar SwiftLexer;


// Whitespace
NL                  : '\r\n' | 'r' | '\n' ;
WS                 : [\t ]+ -> skip ;

// Keywords
VAR                : 'var' ;
LET                : 'let' ;
PRINT              : 'print';
AS                 : 'as';
INT                : 'Int';
DOUBLE             : 'Double';
BOOL               : 'Bool';
STRING             : 'String';

INT_LIT
    : DecDigitNoZero DecDigit*
    | DecDigit
     ;

BOOL_LIT : 'true' | 'false';

DOUBLE_LIT
     : DecDigits? '.' DecDigits
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
COLON              : ':';
LCURL              : '{';
RCURL              : '}';
SEMICOLON          : ';';
COMMA              : ',';
AT                 : '@';
DOT                : '.';

IF: 'if';
ELSE: 'else';
FUN: 'func';
PACKAGE: 'package';
RETURN                  : 'return' ;

//Colors
COLOR_LITERAL: '0'[xX][0-9a-fA-F]+;


// SECTION: strings

QUOTE_OPEN: '"' -> pushMode(LineString);

TEXT_WIDGET: 'Text';
IMAGE_WIDGET: 'Image';
RESIZABLE: 'resizable';

ASPECT_RATIO_PARAM: 'aspectRatio';
CONTENT_MODE_PARAM: 'contentMode';
CONTENT_MODE: 'ContentMode';
CONTENT_FIT: 'fit';
CONTENT_FILL: 'fill';

FONT_WEIGHT_PARAM: 'fontWeight';
COLOR: 'Color';
COLOR_BLUE: 'blue';

FONT_WEIGHT_BOLD: 'bold';
FOREGROUND_COLOR: 'foregroundColor';
FONT: 'Font';
WEIGHT: 'Weight';


// Identifiers
ID                 : [_]*[a-z][A-Za-z0-9_]* ;

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