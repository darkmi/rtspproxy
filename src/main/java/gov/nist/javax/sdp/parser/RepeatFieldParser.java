package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import gov.nist.core.*;
import java.text.ParseException;

/** Parser for Repeat field.
*
*@version  JAIN-SIP-1.1
*
*@author Olivier Deruelle <deruelle@nist.gov>
*@author M. Ranganathan <mranga@nist.gov>  <br/>
*
*<a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
*
 */
public class RepeatFieldParser extends SDPParser {

	/** Creates new RepeatFieldsParser */
	public RepeatFieldParser(String repeatField) {
		lexer = new Lexer("charLexer", repeatField);
	}

	/** Get the typed time
	 * @param String tokenValue to set
	 * @return TypedTime
	 */
	public TypedTime getTypedTime(String tokenValue) {
		TypedTime typedTime = new TypedTime();

		if (tokenValue.endsWith("d")) {
			typedTime.setUnit("d");
			String t = tokenValue.replace('d', ' ');

			typedTime.setTime(Integer.parseInt(t.trim()));
		} else if (tokenValue.endsWith("h")) {
			typedTime.setUnit("h");
			String t = tokenValue.replace('h', ' ');
			typedTime.setTime(Integer.parseInt(t.trim()));
		} else if (tokenValue.endsWith("m")) {
			typedTime.setUnit("m");
			String t = tokenValue.replace('m', ' ');
			typedTime.setTime(Integer.parseInt(t.trim()));
		} else {
			typedTime.setUnit("s");
			if (tokenValue.endsWith("s")) {
				String t = tokenValue.replace('s', ' ');
				typedTime.setTime(Integer.parseInt(t.trim()));
			} else
				typedTime.setTime(Integer.parseInt(tokenValue.trim()));
		}
		return typedTime;
	}

	/** parse the field string
	 * @return RepeatFields 
	 */
	public RepeatField repeatField() throws ParseException {
		try {

			this.lexer.match('r');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			RepeatField repeatField = new RepeatField();

			lexer.match(LexerCore.ID);
			Token repeatInterval = lexer.getNextToken();
			this.lexer.SPorHT();
			TypedTime typedTime = getTypedTime(repeatInterval.getTokenValue());
			repeatField.setRepeatInterval(typedTime);

			lexer.match(LexerCore.ID);
			Token activeDuration = lexer.getNextToken();
			this.lexer.SPorHT();
			typedTime = getTypedTime(activeDuration.getTokenValue());
			repeatField.setActiveDuration(typedTime);

			// The offsets list:
			while (lexer.lookAhead(0) != '\n') {
				lexer.match(LexerCore.ID);
				Token offsets = lexer.getNextToken();
				this.lexer.SPorHT();
				typedTime = getTypedTime(offsets.getTokenValue());
				repeatField.addOffset(typedTime);
			}

			return repeatField;
		} catch (Exception e) {
			throw lexer.createParseException();
		}
	}

	public SDPField parse() throws ParseException {
		return this.repeatField();
	}

	/**
	    
	    public static void main(String[] args) throws ParseException {
	        String repeat[] = {
	                        "r=604800s 3600s 0s 90000s\n",
				"r=7d 1h 0 25h\n",
	                        "r=7 6 5 4 3 2 1 0 \n" 
	                };
	
		    for (int i = 0; i < repeat.length; i++) {
		        RepeatFieldParser repeatFieldParser=new RepeatFieldParser(
	                repeat[i] );
		        RepeatField repeatFields=repeatFieldParser.repeatField();
			System.out.println("toParse: " +repeat[i]);
			System.out.println("encoded: " +repeatFields.encode());
		    }
	
		}
	**/
}
/*
 * $Log: RepeatFieldParser.java,v $
 * Revision 1.2  2004/01/22 13:26:28  sverker
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
