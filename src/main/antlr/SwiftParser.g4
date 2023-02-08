parser grammar SwiftParser;

options { tokenVocab=SwiftLexer; }

file
    : NL* declaration* EOF #swiftFile
    |  lines=line+ #swiftScript;


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
    | structDeclaration
    ;


statement : propertyDeclaration # propertyDeclarationStatement
          | assignment     # assignmentStatement
          | expression     #expressionStatement
          | print          # printStatement;

print : PRINT LPAREN expression RPAREN ;

varDeclaration : VAR ID (NL* COLON NL* SOME? type)?;

letDeclaration : LET ID (NL* COLON NL* SOME? type)?;

propertyDeclaration:  (varDeclaration|letDeclaration) ((ASSIGN expression)|computedPropertyDeclarationBody)?;

annotation: AT ID;

assignment : ID ASSIGN expression ;

computedPropertyDeclarationBody:
    NL* block;

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
           | stringLiteral                                                 # stringLiteralExpression
           | RETURN returnExpression=expression                            # returnExpression
           | widgetCall #widgetCallExpression
           | color                       # colorLiteral;

if
    : IF NL* LPAREN NL* expression NL* RPAREN NL*
      (
        body=controlStructureBody
      | body=controlStructureBody? NL* SEMICOLON? NL* ELSE NL* (elseBody=controlStructureBody | SEMICOLON)
      | SEMICOLON)
    ;

controlStructureBody
    : block
    | statement
    ;

block
    : LCURL NL* (statement semis?)* NL* RCURL
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
    : ID NL* COLON NL* SOME? type
    ;

functionDeclaration:
    annotation? FUN NL* ID
    NL* functionValueParameters
    (NL* COLON NL* type)?
    (NL* functionBody)?
    ;

functionBody
    : block
    | ASSIGN NL* expression
    ;

structDeclaration:
    STRUCT ID (NL* COLON NL* delegationSpecifiers)?
    (NL* classBody)?
    ;

classBody
    : LCURL NL* classMemberDeclarations NL* RCURL
    ;
delegationSpecifiers
    : ID (NL* COMMA NL* ID)*
    ;

classMemberDeclarations
    : (declaration semis?)*
    ;

semis
    : (SEMICOLON | NL)+
    ;

type : INT     # integer |
       DOUBLE  # double |
       BOOL    # bool |
       STRING  # string |
      ID      #userType;



widgetCall:
    TEXT_WIDGET LPAREN expression RPAREN ((NL* DOT NL* swiftUITextSuffix) (NL* DOT NL* swiftUITextSuffix)*)?  #textWidget
    | DIVIDER_WIDGET LPAREN RPAREN (NL* DOT NL* swiftUIGenericWidgetSuffix)*? #dividerWidget
    | SPACER_WIDGET LPAREN RPAREN (NL* DOT NL* swiftUIGenericWidgetSuffix)*? #spacerWidget;

swiftUITextSuffix:
    FOREGROUND_COLOR LPAREN color RPAREN # foregroundColorSuffix
    | FONT_WEIGHT_PARAM LPAREN fontWeight RPAREN # boldSuffix;

swiftUIGenericWidgetSuffix:
    FRAME LPAREN WIDTH COLON width = expression COMMA HEIGHT COLON heigth = expression RPAREN #frameSuffix;

fontWeight:
     FONT DOT WEIGHT DOT FONT_WEIGHT_BOLD #boldFontWeight;

color:
     COLOR DOT COLOR_BLUE #blueColor;
