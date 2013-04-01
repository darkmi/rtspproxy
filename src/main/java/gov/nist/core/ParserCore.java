package gov.nist.core;

import java.text.ParseException;

/** Generic parser class.
* All parsers inherit this class.
*
*@version  JAIN-SIP-1.1
*
*@author M. Ranganathan <mranga@nist.gov>  <br/>
*
*<a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
*
*/
public abstract class ParserCore {
	public static final boolean debug = Debug.parserDebug;

	protected static int nesting_level;

	protected LexerCore lexer;

	
	protected NameValue nameValue(char separator) throws ParseException  {
		if (debug) dbg_enter("nameValue");
		try {
               
		lexer.match(LexerCore.ID);
		Token name = lexer.getNextToken();
		// eat white space.
		lexer.SPorHT();
		try {
         
                    
		        boolean quoted = false;

			char la = lexer.lookAhead(0);
                       
			if (la == separator ) {
				lexer.consume(1);
				lexer.SPorHT();
				String str = null;
				if (lexer.lookAhead(0) == '\"')  {
					 str = lexer.quotedString();
					  quoted = true;
				} else {
				   lexer.match(LexerCore.ID);
				   Token value = lexer.getNextToken();
				   str = value.tokenValue;
				}
				NameValue nv = 
				new NameValue(name.tokenValue,str);
				if (quoted) nv.setQuotedValue();
				return nv;
			}  else {
				return new NameValue(name.tokenValue,null);
			}
		} catch (ParseException ex) {
			return new NameValue(name.tokenValue,null);
		}

		} finally {
			if (debug) dbg_leave("nameValue");
		}


	}

	protected  void dbg_enter(String rule) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < nesting_level ; i++) 
			stringBuffer.append(">");
		    
		if (debug)  {
			System.out.println(
				stringBuffer + rule + 
				"\nlexer buffer = \n" + 
				lexer.getRest());
		}
		nesting_level++;
	}

	protected void dbg_leave(String rule) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < nesting_level ; i++) 
			stringBuffer.append("<");
		    
		if (debug)  {
			System.out.println(
				stringBuffer +
				rule + 
				"\nlexer buffer = \n" + 
				lexer.getRest());
		}
		nesting_level --;
	}
	
	protected NameValue nameValue() throws ParseException  {
		return nameValue('=');
	}
	
	protected void peekLine(String rule) {
		if (debug) {
			Debug.println(rule +" " + lexer.peekLine());
		}
	}
}


/*
 * $Log: ParserCore.java,v $
 * Revision 1.4  2004/01/22 13:26:27  sverker
 * Issue number:
 * Obtained from:
 * Submitted by:  sverker
 * Reviewed by:   mranga
 *
 * Major reformat of code to conform with style guide. Resolved compiler and javadoc warnings. Added CVS tags.
 *
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 */
