/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

/*
 * $Id: ServerRtpPacketHandler.java 309 2005-12-01 22:36:28Z merlimat $
 * 
 * $URL:
 * http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/proxy/
 * ServerRtpPacketHandler.java $
 */

package rtspproxy.proxy;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import rtspproxy.lib.Exceptions;
import rtspproxy.rtp.RtpPacket;

/**
 * @author Matteo Merli
 */
public class ServerRtpPacketHandler extends IoHandlerAdapter {

  private static Logger log = Logger.getLogger(ServerRtpPacketHandler.class);

  /*
   * @see org.apache.mina.io.IoHandlerAdapter#dataRead(org.apache.mina.io.IoSession,
   * org.apache.mina.common.ByteBuffer)
   */
  @Override
  public void messageReceived(IoSession session, Object buffer) throws Exception {
    // log.debug( "Received RTP packet" );
    RtpPacket packet = new RtpPacket((IoBuffer) buffer);
    Track track = Track.getByServerSSRC(packet.getSsrc());

    if (track == null) {
      track = Track.getByServerAddress((InetSocketAddress) session.getRemoteAddress());

      if (track == null) {
        // drop packet
        log.debug("Invalid SSRC identifier: " + packet.getSsrc().toHexString());
        return;
      } else {
        // hot-wire the ssrc into the track
        log.debug("Adding SSRC identifier: " + packet.getSsrc().toHexString());
        track.setServerSSRC(packet.getSsrc());
      }

    }

    track.setRtpServerSession(session);
    track.forwardRtpToClient(packet);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.mina.io.IoHandlerAdapter#exceptionCaught(org.apache.mina.io.IoSession,
   * java.lang.Throwable)
   */
  @SuppressWarnings("deprecation")
  @Override
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    log.debug("Exception: " + cause);
    Exceptions.logStackTrace(cause);
    session.close();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.mina.io.IoHandlerAdapter#sessionCreated(org.apache.mina.io.IoSession)
   */
  @Override
  public void sessionCreated(IoSession session) throws Exception {}

}
