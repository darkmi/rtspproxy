/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/
package gov.nist.javax.sdp.fields;
import gov.nist.core.*;
import javax.sdp.*;

/** Proto version field of SDP announce.
*
*@version  JSR141-PUBLIC-REVIEW (subject to change).
*
*@author Olivier Deruelle <deruelle@antd.nist.gov>
*@author M. Ranganathan <mranga@nist.gov>  <br/>
*
*
*<a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
*
*/
@SuppressWarnings("serial")
public class ProtoVersionField extends SDPField implements javax.sdp.Version {
	protected int protoVersion;

	public ProtoVersionField() {
		super(PROTO_VERSION_FIELD);
	}

	public int getProtoVersion() {
		return protoVersion;
	}

	/**
	* Set the protoVersion member  
	*/
	public void setProtoVersion(int pv) {
		protoVersion = pv;
	}

	/** Returns the version number.
	 * @throws SdpParseException
	 * @return int
	 */
	public int getVersion() throws SdpParseException {
		return getProtoVersion();
	}

	/** Sets the version.
	 * @param value the - new version value.
	 * @throws SdpException if the value is <=0
	 */
	public void setVersion(int value) throws SdpException {
		if (value < 0)
			throw new SdpException("The value is <0");
		else
			setProtoVersion(value);
	}

	/**
	 *  Get the string encoded version of this object
	 * @since v1.0
	 */
	public String encode() {
		return PROTO_VERSION_FIELD + protoVersion + Separators.NEWLINE;
	}

}
/*
 * $Log: ProtoVersionField.java,v $
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
