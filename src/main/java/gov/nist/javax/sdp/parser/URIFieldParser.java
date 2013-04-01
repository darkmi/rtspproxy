/*
 * URIFieldParser.java
 *
 * Created on February 25, 2002, 11:10 AM
 */
package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import java.text.ParseException;

/**
 * URI Field Parser.
 *
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 *
 * @author Olivier Deruelle <deruelle@nist.gov>  
 * @author M. Ranganathan <mranga@nist.gov> <br/>
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 *
 */
public class URIFieldParser extends SDPParser {

	/** Creates new URIFieldParser */
	public URIFieldParser(String uriField) {
		this.lexer = new Lexer("charLexer", uriField);
	}

	/** Get the URI field
	 * @return URIField
	 */
	public URIField uriField() throws ParseException {
		try {
			this.lexer.match('u');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			URIField uriField = new URIField();
			String rest = lexer.getRest().trim();
			uriField.setURI(rest);
			return uriField;
		} catch (Exception e) {
			throw lexer.createParseException();
		}
	}

	public SDPField parse() throws ParseException {
		return this.uriField();
	}

	/**
	    
	    public static void main(String[] args) throws ParseException {
		    String uri[] = {
				"u=http://www.cs.ucl.ac.uk/staff/M.Handley/sdp.03.ps\n",
	                        "u=sip:j.doe@big.com\n",
	                        "u=sip:j.doe:secret@big.com;transport=tcp\n",
	                        "u=sip:j.doe@big.com?subject=project\n",
	                        "u=sip:+1-212-555-1212:1234@gateway.com;user=phone\n"
	                };
	
		    for (int i = 0; i < uri.length; i++) {
		       URIFieldParser uriFieldParser=new URIFieldParser(
	                uri[i] );
			URIField uriField=uriFieldParser.uriField();
			System.out.println("toParse: " +uri[i]);
			System.out.println("encoded: " +uriField.encode());
		    }
	
		}
	**/
}
/*
 * $Log: URIFieldParser.java,v $
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
