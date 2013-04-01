/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/
package gov.nist.javax.sdp.fields;

import gov.nist.core.*;
import javax.sdp.*;

/**
*   Key field part of an SDP header. 
* Acknowledgement. Bug fix contributed by espen@java.net
*
*@version  JSR141-PUBLIC-REVIEW (subject to change)
*
*@author Oliver Deruelle <deruelle@antd.nist.gov> 
*@author M. Ranganathan <mranga@nist.gov>  <br/>
*/
@SuppressWarnings("serial")
public class KeyField extends SDPField implements javax.sdp.Key {
	protected String type;
	protected String keyData;

	public KeyField() {
		super(KEY_FIELD);
	}

	public String getType() {
		return type;
	}

	public String getKeyData() {
		return keyData;
	}

	/**
	* Set the type member  
	*/
	public void setType(String t) {
		type = t;
	}
	/**
	* Set the keyData member  
	*/
	public void setKeyData(String k) {
		keyData = k;
	}

	/**
	 *  Get the string encoded version of this object
	 * @since v1.0
	 */
	public String encode() {
		String encoded_string;
		encoded_string = KEY_FIELD + type;
		if (keyData != null) {
			encoded_string += Separators.COLON;
			encoded_string += keyData;
		}
		encoded_string += Separators.NEWLINE;
		return encoded_string;
	}

	/** Returns the name of this attribute
	 * @throws SdpParseException
	 * @return the name of this attribute
	 */
	public String getMethod() throws SdpParseException {
		return this.type;
	}

	/** Sets the id of this attribute.
	 * @param name to set
	 * @throws SdpException if the name is null
	 */
	public void setMethod(String name) throws SdpException {
		this.type = name;
	}

	/** Determines if this attribute has an associated value.
	 * @throws SdpParseException
	 * @return if this attribute has an associated value.
	 */
	public boolean hasKey() throws SdpParseException {
		String key = getKeyData();
		return key != null;
	}

	/** Returns the value of this attribute.
	 * @throws SdpParseException
	 * @return the value of this attribute
	 */
	public String getKey() throws SdpParseException {
		return getKeyData();
	}

	/** Sets the value of this attribute.
	 * @param key to set
	 * @throws SdpException if key is null
	 */
	public void setKey(String key) throws SdpException {
		if (key == null)
			throw new SdpException("The key is null");
		else
			setKeyData(key);
	}
}
/*
 * $Log: KeyField.java,v $
 * Revision 1.3  2004/01/22 13:26:27  sverker
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
