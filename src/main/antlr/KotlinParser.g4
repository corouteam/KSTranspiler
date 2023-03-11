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
           | INT_LIT DOT DP_SUFFIX                                             # dpLiteral
           | name=ID NL* functionCallParameters NL*                        # functionCall
           | if                                                            # ifExpression
           | stringLiteral                                                 # stringLiteralExpression
           | left=expression RANGE NL* right=expression                    # rangeExpression
           | LISTOF typeArguments LPAREN NL* (expression (NL* COMMA NL* expression)* (NL* COMMA)?)? NL* RPAREN # listExpression
           | RETURN returnExpression=expression                            # returnExpression
           | composableCall #composableCallExpression
           | color                                                         # colorLiteral
           | fontWeight                                                    # fontWeightLiteral
           | painter                                                       # painterLiteral
           | fontWeight                                                    # fontWeightLiteral
           | horizontalAlignment                                           #horizhontalAlignmentExpression
           | verticalAlignment                                             #verticalAlignmentExpression
           | arrangement                                                   #arrangementExpression;


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
    : ID NL* COLON NL* type
    ;

functionCallParameters
    : LPAREN NL* (expression (NL* COMMA NL* expression)* (NL* COMMA)?)? NL* RPAREN
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
semis
    : (SEMICOLON | NL)+;

type : INT     # integer |
       DOUBLE  # double |
       BOOL    # bool |
       STRING  # string;

typeArguments
    : LANGLE NL* type (NL* COMMA NL* type)* (NL* COMMA)? NL* RANGLE
    ;

composableCall:
    TEXT_COMPOSE LPAREN expression ((NL* COMMA NL* textComposeParameter) (NL* COMMA NL* textComposeParameter)*)?  RPAREN #textComposable
    | DIVIDER_COMPOSE LPAREN ((NL* dividerComposeParameter) (NL* COMMA NL* dividerComposeParameter)*)? RPAREN (NL* DOT NL* composableUIGenericWidgetSuffix)*? #dividerComposable
    | SPACER_COMPOSE LPAREN (NL* modifierParameter NL*)? RPAREN  #spacerComposable
    | COLUMN_COMPOSE LPAREN ((NL* columnComposeParameter) (NL* COMMA NL* columnComposeParameter)*)?  RPAREN block? #columnComposable
    | ROW_COMPOSE LPAREN ((NL* rowComposeParameter) (NL* COMMA NL* rowComposeParameter)*)?  RPAREN block? #rowComposable
    | BOX LPAREN (NL* modifierParameter NL*)? RPAREN block? #boxComposable
    | BUTTON_COMPOSABLE LPAREN ID ASSIGN action = functionBody (NL* COMMA NL* modifierParameter)?  RPAREN body = block #iconButtonComposable
    | IMAGE_COMPOSE LPAREN (NL* imageComposeParameter) ((NL* COMMA NL* imageComposeParameter) (NL* COMMA NL* imageComposeParameter)*)? RPAREN #imageComposable;

textComposeParameter:
    COLOR_PARAM ASSIGN color #colorParameter
    | FONT_WEIGHT_PARAM ASSIGN fontWeight #fontWeightParameter
    | modifierParameter #modifierTextParameter;

composableUIGenericWidgetSuffix:
    SIZE LPAREN WIDTH COLON width = expression COMMA HEIGHT COLON heigth = expression RPAREN #sizeSuffix;

columnComposeParameter:
    VERTICAL_ARRANGEMENT_PARAM NL* ASSIGN NL* expression #verticalArrangementParameter |
    HORIZONTAL_ALIGNMENT_PARAM NL* ASSIGN NL* expression #horizontalAlignmentParameter |
    modifierParameter #modifierColumnParameter;

rowComposeParameter:
    VERTICAL_ALIGNMENT_PARAM NL* ASSIGN NL* expression #verticalAlignmentParameter |
    HORIZONTAL_ARRANGEMENT_PARAM NL* ASSIGN NL* expression #horizontalArrangementParameter |
    modifierParameter #modifierRowParameter;

dividerComposeParameter:
    THICKNESS ASSIGN expression #dividerTicknessParamater |
    COLOR_PARAM ASSIGN color #dividerColorParameter |
    modifierParameter #dividerModifierParameter;

arrangement:
   ARRANGEMENT DOT SPACED_BY LPAREN expression RPAREN;

horizontalAlignment:
    ALIGNMENT DOT START #startAlignment |
    ALIGNMENT DOT END #endAlignment |
    ALIGNMENT DOT CENTER_HORIZONTALLY #centerHorizontallyAlignment;

verticalAlignment:
    ALIGNMENT DOT TOP #topAlignment |
    ALIGNMENT DOT BOTTOM #bottomAlignment |
    ALIGNMENT DOT CENTER_VERTICALLY #centerVerticalltAlignment;

imageComposeParameter:
    PAINTER_PARAM ASSIGN painter #painterParameter |
    MODIFIER_PARAM ASSIGN MODIFIER DOT RESIZABLE LPAREN RPAREN  #resizable |
    CONTENTSCALE_PARAM ASSIGN CONTENTSCALE DOT FILLWIDTH #contentScaleFillWidth |
    CONTENTSCALE_PARAM ASSIGN CONTENTSCALE DOT FIT #contentScaleFit;

color:
     COLOR LPAREN COLOR_LITERAL RPAREN #customColor
     | COLOR DOT COLOR_BLUE #blueColor
     | ID #idColor ;

fontWeight:
    FONT_WEIGHT LPAREN INT_LIT RPAREN #customWeight
    | FONT_WEIGHT DOT FONT_WEIGHT_BOLD #boldFontWeight;

modifierParameter:
    MODIFIER_PARAM NL* ASSIGN NL* modifier;

modifier:
    MODIFIER (NL* DOT NL* modifierSuffix)*;

modifierSuffix:
   VERTICAL_SCROLL_SUFFIX LPAREN REMEMBER_SCROLL LPAREN RPAREN RPAREN #verticalScrollSuffix|
   HORIZONTAL_SCROLL_SUFFIX LPAREN REMEMBER_SCROLL LPAREN RPAREN RPAREN #horizontalScrollSuffix |
   HEIGHT LPAREN expression RPAREN #heightSuffix |
   WIDTH LPAREN expression RPAREN #widthSuffix |
   ZINDEX LPAREN expression RPAREN #zIndexSuffix;

painter:
    PAINTER_RESOURCE LPAREN painterResourceParam RPAREN #painterResource;

painterResourceParam:
    PAINTER_RESOURCE_PARAM ASSIGN getResource = resource #painterResourceParameter;

resource:
    GET_RESOURCE LPAREN RPAREN DOT GET_IDENTIFIER LPAREN imageName = expression COMMA expression COMMA CONTEXT DOT GET_PACKAGENAME LPAREN RPAREN RPAREN #drawableResource;