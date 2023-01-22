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
DP                 : 'Dp';


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
RANGE              : '..';
LANGLE: '<';
RANGLE: '>';
AT                 : '@';
DOT                : '.';

IF: 'if';
ELSE: 'else';
FUN: 'fun';
PACKAGE: 'package';
RETURN                  : 'return' ;
LISTOF: 'listOf';

//Colors
COLOR_LITERAL: '0'[xX][0-9a-fA-F]+;


// SECTION: strings

QUOTE_OPEN: '"' -> pushMode(LineString);

TEXT_COMPOSE: 'Text';
COLUMN_COMPOSE: 'Column';

COLOR: 'Color';
COLOR_BLUE: 'Blue';

FONT_WEIGHT: 'FontWeight';
FONT_WEIGHT_BOLD: 'Bold';

FONT_WEIGHT_PARAM: 'fontWeight';
COLOR_PARAM: 'color';

VERTICAL_ARRANGEMENT_PARAM: 'verticalArrangement';
HORIZONTAL_ALIGNMENT_PARAM: 'horizontalAlignment';

ARRANGEMENT: 'Arrangement';
ALIGNMENT: 'Alignment';
TOP: 'Top';
START: 'Start';
CENTER_HORIZONTALLY: 'CenterHorizontally';
END: 'End';
SPACED_BY: 'spacedBy';
DP_SUFFIX: 'dp';
MODIFIER_PARAM: 'modifier';
VERTICAL_SCROLL_SUFFIX: 'verticalScroll';
REMEMBER_SCROLL: 'rememberScrollState';
MODIFIER: 'Modifier';

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