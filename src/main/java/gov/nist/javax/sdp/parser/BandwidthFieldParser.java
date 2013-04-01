package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import gov.nist.core.*;
import java.text.*;

/**
 * @author  deruelle
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 */
public class BandwidthFieldParser extends SDPParser {

	/** Creates new BandwidthFieldParser */
	public BandwidthFieldParser(String bandwidthField) {
		this.lexer = new Lexer("charLexer", bandwidthField);
	}

	public BandwidthField bandwidthField() throws ParseException {
		try {
			this.lexer.match('b');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			BandwidthField bandwidthField = new BandwidthField();

			NameValue nameValue = nameValue(':');
			String name = nameValue.getName();
			String value = (String) nameValue.getValue();

			bandwidthField.setBandwidth(Integer.parseInt(value.trim()));
			bandwidthField.setBwtype(name);

			this.lexer.SPorHT();
			return bandwidthField;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException(lexer.getBuffer(), lexer.getPtr());
		}
	}

	public SDPField parse() throws ParseException {
		return this.bandwidthField();
	}

	/**
	    public static void main(String[] args) throws ParseException {
		    String bandwidth[] = {
				"b=X-YZ:128\n",
				"b=CT: 31\n",
				"b=AS:0 \n",
	                        "b= AS:4\n"
	                };
	
		    for (int i = 0; i < bandwidth.length; i++) {
		        BandwidthFieldParser bandwidthFieldParser=new BandwidthFieldParser(
	                bandwidth[i] );
			System.out.println("toParse: " + bandwidth[i]);
			BandwidthField bandwidthField = bandwidthFieldParser.bandwidthField();
			System.out.println("encoded: " + bandwidthField.encode());
		    }
	
		}
	**/
}
/*
 * $Log: BandwidthFieldParser.java,v $
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
