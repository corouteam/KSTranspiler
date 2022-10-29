// Generated from SandyParser.g4 by ANTLR 4.8
package it.poliba.KSTranspiler;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SandyParser}.
 */
public interface SandyParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SandyParser#sandyFile}.
	 * @param ctx the parse tree
	 */
	void enterSandyFile(SandyParser.SandyFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SandyParser#sandyFile}.
	 * @param ctx the parse tree
	 */
	void exitSandyFile(SandyParser.SandyFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SandyParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(SandyParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link SandyParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(SandyParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varDeclarationStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclarationStatement(SandyParser.VarDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varDeclarationStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclarationStatement(SandyParser.VarDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignmentStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(SandyParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignmentStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(SandyParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(SandyParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(SandyParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code letDeclarationStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterLetDeclarationStatement(SandyParser.LetDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code letDeclarationStatement}
	 * labeled alternative in {@link SandyParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitLetDeclarationStatement(SandyParser.LetDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SandyParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(SandyParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SandyParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(SandyParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SandyParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(SandyParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SandyParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(SandyParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SandyParser#letDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLetDeclaration(SandyParser.LetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SandyParser#letDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLetDeclaration(SandyParser.LetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SandyParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(SandyParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SandyParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(SandyParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(SandyParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(SandyParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(SandyParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(SandyParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(SandyParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(SandyParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(SandyParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(SandyParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryOperation}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOperation(SandyParser.BinaryOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryOperation}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOperation(SandyParser.BinaryOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeConversion}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTypeConversion(SandyParser.TypeConversionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeConversion}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTypeConversion(SandyParser.TypeConversionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varReference}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVarReference(SandyParser.VarReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varReference}
	 * labeled alternative in {@link SandyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVarReference(SandyParser.VarReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integer}
	 * labeled alternative in {@link SandyParser#type}.
	 * @param ctx the parse tree
	 */
	void enterInteger(SandyParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integer}
	 * labeled alternative in {@link SandyParser#type}.
	 * @param ctx the parse tree
	 */
	void exitInteger(SandyParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link SandyParser#type}.
	 * @param ctx the parse tree
	 */
	void enterDecimal(SandyParser.DecimalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link SandyParser#type}.
	 * @param ctx the parse tree
	 */
	void exitDecimal(SandyParser.DecimalContext ctx);
}