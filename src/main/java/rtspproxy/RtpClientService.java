/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: RtpClientService.java 293 2005-11-24 19:50:47Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/
 * RtpClientService.java $
 */

package rtspproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import rtspproxy.lib.NoPortAvailableException;
import rtspproxy.lib.PortManager;

/**
 * This service is responsible of receiving and sending RTP and RTCP packets to clients.
 * 
 * @author Matteo Merli
 */
public class RtpClientService implements ProxyService {

  private static Logger log = Logger.getLogger(RtpClientService.class);

  static InetSocketAddress rtpAddress = null;
  static InetSocketAddress rtcpAddress = null;

  public void start() throws IOException, NoPortAvailableException {
    int rtpPort = Config.getInt("proxy.client.rtp.port", 8002);
    int rtcpPort = Config.getInt("proxy.client.rtcp.port", 8003);
    String netInterface = Config.get("proxy.client.interface", null);
    boolean dinPorts = Config.getBoolean("proxy.client.dynamicPorts", false);

    // If dinPorts is true, we have to first check the availability
    // of the ports and choose 2 valid ports.
    if (dinPorts) {
      int[] ports = PortManager.findAvailablePorts(2, rtpPort);
      rtpPort = ports[0];
      rtcpPort = ports[1];
    }

    // Update properties with effective ports
    Config.setInt("proxy.client.rtp.port", rtpPort);
    Config.setInt("proxy.client.rtcp.port", rtcpPort);

    rtpAddress = new InetSocketAddress(InetAddress.getByName(netInterface), rtpPort);
    rtcpAddress = new InetSocketAddress(InetAddress.getByName(netInterface), rtcpPort);

    // try {
    // Service rtpService, rtcpService;

    // rtpService = new Service("RtpClientService", TransportType.DATAGRAM, rtpAddress);
    // rtcpService = new Service("RtcpClientService", TransportType.DATAGRAM, rtcpAddress);

    // Reactor.getRegistry().bind(rtpService, new ClientRtpPacketHandler());
    // Reactor.getRegistry().bind(rtcpService, new ClientRtcpPacketHandler());
    // log.info("RtpClientService Started - Listening on: " + InetAddress.getByName(netInterface) +
    // " " + rtpPort
    // + "-" + rtcpPort);

    // } catch (IOException e) {
    // log.fatal("Can't start RtpClientService. " + e);
    // throw e;
    // }
  }

  public void stop() {
    // for (Object service : Reactor.getRegistry().getServices("RtpClientService")) {
    // Reactor.getRegistry().unbind((Service) service);
    // }
    // for (Object service : Reactor.getRegistry().getServices("RtcpClientService")) {
    // Reactor.getRegistry().unbind((Service) service);
    // }

    log.info("RtpClientService Stopped");
  }

  public static IoSession newRtpSession(SocketAddress remoteAddress) {
    // return Reactor.getRegistry().getAcceptor(TransportType.DATAGRAM).newSession(remoteAddress,
    // rtpAddress);
    return null;
  }

  public static IoSession newRtcpSession(SocketAddress remoteAddress) {
    // return Reactor.getRegistry().getAcceptor(TransportType.DATAGRAM).newSession(remoteAddress,
    // rtcpAddress);
    return null;
  }

  public static InetSocketAddress getRtpAddress() {
    return rtpAddress;
  }

  public static InetSocketAddress getRtcpAddress() {
    return rtcpAddress;
  }

  public static InetAddress getHostAddress() {
    /*
     * The InetAddress (IP) is the same for both RTP and RTCP.
     */
    return rtpAddress.getAddress();
  }

  public static int getRtpPort() {
    return rtpAddress.getPort();
  }

  public static int getRtcpPort() {
    return rtcpAddress.getPort();
  }
}
