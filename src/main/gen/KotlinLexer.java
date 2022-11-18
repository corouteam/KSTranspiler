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
		NL=1, WS=2, VAR=3, VAL=4, PRINT=5, AS=6, INT=7, DOUBLE=8, BOOL=9, INT_LIT=10, 
		BOOL_LIT=11, DOUBLE_LIT=12, FLOAT_LIT=13, PLUS=14, MINUS=15, ASTERISK=16, 
		DIVISION=17, ASSIGN=18, LPAREN=19, RPAREN=20, COLON=21, ID=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NL", "WS", "VAR", "VAL", "PRINT", "AS", "INT", "DOUBLE", "BOOL", "INT_LIT", 
			"BOOL_LIT", "DOUBLE_LIT", "FLOAT_LIT", "PLUS", "MINUS", "ASTERISK", "DIVISION", 
			"ASSIGN", "LPAREN", "RPAREN", "COLON", "ID", "DecDigit", "DecDigitNoZero", 
			"DecDigits"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'var'", "'val'", "'print'", "'as'", "'Int'", "'Double'", 
			"'Bool'", null, null, null, null, "'+'", "'-'", "'*'", "'/'", "'='", 
			"'('", "')'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NL", "WS", "VAR", "VAL", "PRINT", "AS", "INT", "DOUBLE", "BOOL", 
			"INT_LIT", "BOOL_LIT", "DOUBLE_LIT", "FLOAT_LIT", "PLUS", "MINUS", "ASTERISK", 
			"DIVISION", "ASSIGN", "LPAREN", "RPAREN", "COLON", "ID"
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
		"\u0004\u0000\u0016\u00b0\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000"+
		"7\b\u0000\u0001\u0001\u0004\u0001:\b\u0001\u000b\u0001\f\u0001;\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0005\tc\b\t\n\t\f"+
		"\tf\t\t\u0001\t\u0003\ti\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\nt\b\n\u0001\u000b\u0003\u000bw\b\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b|\b\u000b\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u0084\b\f\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0015\u0005\u0015\u0097\b\u0015\n\u0015\f\u0015"+
		"\u009a\t\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u009e\b\u0015\n\u0015"+
		"\f\u0015\u00a1\t\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017"+
		"\u0001\u0018\u0001\u0018\u0005\u0018\u00a9\b\u0018\n\u0018\f\u0018\u00ac"+
		"\t\u0018\u0001\u0018\u0003\u0018\u00af\b\u0018\u0000\u0000\u0019\u0001"+
		"\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007"+
		"\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d"+
		"\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0000/"+
		"\u00001\u0000\u0001\u0000\u0006\u0002\u0000\n\nrr\u0002\u0000\t\t  \u0002"+
		"\u0000FFff\u0001\u0000__\u0001\u0000az\u0004\u000009AZ__az\u00b8\u0000"+
		"\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000"+
		"\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000"+
		"\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r"+
		"\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019"+
		"\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d"+
		"\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001"+
		"\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000"+
		"\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000"+
		"\u0000+\u0001\u0000\u0000\u0000\u00016\u0001\u0000\u0000\u0000\u00039"+
		"\u0001\u0000\u0000\u0000\u0005?\u0001\u0000\u0000\u0000\u0007C\u0001\u0000"+
		"\u0000\u0000\tG\u0001\u0000\u0000\u0000\u000bM\u0001\u0000\u0000\u0000"+
		"\rP\u0001\u0000\u0000\u0000\u000fT\u0001\u0000\u0000\u0000\u0011[\u0001"+
		"\u0000\u0000\u0000\u0013h\u0001\u0000\u0000\u0000\u0015s\u0001\u0000\u0000"+
		"\u0000\u0017{\u0001\u0000\u0000\u0000\u0019\u0083\u0001\u0000\u0000\u0000"+
		"\u001b\u0085\u0001\u0000\u0000\u0000\u001d\u0087\u0001\u0000\u0000\u0000"+
		"\u001f\u0089\u0001\u0000\u0000\u0000!\u008b\u0001\u0000\u0000\u0000#\u008d"+
		"\u0001\u0000\u0000\u0000%\u008f\u0001\u0000\u0000\u0000\'\u0091\u0001"+
		"\u0000\u0000\u0000)\u0093\u0001\u0000\u0000\u0000+\u0098\u0001\u0000\u0000"+
		"\u0000-\u00a2\u0001\u0000\u0000\u0000/\u00a4\u0001\u0000\u0000\u00001"+
		"\u00ae\u0001\u0000\u0000\u000034\u0005\r\u0000\u000047\u0005\n\u0000\u0000"+
		"57\u0007\u0000\u0000\u000063\u0001\u0000\u0000\u000065\u0001\u0000\u0000"+
		"\u00007\u0002\u0001\u0000\u0000\u00008:\u0007\u0001\u0000\u000098\u0001"+
		"\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000"+
		";<\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=>\u0006\u0001\u0000"+
		"\u0000>\u0004\u0001\u0000\u0000\u0000?@\u0005v\u0000\u0000@A\u0005a\u0000"+
		"\u0000AB\u0005r\u0000\u0000B\u0006\u0001\u0000\u0000\u0000CD\u0005v\u0000"+
		"\u0000DE\u0005a\u0000\u0000EF\u0005l\u0000\u0000F\b\u0001\u0000\u0000"+
		"\u0000GH\u0005p\u0000\u0000HI\u0005r\u0000\u0000IJ\u0005i\u0000\u0000"+
		"JK\u0005n\u0000\u0000KL\u0005t\u0000\u0000L\n\u0001\u0000\u0000\u0000"+
		"MN\u0005a\u0000\u0000NO\u0005s\u0000\u0000O\f\u0001\u0000\u0000\u0000"+
		"PQ\u0005I\u0000\u0000QR\u0005n\u0000\u0000RS\u0005t\u0000\u0000S\u000e"+
		"\u0001\u0000\u0000\u0000TU\u0005D\u0000\u0000UV\u0005o\u0000\u0000VW\u0005"+
		"u\u0000\u0000WX\u0005b\u0000\u0000XY\u0005l\u0000\u0000YZ\u0005e\u0000"+
		"\u0000Z\u0010\u0001\u0000\u0000\u0000[\\\u0005B\u0000\u0000\\]\u0005o"+
		"\u0000\u0000]^\u0005o\u0000\u0000^_\u0005l\u0000\u0000_\u0012\u0001\u0000"+
		"\u0000\u0000`d\u0003/\u0017\u0000ac\u0003-\u0016\u0000ba\u0001\u0000\u0000"+
		"\u0000cf\u0001\u0000\u0000\u0000db\u0001\u0000\u0000\u0000de\u0001\u0000"+
		"\u0000\u0000ei\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000\u0000gi\u0003"+
		"-\u0016\u0000h`\u0001\u0000\u0000\u0000hg\u0001\u0000\u0000\u0000i\u0014"+
		"\u0001\u0000\u0000\u0000jk\u0005t\u0000\u0000kl\u0005r\u0000\u0000lm\u0005"+
		"u\u0000\u0000mt\u0005e\u0000\u0000no\u0005f\u0000\u0000op\u0005a\u0000"+
		"\u0000pq\u0005l\u0000\u0000qr\u0005s\u0000\u0000rt\u0005e\u0000\u0000"+
		"sj\u0001\u0000\u0000\u0000sn\u0001\u0000\u0000\u0000t\u0016\u0001\u0000"+
		"\u0000\u0000uw\u00031\u0018\u0000vu\u0001\u0000\u0000\u0000vw\u0001\u0000"+
		"\u0000\u0000wx\u0001\u0000\u0000\u0000xy\u0005.\u0000\u0000y|\u00031\u0018"+
		"\u0000z|\u00031\u0018\u0000{v\u0001\u0000\u0000\u0000{z\u0001\u0000\u0000"+
		"\u0000|\u0018\u0001\u0000\u0000\u0000}~\u0003\u0017\u000b\u0000~\u007f"+
		"\u0007\u0002\u0000\u0000\u007f\u0084\u0001\u0000\u0000\u0000\u0080\u0081"+
		"\u00031\u0018\u0000\u0081\u0082\u0007\u0002\u0000\u0000\u0082\u0084\u0001"+
		"\u0000\u0000\u0000\u0083}\u0001\u0000\u0000\u0000\u0083\u0080\u0001\u0000"+
		"\u0000\u0000\u0084\u001a\u0001\u0000\u0000\u0000\u0085\u0086\u0005+\u0000"+
		"\u0000\u0086\u001c\u0001\u0000\u0000\u0000\u0087\u0088\u0005-\u0000\u0000"+
		"\u0088\u001e\u0001\u0000\u0000\u0000\u0089\u008a\u0005*\u0000\u0000\u008a"+
		" \u0001\u0000\u0000\u0000\u008b\u008c\u0005/\u0000\u0000\u008c\"\u0001"+
		"\u0000\u0000\u0000\u008d\u008e\u0005=\u0000\u0000\u008e$\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0005(\u0000\u0000\u0090&\u0001\u0000\u0000\u0000\u0091"+
		"\u0092\u0005)\u0000\u0000\u0092(\u0001\u0000\u0000\u0000\u0093\u0094\u0005"+
		":\u0000\u0000\u0094*\u0001\u0000\u0000\u0000\u0095\u0097\u0007\u0003\u0000"+
		"\u0000\u0096\u0095\u0001\u0000\u0000\u0000\u0097\u009a\u0001\u0000\u0000"+
		"\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000"+
		"\u0000\u0099\u009b\u0001\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000"+
		"\u0000\u009b\u009f\u0007\u0004\u0000\u0000\u009c\u009e\u0007\u0005\u0000"+
		"\u0000\u009d\u009c\u0001\u0000\u0000\u0000\u009e\u00a1\u0001\u0000\u0000"+
		"\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000"+
		"\u0000\u00a0,\u0001\u0000\u0000\u0000\u00a1\u009f\u0001\u0000\u0000\u0000"+
		"\u00a2\u00a3\u000209\u0000\u00a3.\u0001\u0000\u0000\u0000\u00a4\u00a5"+
		"\u000219\u0000\u00a50\u0001\u0000\u0000\u0000\u00a6\u00aa\u0003-\u0016"+
		"\u0000\u00a7\u00a9\u0003-\u0016\u0000\u00a8\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a9\u00ac\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000"+
		"\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u00af\u0001\u0000\u0000\u0000"+
		"\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ad\u00af\u0003-\u0016\u0000\u00ae"+
		"\u00a6\u0001\u0000\u0000\u0000\u00ae\u00ad\u0001\u0000\u0000\u0000\u00af"+
		"2\u0001\u0000\u0000\u0000\r\u00006;dhsv{\u0083\u0098\u009f\u00aa\u00ae"+
		"\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}