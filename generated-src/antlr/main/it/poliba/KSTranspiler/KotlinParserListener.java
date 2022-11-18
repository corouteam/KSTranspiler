// Generated from java-escape by ANTLR 4.11.1
package it.poliba.KSTranspiler;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KotlinParser}.
 */
public interface KotlinParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 */
	void enterKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 */
	void exitKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(KotlinParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(KotlinParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varDeclarationStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclarationStatement(KotlinParser.VarDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varDeclarationStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclarationStatement(KotlinParser.VarDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignmentStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(KotlinParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignmentStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(KotlinParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(KotlinParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(KotlinParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code letDeclarationStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterLetDeclarationStatement(KotlinParser.LetDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code letDeclarationStatement}
	 * labeled alternative in {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitLetDeclarationStatement(KotlinParser.LetDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(KotlinParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(KotlinParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(KotlinParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(KotlinParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#letDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLetDeclaration(KotlinParser.LetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#letDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLetDeclaration(KotlinParser.LetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(KotlinParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(KotlinParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(KotlinParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(KotlinParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(KotlinParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(KotlinParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(KotlinParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(KotlinParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(KotlinParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(KotlinParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryOperation}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOperation(KotlinParser.BinaryOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryOperation}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOperation(KotlinParser.BinaryOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeConversion}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTypeConversion(KotlinParser.TypeConversionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeConversion}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTypeConversion(KotlinParser.TypeConversionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varReference}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVarReference(KotlinParser.VarReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varReference}
	 * labeled alternative in {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVarReference(KotlinParser.VarReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integer}
	 * labeled alternative in {@link KotlinParser#type}.
	 * @param ctx the parse tree
	 */
	void enterInteger(KotlinParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integer}
	 * labeled alternative in {@link KotlinParser#type}.
	 * @param ctx the parse tree
	 */
	void exitInteger(KotlinParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link KotlinParser#type}.
	 * @param ctx the parse tree
	 */
	void enterDecimal(KotlinParser.DecimalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link KotlinParser#type}.
	 * @param ctx the parse tree
	 */
	void exitDecimal(KotlinParser.DecimalContext ctx);
}