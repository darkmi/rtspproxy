/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: IpAddressFilter.java 336 2005-12-08 20:48:05Z merlimat $
 * 
 * $URL:
 * http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter
 * /ipaddress/IpAddressFilter.java $
 */

package rtspproxy.filter.ipaddress;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import rtspproxy.Config;
import rtspproxy.Reactor;

/**
 * @author Matteo Merli
 * 
 */
@SuppressWarnings("unused")
public class IpAddressFilter extends IoFilterAdapter {

  private static Logger log = Logger.getLogger(IpAddressFilter.class);

  private IpAddressProvider provider;

  @SuppressWarnings("rawtypes")
  public IpAddressFilter() {
    // Check which backend implementation to use
    // Default is plain-text implementation
    String className =
        Config.get("proxy.filter.ipaddress.implementationClass",
            "rtspproxy.filter.ipaddress.PlainTextIpAddressProvider");

    Class providerClass;
    try {
      providerClass = Class.forName(className);

    } catch (ClassNotFoundException e) {
      log.fatal("Invalid IpAddressProvider class: " + className);
      Reactor.stop();
      return;
    }

    // Check if the class implements the IpAddressProvider interfaces
    boolean found = false;
    for (Class interFace : providerClass.getInterfaces()) {
      if (IpAddressProvider.class.equals(interFace)) {
        found = true;
        break;
      }
    }

    if (!found) {
      log.fatal("Class (" + provider + ") does not implement the IpAddressProvider interface.");
      Reactor.stop();
      return;
    }

    try {
      provider = (IpAddressProvider) providerClass.newInstance();
      provider.init();
    } catch (Exception e) {
      log.fatal("Error starting IpAddressProvider: " + e);
      Reactor.stop();
      return;
    }

    log.info("Using IpAddressFilter (" + className + ")");
  }

  @Override
  public void messageReceived(NextFilter nextFilter, IoSession session, Object message)
      throws Exception {
    if (!provider.isBlocked(((InetSocketAddress) session.getRemoteAddress()).getAddress())) {
      // forward if not blocked
      nextFilter.messageReceived(session, message);
    } else {
      blockSession(session);
    }
  }

  @Override
  public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
    if (!provider.isBlocked(((InetSocketAddress) session.getRemoteAddress()).getAddress())) {
      // forward if not blocked
      nextFilter.sessionCreated(session);
    } else {
      blockSession(session);
    }
  }

  @SuppressWarnings("deprecation")
  protected void blockSession(IoSession session) {
    log.info("Blocked connection from : " + session.getRemoteAddress());
    session.close();
  }
}
