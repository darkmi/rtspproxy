package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import gov.nist.core.*;
import java.text.ParseException;

/**
 * Parser For the Zone field.
 *
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 *
 * @author Olivier Deruelle <deruelle@antd.nist.gov>  
 * @author M. Ranganathan <mranga@antd.nist.gov> <br/>
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 *
 */
public class ZoneFieldParser extends SDPParser {

	/** Creates new ZoneFieldParser */
	public ZoneFieldParser(String zoneField) {
		lexer = new Lexer("charLexer", zoneField);
	}

	/** Get the sign of the offset
	 * @param String tokenValue to set
	 * @return String
	 */
	public String getSign(String tokenValue) {
		if (tokenValue.startsWith("-"))
			return "-";
		else
			return "+";
	}

	/** Get the typed time
	  * @param String tokenValue to set
	  * @return TypedTime
	  */
	public TypedTime getTypedTime(String tokenValue) {
		TypedTime typedTime = new TypedTime();
		String offset = null;
		if (tokenValue.startsWith("-"))
			offset = tokenValue.replace('-', ' ');
		else if (tokenValue.startsWith("+"))
			offset = tokenValue.replace('+', ' ');
		else
			offset = tokenValue;

		if (offset.endsWith("d")) {
			typedTime.setUnit("d");
			String t = offset.replace('d', ' ');

			typedTime.setTime(Integer.parseInt(t.trim()));
		} else if (offset.endsWith("h")) {
			typedTime.setUnit("h");
			String t = offset.replace('h', ' ');
			typedTime.setTime(Integer.parseInt(t.trim()));
		} else if (offset.endsWith("m")) {
			typedTime.setUnit("m");
			String t = offset.replace('m', ' ');
			typedTime.setTime(Integer.parseInt(t.trim()));
		} else {
			typedTime.setUnit("s");
			if (offset.endsWith("s")) {
				String t = offset.replace('s', ' ');
				typedTime.setTime(Integer.parseInt(t.trim()));
			} else
				typedTime.setTime(Integer.parseInt(offset.trim()));
		}
		return typedTime;
	}

	/** parse the Zone field string
	 * @return ZoneField 
	 */
	public ZoneField zoneField() throws ParseException {
		try {
			ZoneField zoneField = new ZoneField();

			this.lexer.match('z');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			// The zoneAdjustment list:
			while (lexer.lookAhead(0) != '\n') {
				ZoneAdjustment zoneAdjustment = new ZoneAdjustment();

				lexer.match(LexerCore.ID);
				Token time = lexer.getNextToken();
				this.lexer.SPorHT();
				zoneAdjustment.setTime(Long.parseLong(time.getTokenValue()));

				lexer.match(LexerCore.ID);
				Token offset = lexer.getNextToken();
				this.lexer.SPorHT();
				String sign = getSign(offset.getTokenValue());
				TypedTime typedTime = getTypedTime(offset.getTokenValue());
				zoneAdjustment.setSign(sign);
				zoneAdjustment.setOffset(typedTime);

				zoneField.addZoneAdjustment(zoneAdjustment);
			}

			return zoneField;
		} catch (Exception e) {
			throw lexer.createParseException();
		}
	}

	public SDPField parse() throws ParseException {
		return this.zoneField();
	}

	/**
	    public static void main(String[] args) throws ParseException {
	        String zone[] = {
	                        "z=2882844526 -1h 2898848070 0\n",
				"z=2886 +1h 2898848070 10 23423 -6s \n"
	                };
	
		    for (int i = 0; i <zone.length; i++) {
		        ZoneFieldParser zoneFieldParser=new ZoneFieldParser(
	                zone[i] );
		        ZoneField zoneField=zoneFieldParser.zoneField();
			System.out.println("encoded: " +zoneField.encode());
		    }
	
		}
	**/

}
/*
 * $Log: ZoneFieldParser.java,v $
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
