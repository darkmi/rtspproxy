/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/
package gov.nist.javax.sdp.fields;
import gov.nist.core.*;
import javax.sdp.*;
/**
* Phone Field SDP header
*
*@version  JSR141-PUBLIC-REVIEW (subject to change).
*
*@author Olivier Deruelle <deruelle@antd.nist.gov>
*@author M. Ranganathan <mranga@nist.gov>  <br/>
*
*<a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
*
*/
@SuppressWarnings("serial")
public class PhoneField extends SDPField implements javax.sdp.Phone {
	protected String name;
	protected String phoneNumber;

	public PhoneField() {
		super(PHONE_FIELD);
	}

	public String getName() {
		return name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	* Set the name member  
	    *
	    *@param name - the name to set.
	*/
	public void setName(String name) {
		this.name = name;
	}
	/**
	* Set the phoneNumber member 
	    *@param phoneNumber - phone number to set. 
	*/
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/** Returns the value.
	 * @throws SdpParseException
	 * @return the value.
	 */
	public String getValue() throws SdpParseException {
		return getName();
	}

	/** Sets the value.
	 * @param value the - new information.
	 * @throws SdpException if the value is null
	 */
	public void setValue(String value) throws SdpException {
		if (value == null)
			throw new SdpException("The value parameter is null");
		else
			setName(value);
	}

	/**
	 *  Get the string encoded version of this object
	 * @since v1.0
	 * Here, we implement only the "name <phoneNumber>" form
	 * and not the "phoneNumber (name)" form
	 */
	public String encode() {
		String encoded_string;
		encoded_string = PHONE_FIELD;
		if (name != null) {
			encoded_string += name + Separators.LESS_THAN;
		}
		encoded_string += phoneNumber;
		if (name != null) {
			encoded_string += Separators.GREATER_THAN;
		}
		encoded_string += Separators.NEWLINE;
		return encoded_string;
	}

}
/*
 * $Log: PhoneField.java,v $
 * Revision 1.2  2004/01/22 13:26:27  sverker
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
