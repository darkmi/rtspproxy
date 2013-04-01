package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import gov.nist.core.*;
import java.text.*;

/**
 * Parser for Proto Version.
 *
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 *
 * @author Olivier Deruelle <deruelle@antd.nist.gov> 
 * @author M. Ranganathan <mranga@nist.gov>
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 *
 */
public class ProtoVersionFieldParser extends SDPParser {

	/** Creates new ProtoVersionFieldParser */
	public ProtoVersionFieldParser(String protoVersionField) {
		this.lexer = new Lexer("charLexer", protoVersionField);
	}

	public ProtoVersionField protoVersionField() throws ParseException {
		try {
			this.lexer.match('v');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			ProtoVersionField protoVersionField = new ProtoVersionField();
			lexer.match(Lexer.ID);
			Token version = lexer.getNextToken();
			protoVersionField.setProtoVersion(
				Integer.parseInt(version.getTokenValue()));
			this.lexer.SPorHT();

			return protoVersionField;
		} catch (Exception e) {
			throw lexer.createParseException();
		}
	}

	public SDPField parse() throws ParseException {
		return this.protoVersionField();
	}

	/**
	    public static void main(String[] args) throws ParseException {
		    String protoVersion[] = {
				"v=0\n"
	                };
	
		    for (int i = 0; i < protoVersion.length; i++) {
		        ProtoVersionFieldParser protoVersionFieldParser=
					new ProtoVersionFieldParser(
	                protoVersion[i] );
			ProtoVersionField protoVersionField =
				protoVersionFieldParser.protoVersionField();
			System.out.println
				("encoded: " +protoVersionField.encode());
		    }
	
		}
	**/

}
/*
 * $Log: ProtoVersionFieldParser.java,v $
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
