/*
 * InformationFieldParser.java
 *
 * Created on February 19, 2002, 5:28 PM
 */
package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import java.text.*;

/**
 * @author  deruelle
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 */
public class InformationFieldParser extends SDPParser {

	/** Creates new InformationFieldParser */
	public InformationFieldParser(String informationField) {
		this.lexer = new Lexer("charLexer", informationField);
	}

	public InformationField informationField() throws ParseException {
		try {
			this.lexer.match('i');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			InformationField informationField = new InformationField();
			String rest = lexer.getRest();
			informationField.setInformation(rest.trim());

			return informationField;
		} catch (Exception e) {
			throw new ParseException(lexer.getBuffer(), lexer.getPtr());
		}
	}

	public SDPField parse() throws ParseException {
		return this.informationField();
	}

	/**
	    public static void main(String[] args) throws ParseException {
		    String information[] = {
				"i=A Seminar on the session description protocol\n"
	                };
	
		    for (int i = 0; i < information.length; i++) {
		       InformationFieldParser informationFieldParser=new InformationFieldParser(
	                information[i] );
			InformationField informationField=
	                    informationFieldParser.informationField();
	                
			System.out.println("encoded: " +informationField.encode());
		    }
	
		}
	**/
}
/*
 * $Log: InformationFieldParser.java,v $
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
