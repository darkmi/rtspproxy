/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: AuthenticationFilter.java 336 2005-12-08 20:48:05Z merlimat $
 * 
 * $URL:
 * http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter
 * /authentication/AuthenticationFilter.java $
 */

package rtspproxy.filter.authentication;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import rtspproxy.Config;
import rtspproxy.Reactor;
import rtspproxy.filter.authentication.scheme.AuthenticationScheme;
import rtspproxy.filter.authentication.scheme.BasicAuthentication;
import rtspproxy.filter.authentication.scheme.Credentials;
import rtspproxy.rtsp.RtspCode;
import rtspproxy.rtsp.RtspMessage;
import rtspproxy.rtsp.RtspRequest;
import rtspproxy.rtsp.RtspResponse;

/**
 * @author Matteo Merli
 */
@SuppressWarnings("unused")
public class AuthenticationFilter extends IoFilterAdapter {

  private static Logger log = Logger.getLogger(AuthenticationFilter.class);

  private static final String ATTR = AuthenticationFilter.class.toString() + "Attr";

  /** Different authentication schemes implementation */
  private static AuthenticationScheme[] schemes = {new BasicAuthentication()};

  /** Contains a comma-separated list of the scheme names. */
  private static String schemesString;

  static {
    // Pre-fill the scheme names
    schemesString = new String();
    for (int i = 0; i < schemes.length; i++) {
      schemesString += schemes[i].getName();
      if (i < schemes.length - 1) schemesString += ", ";
    }
  }

  /**
   * Backend provider.
   */
  private AuthenticationProvider provider;

  private String realm;

  /**
   * Construct a new AuthenticationFilter. Looks at the configuration to load the choseen backend
   * implementation.
   */
  @SuppressWarnings("rawtypes")
  public AuthenticationFilter() {
    // Check which backend implementation to use
    // Default is plain-text implementation
    String className =
        Config.get("proxy.filter.authentication.implementationClass",
            "rtspproxy.filter.authentication.PlainTextAuthenticationProvider");

    Class providerClass;
    try {
      providerClass = Class.forName(className);

    } catch (ClassNotFoundException e) {
      log.fatal("Invalid AuthenticationProvider class: " + className);
      Reactor.stop();
      return;
    }

    // Check if the class implements the IpAddressProvider interfaces
    boolean found = false;
    for (Class interFace : providerClass.getInterfaces()) {
      if (AuthenticationProvider.class.equals(interFace)) {
        found = true;
        break;
      }
    }

    if (!found) {
      log.fatal("Class (" + providerClass
          + ") does not implement the AuthenticationProvider interface.");
      Reactor.stop();
      return;
    }

    try {
      provider = (AuthenticationProvider) providerClass.newInstance();
      provider.init();

    } catch (Exception e) {
      log.fatal("Error starting AuthenticationProvider: " + e);
      Reactor.stop();
      return;
    }

    realm = "RtspProxy " + Config.get("proxy.rtsp.interface", "");

    log.info("Using AuthenticationFilter (" + className + ")");
  }

  @SuppressWarnings("deprecation")
  public void messageReceived(NextFilter nextFilter, IoSession session, Object message)
      throws Exception {
    if (!(message instanceof RtspRequest)) {
      // Shouldn't happen
      log.warn("Object message is not a RTSP message");
      return;
    }

    if (session.getAttribute(ATTR) != null) {
      // Client already autheticated
      log.debug("Already authenticaed: " + session.getAttribute(ATTR));
      nextFilter.messageReceived(session, message);
    }

    String authString = ((RtspMessage) message).getHeader("Proxy-Authorization");
    if (authString == null) {
      log.debug("RTSP message: \n" + message);
      RtspResponse response = RtspResponse.errorResponse(RtspCode.ProxyAuthenticationRequired);
      response.setHeader("Proxy-Authenticate", schemesString + " realm=\"" + realm + "\"");

      // TODO: I should be able to send a RtspMessage here using the
      // already provided encoder.
      WriteFuture written = session.write(response);
      // Why have I to wait here????
      written.join();
      // session.close();
      return;
    }

    AuthenticationScheme scheme = getAuthenticationScheme(authString);
    if (scheme == null) {
      RtspResponse response = RtspResponse.errorResponse(RtspCode.BadRequest);

      // TODO: I should be able to send a RtspMessage here using the
      // already provided encoder.
      WriteFuture written = session.write(response);
      // Why have I to wait here????
      written.join();
      // session.close();
      return;
    }

    // Check the authentication credentials
    Credentials credentials = scheme.getCredentials(authString);
    if (credentials == null || provider.isAuthenticated(credentials) == false) {
      RtspResponse response = RtspResponse.errorResponse(RtspCode.Unauthorized);

      // TODO: I should be able to send a RtspMessage here using the
      // already provided encoder.
      WriteFuture written = session.write(ByteBuffer.wrap(response.toString().getBytes()));
      // Why have I to wait here????
      written.join();
      session.close();
      return;
    }

    /*
     * Mark the session with an "authenticated" attribute. This will prevent the check for the
     * credentials for every message received.
     */
    session.setAttribute(ATTR, credentials.getUserName());

    // Forward message
    nextFilter.messageReceived(session, message);
  }

  /**
   * Gets the authentication scheme stated by the client.
   * 
   * @param authString
   * @return
   */
  private static AuthenticationScheme getAuthenticationScheme(String authString) {
    String schemeName;
    try {
      schemeName = authString.split(" ")[0];
    } catch (IndexOutOfBoundsException e) {
      // Malformed auth string
      return null;
    }

    for (int i = 0; i < schemes.length; i++) {
      if (schemeName.equalsIgnoreCase(schemes[i].getName())) return schemes[i];
    }

    // Scheme not valid
    return null;
  }

}
