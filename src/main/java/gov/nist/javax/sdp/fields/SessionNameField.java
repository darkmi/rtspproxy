/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD). *
 ******************************************************************************/
package gov.nist.javax.sdp.fields;

import gov.nist.core.Separators;

import javax.sdp.SdpException;
import javax.sdp.SdpParseException;
import javax.sdp.SessionName;

@SuppressWarnings("serial")
public class SessionNameField extends SDPField implements SessionName
{

	protected String sessionName;

	public SessionNameField()
	{
		super( SDPFieldNames.SESSION_NAME_FIELD );
	}

	public String getSessionName()
	{
		return sessionName;
	}

	/**
	 * Set the sessionName member
	 */
	public void setSessionName( String s )
	{
		sessionName = s;
	}

	/**
	 * Returns the value.
	 * 
	 * @throws SdpParseException
	 * @return the value
	 */
	public String getValue() throws SdpParseException
	{
		return getSessionName();
	}

	/**
	 * Sets the value
	 * 
	 * @param value the - new information.
	 * @throws SdpException if the value is null
	 */
	public void setValue( String value ) throws SdpException
	{
		if ( value == null )
			throw new SdpException( "The value is null" );
		else {
			setSessionName( value );
		}
	}

	/**
	 * Get the string encoded version of this object
	 * 
	 * @since v1.0
	 */
	public String encode()
	{
		return SESSION_NAME_FIELD + sessionName + Separators.NEWLINE;
	}

}
/*
 * $Log: SessionNameField.java,v $ Revision 1.2 2004/01/22 13:26:27 sverker
 * Issue number: Obtained from: Submitted by: sverker Reviewed by: mranga Major
 * reformat of code to conform with style guide. Resolved compiler and javadoc
 * warnings. Added CVS tags. CVS:
 * ---------------------------------------------------------------------- CVS:
 * Issue number: CVS: If this change addresses one or more issues, CVS: then
 * enter the issue number(s) here. CVS: Obtained from: CVS: If this change has
 * been taken from another system, CVS: then name the system in this line,
 * otherwise delete it. CVS: Submitted by: CVS: If this code has been
 * contributed to the project by someone else; i.e., CVS: they sent us a patch
 * or a set of diffs, then include their name/email CVS: address here. If this
 * is your work then delete this line. CVS: Reviewed by: CVS: If we are doing
 * pre-commit code reviews and someone else has CVS: reviewed your changes,
 * include their name(s) here. CVS: If you have not had it reviewed then delete
 * this line.
 */
