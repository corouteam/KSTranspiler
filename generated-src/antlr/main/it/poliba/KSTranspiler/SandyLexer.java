// Generated from SandyLexer.g4 by ANTLR 4.8
package it.poliba.KSTranspiler;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SandyLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NEWLINE=1, WS=2, VAR=3, LET=4, PRINT=5, AS=6, INT=7, DECIMAL=8, INTLIT=9, 
		DECLIT=10, PLUS=11, MINUS=12, ASTERISK=13, DIVISION=14, ASSIGN=15, LPAREN=16, 
		RPAREN=17, ID=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NEWLINE", "WS", "VAR", "LET", "PRINT", "AS", "INT", "DECIMAL", "INTLIT", 
			"DECLIT", "PLUS", "MINUS", "ASTERISK", "DIVISION", "ASSIGN", "LPAREN", 
			"RPAREN", "ID"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'var'", "'let'", "'print'", "'as'", "'Int'", "'Decimal'", 
			null, null, "'+'", "'-'", "'*'", "'/'", "'='", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NEWLINE", "WS", "VAR", "LET", "PRINT", "AS", "INT", "DECIMAL", 
			"INTLIT", "DECLIT", "PLUS", "MINUS", "ASTERISK", "DIVISION", "ASSIGN", 
			"LPAREN", "RPAREN", "ID"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SandyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SandyLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\24\u0085\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\2\5\2+\n\2\3\3\6\3.\n\3\r\3\16\3/\3\3\3\3\3"+
		"\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\7\nT\n\n\f\n"+
		"\16\nW\13\n\5\nY\n\n\3\13\3\13\3\13\7\13^\n\13\f\13\16\13a\13\13\3\13"+
		"\3\13\6\13e\n\13\r\13\16\13f\5\13i\n\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17"+
		"\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\7\23z\n\23\f\23\16\23}\13\23"+
		"\3\23\3\23\7\23\u0081\n\23\f\23\16\23\u0084\13\23\2\2\24\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\3\2\t\4\2\f\ftt\4\2\13\13\"\"\3\2\63;\3\2\62;\3\2aa\3\2c|\6\2\62"+
		";C\\aac|\2\u008d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\3*\3\2\2\2\5-\3\2\2\2\7\63\3\2\2\2\t\67"+
		"\3\2\2\2\13;\3\2\2\2\rA\3\2\2\2\17D\3\2\2\2\21H\3\2\2\2\23X\3\2\2\2\25"+
		"h\3\2\2\2\27j\3\2\2\2\31l\3\2\2\2\33n\3\2\2\2\35p\3\2\2\2\37r\3\2\2\2"+
		"!t\3\2\2\2#v\3\2\2\2%{\3\2\2\2\'(\7\17\2\2(+\7\f\2\2)+\t\2\2\2*\'\3\2"+
		"\2\2*)\3\2\2\2+\4\3\2\2\2,.\t\3\2\2-,\3\2\2\2./\3\2\2\2/-\3\2\2\2/\60"+
		"\3\2\2\2\60\61\3\2\2\2\61\62\b\3\2\2\62\6\3\2\2\2\63\64\7x\2\2\64\65\7"+
		"c\2\2\65\66\7t\2\2\66\b\3\2\2\2\678\7n\2\289\7g\2\29:\7v\2\2:\n\3\2\2"+
		"\2;<\7r\2\2<=\7t\2\2=>\7k\2\2>?\7p\2\2?@\7v\2\2@\f\3\2\2\2AB\7c\2\2BC"+
		"\7u\2\2C\16\3\2\2\2DE\7K\2\2EF\7p\2\2FG\7v\2\2G\20\3\2\2\2HI\7F\2\2IJ"+
		"\7g\2\2JK\7e\2\2KL\7k\2\2LM\7o\2\2MN\7c\2\2NO\7n\2\2O\22\3\2\2\2PY\7\62"+
		"\2\2QU\t\4\2\2RT\t\5\2\2SR\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2VY\3\2"+
		"\2\2WU\3\2\2\2XP\3\2\2\2XQ\3\2\2\2Y\24\3\2\2\2Zi\7\62\2\2[_\t\4\2\2\\"+
		"^\t\5\2\2]\\\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3\2\2\2`b\3\2\2\2a_\3\2\2\2"+
		"bd\7\60\2\2ce\t\5\2\2dc\3\2\2\2ef\3\2\2\2fd\3\2\2\2fg\3\2\2\2gi\3\2\2"+
		"\2hZ\3\2\2\2h[\3\2\2\2i\26\3\2\2\2jk\7-\2\2k\30\3\2\2\2lm\7/\2\2m\32\3"+
		"\2\2\2no\7,\2\2o\34\3\2\2\2pq\7\61\2\2q\36\3\2\2\2rs\7?\2\2s \3\2\2\2"+
		"tu\7*\2\2u\"\3\2\2\2vw\7+\2\2w$\3\2\2\2xz\t\6\2\2yx\3\2\2\2z}\3\2\2\2"+
		"{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}{\3\2\2\2~\u0082\t\7\2\2\177\u0081\t\b"+
		"\2\2\u0080\177\3\2\2\2\u0081\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083"+
		"\3\2\2\2\u0083&\3\2\2\2\u0084\u0082\3\2\2\2\f\2*/UX_fh{\u0082\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}