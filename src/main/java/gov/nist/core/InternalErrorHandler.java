/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/
package gov.nist.core;
/**
*  Handle Internal error failures and print a stack trace (for debugging).
*
*@version  JAIN-SIP-1.1
*
*@author M. Ranganathan <mranga@nist.gov>  <br/>
*
*<a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
*
*/
public class InternalErrorHandler {
	/**
	* Handle an unexpected exception.
	*/
	public static void handleException(Exception ex) {
		try {
			throw ex;
		} catch (Exception e) {
			System.err.println("Unexpected exception : " + e);
			System.err.println("Error message is " + ex.getMessage());
			System.err.println("*************Stack Trace ************");
			e.printStackTrace(System.err);
			System.exit(0);
		}
	}
	/**
	* Handle an unexpected condition (and print the error code).
	*/

	public static void handleException(String emsg) {
		try {
			throw new Exception(emsg);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
}
/*
 * $Log: InternalErrorHandler.java,v $
 * Revision 1.4  2004/01/22 13:26:27  sverker
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
