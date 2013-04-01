/*
 * SessionNameFieldParser.java
 *
 * Created on February 25, 2002, 10:26 AM
 */

package gov.nist.javax.sdp.parser;
import gov.nist.javax.sdp.fields.*;
import java.text.*;

/**
 * @author  deruelle
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 */
public class SessionNameFieldParser extends SDPParser {

	/** Creates new SessionNameFieldParser */
	public SessionNameFieldParser(String sessionNameField) {
		this.lexer = new Lexer("charLexer", sessionNameField);
	}

	/** Get the SessionNameField
	 * @return SessionNameField
	 */
	public SessionNameField sessionNameField() throws ParseException {
		try {
			this.lexer.match('s');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			SessionNameField sessionNameField = new SessionNameField();
			String rest = lexer.getRest();
			sessionNameField.setSessionName(rest.trim());

			return sessionNameField;
		} catch (Exception e) {
			throw lexer.createParseException();
		}

	}

	public SDPField parse() throws ParseException {
		return this.sessionNameField();
	}

	public static void main(String[] args) throws ParseException {
		String session[] = { "s=SDP Seminar \n", "s= Session SDP\n" };

		for (int i = 0; i < session.length; i++) {
			SessionNameFieldParser sessionNameFieldParser =
				new SessionNameFieldParser(session[i]);
			SessionNameField sessionNameField =
				sessionNameFieldParser.sessionNameField();
			System.out.println("encoded: " + sessionNameField.encode());
		}
	}
}
/*
 * $Log: SessionNameFieldParser.java,v $
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
