package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import gov.nist.core.*;
import java.text.*;

/**
 * Parser for key field. Ack: bug fix contributed by espen@java.net
 *
 * @author  deruelle
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 */
public class KeyFieldParser extends SDPParser {

	/** Creates new KeyFieldParser */
	public KeyFieldParser(String keyField) {
		this.lexer = new Lexer("charLexer", keyField);
	}

	public KeyField keyField() throws ParseException {
		try {
			this.lexer.match('k');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			KeyField keyField = new KeyField();
			//Espen: Stealing the approach from AttributeFieldParser from from here...
			NameValue nameValue = new NameValue();

			int ptr = this.lexer.markInputPosition();
			try {
				String name = lexer.getNextToken(':');
				this.lexer.consume(1);
				String value = lexer.getRest();
				nameValue = new NameValue(name.trim(), value.trim());
			} catch (ParseException ex) {
				this.lexer.rewindInputPosition(ptr);
				String rest = this.lexer.getRest();
				if (rest == null)
					throw new ParseException(
						this.lexer.getBuffer(),
						this.lexer.getPtr());
				nameValue = new NameValue(rest.trim(), null);
			}
			keyField.setType(nameValue.getName());
			keyField.setKeyData((String) nameValue.getValue());
			this.lexer.SPorHT();

			return keyField;
		} catch (Exception e) {
			throw new ParseException(lexer.getBuffer(), lexer.getPtr());
		}
	}

	public SDPField parse() throws ParseException {
		return this.keyField();
	}

	/**
	    public static void main(String[] args) throws ParseException {
		    String key[] = {
				"k=clear:1234124\n",
	                        "k=base64:12\n",
	                        "k=http://www.cs.ucl.ac.uk/staff/M.Handley/sdp.03.ps\n",
	                        "k=prompt\n"
	                };
	
		    for (int i = 0; i < key.length; i++) {
		       KeyFieldParser keyFieldParser=new KeyFieldParser(
	                key[i] );
			KeyField keyField=keyFieldParser.keyField();
			System.out.println("toParse: " +key[i]);
			System.out.println("encoded: " +keyField.encode());
		    }
	
		}
	**/
}
/*
 * $Log: KeyFieldParser.java,v $
 * Revision 1.3  2004/01/22 13:26:28  sverker
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
