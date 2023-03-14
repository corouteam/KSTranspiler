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
    classDeclaration
    |functionDeclaration
    | propertyDeclaration;


statement : propertyDeclaration # propertyDeclarationStatement
          | assignment     # assignmentStatement
          | expression     #expressionStatement
          | print          # printStatement;

print : PRINT LPAREN expression RPAREN ;

varDeclaration : VAR ID (NL* COLON NL* SOME? type)?;

letDeclaration : LET ID (NL* COLON NL* SOME? type)?;

propertyDeclaration:  (varDeclaration|letDeclaration) ((ASSIGN expression)|computedPropertyDeclarationBody)?;

annotation: AT ID;

assignment : left=expression ASSIGN right=expression ;

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
           | CG_FLOAT LPAREN INT_LIT RPAREN                                # cgFloatLiteral
           | if                                                            # ifExpression
           | stringLiteral                                                 # stringLiteralExpression
           | functionCallExpression                                        # functionCall
           | left=expression RANGE NL* right=expression                    # rangeExpression
           | RETURN returnExpression=expression                            # returnExpression
           | name=ID NL* functionCallParameters NL*                        # functionCall
           | widgetCall                                                    # widgetCallExpression
           | horizontalAlignment                                           # horizontalAlignmentExpression
           | verticalAlignment                                             # verticalAlignmentExpression
           | color                                                         # colorLiteral
           | SELF                                                          # selfExpression
           | (ID | functionCallExpression | SELF) (accessSuffix)*          # complexExpression
           | CONTENT_MODE DOT contentMode #contentModeExpression;

if
    : IF NL* LPAREN NL* expression NL* RPAREN NL*
      (
        body=controlStructureBody
      | body=controlStructureBody? NL* SEMICOLON? NL* ELSE NL* (elseBody=controlStructureBody | SEMICOLON)
      | SEMICOLON)
    ;

accessSuffix:
(navSuffix expression);

navSuffix:
    DOT #dotNavigation
    | ELVIS DOT #elvisNavigation;

functionCallExpression:
    name=ID NL* functionCallParameters NL* ;

controlStructureBody
    : block
    | statement
    ;

functionCallParameters
    : LPAREN NL* (expression (NL* COMMA NL* expression)* (NL* COMMA)?)? NL* RPAREN
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
    (NL* FUNCTION_RETURN NL* type)?
    (NL* functionBody)?
    ;

functionBody
    : block
    ;

structDeclaration:
    STRUCT ID (NL* COLON NL* delegationSpecifiers)?
    (NL* classBody)?
    ;

classDeclaration:
    (CLASS|STRUCT) ID (NL* COLON NL* delegationSpecifiers)?
    (NL* classBody)?
    ;

classBody
    : LCURL NL* (classMemberDeclaration semis?)* NL* RCURL
    ;
delegationSpecifiers
    : type (NL* COMMA NL* type)*
    ;

classMemberDeclaration
    :  primaryConstructor
    |  declaration;

primaryConstructor
    : CONSTRUCTOR NL* functionValueParameters  NL* block?
    ;
semis
    : (SEMICOLON | NL)+
    ;

type : INT     # integer |
       DOUBLE  # double |
       BOOL    # bool |
       STRING  # string |
       ID      #userType |
       CG_FLOAT #cgFloat |
       CONTENT_MODE #contentModeType;



widgetCall:
    TEXT_WIDGET LPAREN expression RPAREN ((NL* DOT NL* swiftUITextSuffix) (NL* DOT NL* swiftUITextSuffix)*)?  #textWidget
    | BUTTON_WIDGET LPAREN ID COLON action = functionBody NL* RPAREN body = block #buttonWidget
    | DIVIDER_WIDGET LPAREN RPAREN (NL* DOT NL* swiftUIGenericWidgetSuffix)*? #dividerWidget
    | SPACER_WIDGET LPAREN RPAREN (NL* DOT NL* swiftUIGenericWidgetSuffix)*? #spacerWidget
    |VSTACK_WIDGET LPAREN ((NL* swiftUIColumnParam) (NL* COMMA NL* swiftUIColumnParam)*)?  NL*RPAREN block? #vStackWidget |
    HSTACK_WIDGET LPAREN ((NL* swiftUIColumnParam) (NL* COMMA NL* swiftUIColumnParam)*)?  NL*RPAREN block? #hStackWidget |
    SCROLL_VIEW LPAREN (DOT ID)? NL*RPAREN block #scrollViewWidget |
    ZSTACK block? #zStackWidget
    | IMAGE_WIDGET LPAREN expression RPAREN ((NL* DOT NL* swiftUIImageSuffix) (NL* DOT NL* swiftUIImageSuffix)*)?  #imageWidget;

swiftUITextSuffix:
    FOREGROUND_COLOR LPAREN color RPAREN # foregroundColorSuffix
    | FONT_WEIGHT_PARAM LPAREN fontWeight RPAREN # boldSuffix
    ;

swiftUIColumnParam:
    ALIGNMENT_PARAM COLON expression # alignmentParameter |
    SPACING_PARAM COLON expression # spacingParameter;

horizontalAlignment:
    HORIZONTAL_ALIGNMENT DOT LEADING #leadingAlignment |
    HORIZONTAL_ALIGNMENT DOT TRAILING #trailingAlignment |
    HORIZONTAL_ALIGNMENT DOT CENTER #centerHorizontalAlignment;

verticalAlignment:
    VERTICAL_ALIGNMENT DOT TOP #topAlignment |
    VERTICAL_ALIGNMENT DOT BOTTOM #bottomAlignment |
    VERTICAL_ALIGNMENT DOT CENTER #centerVerticalAlignment;


swiftUIGenericWidgetSuffix:
    FRAME LPAREN ((NL* frameParam) (NL* COMMA NL* frameParam)*)? RPAREN #frameSuffix
    | OVERLAY LPAREN color RPAREN #overlaySuffix;

frameParam:
     HEIGHT COLON expression #heightParam |
     WIDTH COLON expression #widthParam;

fontWeight:
     FONT DOT WEIGHT DOT FONT_WEIGHT_BOLD #boldFontWeight;

color:
     COLOR DOT COLOR_BLUE #blueColor;

swiftUIImageSuffix:
    RESIZABLE LPAREN RPAREN # resizableSuffix |
    ASPECT_RATIO_PARAM LPAREN  CONTENT_MODE_PARAM COLON expression RPAREN # aspectRatioSuffix;

 contentMode:
     CONTENT_FIT  #contentModeFit |
     CONTENT_FILL #contentModeFill;