package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.*;
import java.text.ParseException;

/**
 * Parser for the Phone field.
 *
 * @version JAIN-SDP-PUBLIC-RELEASE $Revision: 268 $ $Date: 2004/01/22 13:26:28 $
 *
 * @author Olivier Deruelle <deruelle@nist.gov>  
 * @author M. Ranganathan <mranga@nist.gov>
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 *
 */
public class PhoneFieldParser extends SDPParser {

	/** Creates new PhoneFieldParser */
	public PhoneFieldParser(String phoneField) {
		lexer = new Lexer("charLexer", phoneField);
	}

	public String getDisplayName(String rest) {
		String retval = null;

		try {
			int begin = rest.indexOf("(");
			int end = rest.indexOf(")");

			if (begin != -1) {
				// p=+44-171-380-7777 (Mark Handley)
				retval = rest.substring(begin + 1, end);
			} else {
				// The alternative RFC822 name quoting convention is 
				// also allowed for
				// email addresses. ex: p=Mark Handley <+44-171-380-7777>
				int ind = rest.indexOf("<");
				if (ind != -1) {
					retval = rest.substring(0, ind);
				} else {
					// There is no display name !!!
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}

	public String getPhoneNumber(String rest) throws ParseException {
		String phoneNumber = null;

		try {
			int begin = rest.indexOf("(");

			if (begin != -1) {
				// p=+44-171-380-7777 (Mark Handley)
				phoneNumber = rest.substring(0, begin).trim();
			} else {
				// The alternative RFC822 name quoting convention is also allowed for
				// email addresses. ex: p=Mark Handley <+44-171-380-7777>
				int ind = rest.indexOf("<");
				int end = rest.indexOf(">");

				if (ind != -1) {
					phoneNumber = rest.substring(ind + 1, end);
				} else {
					// p=+44-171-380-7777
					phoneNumber = rest.trim();
				}
			}
		} catch (Exception e) {
			throw new ParseException(lexer.getBuffer(), lexer.getPtr());
		}
		return phoneNumber;
	}

	public PhoneField phoneField() throws ParseException {
		try {

			this.lexer.match('p');
			this.lexer.SPorHT();
			this.lexer.match('=');
			this.lexer.SPorHT();

			PhoneField phoneField = new PhoneField();
			String rest = lexer.getRest();

			String displayName = getDisplayName(rest.trim());
			phoneField.setName(displayName);
			String phoneNumber = getPhoneNumber(rest);
			phoneField.setPhoneNumber(phoneNumber);

			return phoneField;
		} catch (Exception e) {
			throw new ParseException(lexer.getBuffer(), lexer.getPtr());
		}
	}

	public SDPField parse() throws ParseException {
		return this.phoneField();
	}

	/**
	    
	    public static void main(String[] args) throws ParseException {
		    String phone[] = {
				"p=+44-171-380-7777 (Mark Handley)\n",
				"p=Mark Handley <+1 617 253 6011>\n",
	                        "p= <+44-171-380-7777>\n",
	                        "p=+44-171-380-7777\n"
	                };
	
		    for (int i = 0; i < phone.length; i++) {
		       PhoneFieldParser phoneFieldParser=new PhoneFieldParser(
	                phone[i] );
		       PhoneField phoneFields=phoneFieldParser.phoneField();
	               System.out.println("toParse: " + phone[i]);
			System.out.println("encoded: " +phoneFields.encode());
		    }
	
		}
	
	**/
}
/*
 * $Log: PhoneFieldParser.java,v $
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
