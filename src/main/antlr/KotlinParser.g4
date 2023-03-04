parser grammar KotlinParser;

options { tokenVocab=KotlinLexer; }

file
    : NL* declaration* EOF #kotlinFile
    |  lines=line+ #kotlinScript;


line      : statement (NL | EOF) ;

packageHeader
    : (PACKAGE ID)?
    ;

importList
    : importHeader*
    ;

importHeader
    : IMPORT ID?
    ;

declaration:
    functionDeclaration
    | propertyDeclaration
    ;


statement : propertyDeclaration # propertyDeclarationStatement
          | assignment     # assignmentStatement
          | expression     #expressionStatement
          | print          # printStatement;

print : PRINT LPAREN expression RPAREN ;


varDeclaration : VAR ID (NL* COLON NL* type)?;

valDeclaration : VAL ID (NL* COLON NL* type)?;

propertyDeclaration:  (varDeclaration|valDeclaration) (ASSIGN expression)?;

annotation: AT ID;

assignment : ID ASSIGN expression ;


expression : left=expression operator=(DIVISION|ASTERISK) right=expression # binaryOperation
           | left=expression operator=(PLUS|MINUS) right=expression        # binaryOperation
           | value=expression AS targetType=type                           # typeConversion
           | LPAREN expression RPAREN                                      # parenExpression
           | ID                                                            # varReference
           | MINUS expression                                              # minusExpression
           | INT_LIT                                                       # intLiteral
           | DOUBLE_LIT                                                    # doubleLiteral
           | BOOL_LIT                                                      # boolLiteral
           | if                                                            # ifExpression
           | while                                                         # whileExpression
           | for                                                           # forExpression
           | stringLiteral                                                 # stringLiteralExpression
           | left=expression RANGE NL* right=expression                    # rangeExpression
           | LISTOF typeArguments LPAREN NL* (expression (NL* COMMA NL* expression)* (NL* COMMA)?)? NL* RPAREN # listExpression
           | ARRAYOF typeArguments LPAREN NL* (expression (NL* COMMA NL* expression)* (NL* COMMA)?)? NL* RPAREN # arrayExpression
           | RETURN returnExpression=expression                            # returnExpression
           | composableCall #composableCallExpression
           | color                                                         # colorLiteral
           | fontWeight                                                    # fontWeightLiteral;


while: WHILE LPAREN expression RPAREN NL* body=controlStructureBody*;

if
    : IF NL* LPAREN NL* expression NL* RPAREN NL*
      (
        body=controlStructureBody
      | body=controlStructureBody? NL* SEMICOLON? NL* ELSE NL* (elseBody=controlStructureBody | SEMICOLON)
      | SEMICOLON)
    ;

for
    : FOR NL* LPAREN NL* ID NL* IN NL* expression NL* RPAREN NL* body=controlStructureBody;

controlStructureBody
    : block
    | statement
    ;

block
    : LCURL NL* statement* NL* RCURL
    ;

stringLiteral
   : lineStringLiteral;


lineStringLiteral
   : QUOTE_OPEN (lineStringContent)* QUOTE_CLOSE
   ;

lineStringContent
   : LineStrText
//    | LineStrEscapedChar
//   | LineStrRef
   ;

functionValueParameters
    : LPAREN NL* (functionValueParameter (NL* COMMA NL* functionValueParameter)* (NL* COMMA)?)? NL* RPAREN
    ;

functionValueParameter
    : parameter (NL* ASSIGN NL* expression)?
    ;

parameter
    : ID NL* COLON NL* type
    ;

functionDeclaration:
    annotation? NL* FUN NL* ID
    NL* functionValueParameters
    (NL* COLON NL* type)?
    (NL* functionBody)?
    ;

functionBody
    : block
    | ASSIGN NL* expression
    ;

type : INT     # integer |
       DOUBLE  # double |
       BOOL    # bool |
       STRING  # string ;

typeArguments
    : LANGLE NL* type (NL* COMMA NL* type)* (NL* COMMA)? NL* RANGLE
    ;

composableCall:
    TEXT_COMPOSE LPAREN expression ((NL* COMMA NL* textComposeParameter) (NL* COMMA NL* textComposeParameter)*)?  RPAREN #textComposable;

textComposeParameter:
    COLOR_PARAM ASSIGN color #colorParameter
    | FONT_WEIGHT_PARAM ASSIGN fontWeight #fontWeightParameter;

color:
     COLOR LPAREN COLOR_LITERAL RPAREN #customColor
     | COLOR DOT COLOR_BLUE #blueColor;

fontWeight:
    FONT_WEIGHT LPAREN INT_LIT RPAREN #customWeight
    | FONT_WEIGHT DOT FONT_WEIGHT_BOLD #boldFontWeight;

