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
		"\u0004\u0000\u0012\u0083\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000)\b\u0000\u0001\u0001"+
		"\u0004\u0001,\b\u0001\u000b\u0001\f\u0001-\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b"+
		"\u0001\b\u0005\bR\b\b\n\b\f\bU\t\b\u0003\bW\b\b\u0001\t\u0001\t\u0001"+
		"\t\u0005\t\\\b\t\n\t\f\t_\t\t\u0001\t\u0001\t\u0004\tc\b\t\u000b\t\f\t"+
		"d\u0003\tg\b\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f"+
		"\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0005\u0011x\b\u0011\n\u0011\f\u0011{\t"+
		"\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u007f\b\u0011\n\u0011\f\u0011"+
		"\u0082\t\u0011\u0000\u0000\u0012\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012\u0001"+
		"\u0000\u0007\u0002\u0000\n\nrr\u0002\u0000\t\t  \u0001\u000019\u0001\u0000"+
		"09\u0001\u0000__\u0001\u0000az\u0004\u000009AZ__az\u008b\u0000\u0001\u0001"+
		"\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"+
		"\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"+
		"\u0000#\u0001\u0000\u0000\u0000\u0001(\u0001\u0000\u0000\u0000\u0003+"+
		"\u0001\u0000\u0000\u0000\u00051\u0001\u0000\u0000\u0000\u00075\u0001\u0000"+
		"\u0000\u0000\t9\u0001\u0000\u0000\u0000\u000b?\u0001\u0000\u0000\u0000"+
		"\rB\u0001\u0000\u0000\u0000\u000fF\u0001\u0000\u0000\u0000\u0011V\u0001"+
		"\u0000\u0000\u0000\u0013f\u0001\u0000\u0000\u0000\u0015h\u0001\u0000\u0000"+
		"\u0000\u0017j\u0001\u0000\u0000\u0000\u0019l\u0001\u0000\u0000\u0000\u001b"+
		"n\u0001\u0000\u0000\u0000\u001dp\u0001\u0000\u0000\u0000\u001fr\u0001"+
		"\u0000\u0000\u0000!t\u0001\u0000\u0000\u0000#y\u0001\u0000\u0000\u0000"+
		"%&\u0005\r\u0000\u0000&)\u0005\n\u0000\u0000\')\u0007\u0000\u0000\u0000"+
		"(%\u0001\u0000\u0000\u0000(\'\u0001\u0000\u0000\u0000)\u0002\u0001\u0000"+
		"\u0000\u0000*,\u0007\u0001\u0000\u0000+*\u0001\u0000\u0000\u0000,-\u0001"+
		"\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000"+
		"./\u0001\u0000\u0000\u0000/0\u0006\u0001\u0000\u00000\u0004\u0001\u0000"+
		"\u0000\u000012\u0005v\u0000\u000023\u0005a\u0000\u000034\u0005r\u0000"+
		"\u00004\u0006\u0001\u0000\u0000\u000056\u0005l\u0000\u000067\u0005e\u0000"+
		"\u000078\u0005t\u0000\u00008\b\u0001\u0000\u0000\u00009:\u0005p\u0000"+
		"\u0000:;\u0005r\u0000\u0000;<\u0005i\u0000\u0000<=\u0005n\u0000\u0000"+
		"=>\u0005t\u0000\u0000>\n\u0001\u0000\u0000\u0000?@\u0005a\u0000\u0000"+
		"@A\u0005s\u0000\u0000A\f\u0001\u0000\u0000\u0000BC\u0005I\u0000\u0000"+
		"CD\u0005n\u0000\u0000DE\u0005t\u0000\u0000E\u000e\u0001\u0000\u0000\u0000"+
		"FG\u0005D\u0000\u0000GH\u0005e\u0000\u0000HI\u0005c\u0000\u0000IJ\u0005"+
		"i\u0000\u0000JK\u0005m\u0000\u0000KL\u0005a\u0000\u0000LM\u0005l\u0000"+
		"\u0000M\u0010\u0001\u0000\u0000\u0000NW\u00050\u0000\u0000OS\u0007\u0002"+
		"\u0000\u0000PR\u0007\u0003\u0000\u0000QP\u0001\u0000\u0000\u0000RU\u0001"+
		"\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000"+
		"TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000VN\u0001\u0000\u0000"+
		"\u0000VO\u0001\u0000\u0000\u0000W\u0012\u0001\u0000\u0000\u0000Xg\u0005"+
		"0\u0000\u0000Y]\u0007\u0002\u0000\u0000Z\\\u0007\u0003\u0000\u0000[Z\u0001"+
		"\u0000\u0000\u0000\\_\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000"+
		"]^\u0001\u0000\u0000\u0000^`\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000"+
		"\u0000`b\u0005.\u0000\u0000ac\u0007\u0003\u0000\u0000ba\u0001\u0000\u0000"+
		"\u0000cd\u0001\u0000\u0000\u0000db\u0001\u0000\u0000\u0000de\u0001\u0000"+
		"\u0000\u0000eg\u0001\u0000\u0000\u0000fX\u0001\u0000\u0000\u0000fY\u0001"+
		"\u0000\u0000\u0000g\u0014\u0001\u0000\u0000\u0000hi\u0005+\u0000\u0000"+
		"i\u0016\u0001\u0000\u0000\u0000jk\u0005-\u0000\u0000k\u0018\u0001\u0000"+
		"\u0000\u0000lm\u0005*\u0000\u0000m\u001a\u0001\u0000\u0000\u0000no\u0005"+
		"/\u0000\u0000o\u001c\u0001\u0000\u0000\u0000pq\u0005=\u0000\u0000q\u001e"+
		"\u0001\u0000\u0000\u0000rs\u0005(\u0000\u0000s \u0001\u0000\u0000\u0000"+
		"tu\u0005)\u0000\u0000u\"\u0001\u0000\u0000\u0000vx\u0007\u0004\u0000\u0000"+
		"wv\u0001\u0000\u0000\u0000x{\u0001\u0000\u0000\u0000yw\u0001\u0000\u0000"+
		"\u0000yz\u0001\u0000\u0000\u0000z|\u0001\u0000\u0000\u0000{y\u0001\u0000"+
		"\u0000\u0000|\u0080\u0007\u0005\u0000\u0000}\u007f\u0007\u0006\u0000\u0000"+
		"~}\u0001\u0000\u0000\u0000\u007f\u0082\u0001\u0000\u0000\u0000\u0080~"+
		"\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000\u0081$\u0001"+
		"\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\n\u0000(-SV]df"+
		"y\u0080\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}