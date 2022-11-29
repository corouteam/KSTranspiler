// Generated from /Users/annalabellarte/Dev/Università/KSTranspiler2/src/main/antlr/KotlinLexer.g4 by ANTLR 4.10.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KotlinLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NL=1, WS=2, VAR=3, VAL=4, PRINT=5, AS=6, INT=7, DOUBLE=8, BOOL=9, STRING=10, 
		INT_LIT=11, BOOL_LIT=12, DOUBLE_LIT=13, FLOAT_LIT=14, PLUS=15, MINUS=16, 
		ASTERISK=17, DIVISION=18, ASSIGN=19, LPAREN=20, RPAREN=21, COLON=22, ID=23, 
		QUOTE_OPEN=24, QUOTE_CLOSE=25, LineStrText=26;
	public static final int
		LineString=1, Inside=2;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "LineString", "Inside"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NL", "WS", "VAR", "VAL", "PRINT", "AS", "INT", "DOUBLE", "BOOL", "STRING", 
			"INT_LIT", "BOOL_LIT", "DOUBLE_LIT", "FLOAT_LIT", "PLUS", "MINUS", "ASTERISK", 
			"DIVISION", "ASSIGN", "LPAREN", "RPAREN", "COLON", "ID", "QUOTE_OPEN", 
			"QUOTE_CLOSE", "LineStrText", "Inside_QUOTE_OPEN", "DecDigit", "DecDigitNoZero", 
			"DecDigitOrSeparator", "DecDigits"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'var'", "'val'", "'print'", "'as'", "'Int'", "'Double'", 
			"'Bool'", "'String'", null, null, null, null, "'+'", "'-'", "'*'", "'/'", 
			"'='", "'('", "')'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NL", "WS", "VAR", "VAL", "PRINT", "AS", "INT", "DOUBLE", "BOOL", 
			"STRING", "INT_LIT", "BOOL_LIT", "DOUBLE_LIT", "FLOAT_LIT", "PLUS", "MINUS", 
			"ASTERISK", "DIVISION", "ASSIGN", "LPAREN", "RPAREN", "COLON", "ID", 
			"QUOTE_OPEN", "QUOTE_CLOSE", "LineStrText"
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


	public KotlinLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "KotlinLexer.g4"; }

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
		"\u0004\u0000\u001a\u00e0\u0006\uffff\uffff\u0006\uffff\uffff\u0006\uffff"+
		"\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007"+
		"\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007"+
		"\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b"+
		"\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007"+
		"\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002"+
		"\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012\u0002"+
		"\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015\u0002"+
		"\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018\u0002"+
		"\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b\u0002"+
		"\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0003\u0000E\b\u0000\u0001\u0001\u0004"+
		"\u0001H\b\u0001\u000b\u0001\f\u0001I\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0005\nx\b\n\n\n\f\n{\t\n\u0001\n\u0003\n~\b\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u0089\b\u000b\u0001\f\u0003\f\u008c\b\f\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\u0091\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0003\r\u0099\b\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0016\u0005\u0016\u00ac\b\u0016\n\u0016\f\u0016\u00af\t\u0016"+
		"\u0001\u0016\u0001\u0016\u0005\u0016\u00b3\b\u0016\n\u0016\f\u0016\u00b6"+
		"\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0004\u0019\u00c1\b\u0019\u000b"+
		"\u0019\f\u0019\u00c2\u0001\u0019\u0003\u0019\u00c6\b\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0003\u001d\u00d3\b\u001d"+
		"\u0001\u001e\u0001\u001e\u0005\u001e\u00d7\b\u001e\n\u001e\f\u001e\u00da"+
		"\t\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u00df\b\u001e"+
		"\u0000\u0000\u001f\u0003\u0001\u0005\u0002\u0007\u0003\t\u0004\u000b\u0005"+
		"\r\u0006\u000f\u0007\u0011\b\u0013\t\u0015\n\u0017\u000b\u0019\f\u001b"+
		"\r\u001d\u000e\u001f\u000f!\u0010#\u0011%\u0012\'\u0013)\u0014+\u0015"+
		"-\u0016/\u00171\u00183\u00195\u001a7\u00009\u0000;\u0000=\u0000?\u0000"+
		"\u0003\u0000\u0001\u0002\u0007\u0002\u0000\n\nrr\u0002\u0000\t\t  \u0002"+
		"\u0000FFff\u0001\u0000__\u0001\u0000az\u0004\u000009AZ__az\u0003\u0000"+
		"\"\"$$\\\\\u00e8\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"+
		"\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"+
		"\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'"+
		"\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000"+
		"\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000"+
		"\u00001\u0001\u0000\u0000\u0000\u00013\u0001\u0000\u0000\u0000\u00015"+
		"\u0001\u0000\u0000\u0000\u00027\u0001\u0000\u0000\u0000\u0003D\u0001\u0000"+
		"\u0000\u0000\u0005G\u0001\u0000\u0000\u0000\u0007M\u0001\u0000\u0000\u0000"+
		"\tQ\u0001\u0000\u0000\u0000\u000bU\u0001\u0000\u0000\u0000\r[\u0001\u0000"+
		"\u0000\u0000\u000f^\u0001\u0000\u0000\u0000\u0011b\u0001\u0000\u0000\u0000"+
		"\u0013i\u0001\u0000\u0000\u0000\u0015n\u0001\u0000\u0000\u0000\u0017}"+
		"\u0001\u0000\u0000\u0000\u0019\u0088\u0001\u0000\u0000\u0000\u001b\u0090"+
		"\u0001\u0000\u0000\u0000\u001d\u0098\u0001\u0000\u0000\u0000\u001f\u009a"+
		"\u0001\u0000\u0000\u0000!\u009c\u0001\u0000\u0000\u0000#\u009e\u0001\u0000"+
		"\u0000\u0000%\u00a0\u0001\u0000\u0000\u0000\'\u00a2\u0001\u0000\u0000"+
		"\u0000)\u00a4\u0001\u0000\u0000\u0000+\u00a6\u0001\u0000\u0000\u0000-"+
		"\u00a8\u0001\u0000\u0000\u0000/\u00ad\u0001\u0000\u0000\u00001\u00b7\u0001"+
		"\u0000\u0000\u00003\u00bb\u0001\u0000\u0000\u00005\u00c5\u0001\u0000\u0000"+
		"\u00007\u00c7\u0001\u0000\u0000\u00009\u00cc\u0001\u0000\u0000\u0000;"+
		"\u00ce\u0001\u0000\u0000\u0000=\u00d2\u0001\u0000\u0000\u0000?\u00de\u0001"+
		"\u0000\u0000\u0000AB\u0005\r\u0000\u0000BE\u0005\n\u0000\u0000CE\u0007"+
		"\u0000\u0000\u0000DA\u0001\u0000\u0000\u0000DC\u0001\u0000\u0000\u0000"+
		"E\u0004\u0001\u0000\u0000\u0000FH\u0007\u0001\u0000\u0000GF\u0001\u0000"+
		"\u0000\u0000HI\u0001\u0000\u0000\u0000IG\u0001\u0000\u0000\u0000IJ\u0001"+
		"\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KL\u0006\u0001\u0000\u0000"+
		"L\u0006\u0001\u0000\u0000\u0000MN\u0005v\u0000\u0000NO\u0005a\u0000\u0000"+
		"OP\u0005r\u0000\u0000P\b\u0001\u0000\u0000\u0000QR\u0005v\u0000\u0000"+
		"RS\u0005a\u0000\u0000ST\u0005l\u0000\u0000T\n\u0001\u0000\u0000\u0000"+
		"UV\u0005p\u0000\u0000VW\u0005r\u0000\u0000WX\u0005i\u0000\u0000XY\u0005"+
		"n\u0000\u0000YZ\u0005t\u0000\u0000Z\f\u0001\u0000\u0000\u0000[\\\u0005"+
		"a\u0000\u0000\\]\u0005s\u0000\u0000]\u000e\u0001\u0000\u0000\u0000^_\u0005"+
		"I\u0000\u0000_`\u0005n\u0000\u0000`a\u0005t\u0000\u0000a\u0010\u0001\u0000"+
		"\u0000\u0000bc\u0005D\u0000\u0000cd\u0005o\u0000\u0000de\u0005u\u0000"+
		"\u0000ef\u0005b\u0000\u0000fg\u0005l\u0000\u0000gh\u0005e\u0000\u0000"+
		"h\u0012\u0001\u0000\u0000\u0000ij\u0005B\u0000\u0000jk\u0005o\u0000\u0000"+
		"kl\u0005o\u0000\u0000lm\u0005l\u0000\u0000m\u0014\u0001\u0000\u0000\u0000"+
		"no\u0005S\u0000\u0000op\u0005t\u0000\u0000pq\u0005r\u0000\u0000qr\u0005"+
		"i\u0000\u0000rs\u0005n\u0000\u0000st\u0005g\u0000\u0000t\u0016\u0001\u0000"+
		"\u0000\u0000uy\u0003;\u001c\u0000vx\u00039\u001b\u0000wv\u0001\u0000\u0000"+
		"\u0000x{\u0001\u0000\u0000\u0000yw\u0001\u0000\u0000\u0000yz\u0001\u0000"+
		"\u0000\u0000z~\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000|~\u0003"+
		"9\u001b\u0000}u\u0001\u0000\u0000\u0000}|\u0001\u0000\u0000\u0000~\u0018"+
		"\u0001\u0000\u0000\u0000\u007f\u0080\u0005t\u0000\u0000\u0080\u0081\u0005"+
		"r\u0000\u0000\u0081\u0082\u0005u\u0000\u0000\u0082\u0089\u0005e\u0000"+
		"\u0000\u0083\u0084\u0005f\u0000\u0000\u0084\u0085\u0005a\u0000\u0000\u0085"+
		"\u0086\u0005l\u0000\u0000\u0086\u0087\u0005s\u0000\u0000\u0087\u0089\u0005"+
		"e\u0000\u0000\u0088\u007f\u0001\u0000\u0000\u0000\u0088\u0083\u0001\u0000"+
		"\u0000\u0000\u0089\u001a\u0001\u0000\u0000\u0000\u008a\u008c\u0003?\u001e"+
		"\u0000\u008b\u008a\u0001\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000"+
		"\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d\u008e\u0005.\u0000\u0000"+
		"\u008e\u0091\u0003?\u001e\u0000\u008f\u0091\u0003?\u001e\u0000\u0090\u008b"+
		"\u0001\u0000\u0000\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0091\u001c"+
		"\u0001\u0000\u0000\u0000\u0092\u0093\u0003\u001b\f\u0000\u0093\u0094\u0007"+
		"\u0002\u0000\u0000\u0094\u0099\u0001\u0000\u0000\u0000\u0095\u0096\u0003"+
		"?\u001e\u0000\u0096\u0097\u0007\u0002\u0000\u0000\u0097\u0099\u0001\u0000"+
		"\u0000\u0000\u0098\u0092\u0001\u0000\u0000\u0000\u0098\u0095\u0001\u0000"+
		"\u0000\u0000\u0099\u001e\u0001\u0000\u0000\u0000\u009a\u009b\u0005+\u0000"+
		"\u0000\u009b \u0001\u0000\u0000\u0000\u009c\u009d\u0005-\u0000\u0000\u009d"+
		"\"\u0001\u0000\u0000\u0000\u009e\u009f\u0005*\u0000\u0000\u009f$\u0001"+
		"\u0000\u0000\u0000\u00a0\u00a1\u0005/\u0000\u0000\u00a1&\u0001\u0000\u0000"+
		"\u0000\u00a2\u00a3\u0005=\u0000\u0000\u00a3(\u0001\u0000\u0000\u0000\u00a4"+
		"\u00a5\u0005(\u0000\u0000\u00a5*\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005"+
		")\u0000\u0000\u00a7,\u0001\u0000\u0000\u0000\u00a8\u00a9\u0005:\u0000"+
		"\u0000\u00a9.\u0001\u0000\u0000\u0000\u00aa\u00ac\u0007\u0003\u0000\u0000"+
		"\u00ab\u00aa\u0001\u0000\u0000\u0000\u00ac\u00af\u0001\u0000\u0000\u0000"+
		"\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000\u0000"+
		"\u00ae\u00b0\u0001\u0000\u0000\u0000\u00af\u00ad\u0001\u0000\u0000\u0000"+
		"\u00b0\u00b4\u0007\u0004\u0000\u0000\u00b1\u00b3\u0007\u0005\u0000\u0000"+
		"\u00b2\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b6\u0001\u0000\u0000\u0000"+
		"\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000"+
		"\u00b50\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b8\u0005\"\u0000\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u00ba"+
		"\u0006\u0017\u0001\u0000\u00ba2\u0001\u0000\u0000\u0000\u00bb\u00bc\u0005"+
		"\"\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\u00be\u0006\u0018"+
		"\u0002\u0000\u00be4\u0001\u0000\u0000\u0000\u00bf\u00c1\b\u0006\u0000"+
		"\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000"+
		"\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c2\u00c3\u0001\u0000\u0000"+
		"\u0000\u00c3\u00c6\u0001\u0000\u0000\u0000\u00c4\u00c6\u0005$\u0000\u0000"+
		"\u00c5\u00c0\u0001\u0000\u0000\u0000\u00c5\u00c4\u0001\u0000\u0000\u0000"+
		"\u00c66\u0001\u0000\u0000\u0000\u00c7\u00c8\u00031\u0017\u0000\u00c8\u00c9"+
		"\u0001\u0000\u0000\u0000\u00c9\u00ca\u0006\u001a\u0001\u0000\u00ca\u00cb"+
		"\u0006\u001a\u0003\u0000\u00cb8\u0001\u0000\u0000\u0000\u00cc\u00cd\u0002"+
		"09\u0000\u00cd:\u0001\u0000\u0000\u0000\u00ce\u00cf\u000219\u0000\u00cf"+
		"<\u0001\u0000\u0000\u0000\u00d0\u00d3\u00039\u001b\u0000\u00d1\u00d3\u0005"+
		"_\u0000\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d1\u0001\u0000"+
		"\u0000\u0000\u00d3>\u0001\u0000\u0000\u0000\u00d4\u00d8\u00039\u001b\u0000"+
		"\u00d5\u00d7\u0003=\u001d\u0000\u00d6\u00d5\u0001\u0000\u0000\u0000\u00d7"+
		"\u00da\u0001\u0000\u0000\u0000\u00d8\u00d6\u0001\u0000\u0000\u0000\u00d8"+
		"\u00d9\u0001\u0000\u0000\u0000\u00d9\u00db\u0001\u0000\u0000\u0000\u00da"+
		"\u00d8\u0001\u0000\u0000\u0000\u00db\u00dc\u00039\u001b\u0000\u00dc\u00df"+
		"\u0001\u0000\u0000\u0000\u00dd\u00df\u00039\u001b\u0000\u00de\u00d4\u0001"+
		"\u0000\u0000\u0000\u00de\u00dd\u0001\u0000\u0000\u0000\u00df@\u0001\u0000"+
		"\u0000\u0000\u0012\u0000\u0001\u0002DIy}\u0088\u008b\u0090\u0098\u00ad"+
		"\u00b4\u00c2\u00c5\u00d2\u00d8\u00de\u0004\u0006\u0000\u0000\u0005\u0001"+
		"\u0000\u0004\u0000\u0000\u0007\u0018\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}