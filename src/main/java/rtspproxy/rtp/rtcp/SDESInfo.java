/***************************************************************************
 * * This program is free software; you can redistribute it and/or modify * it under the terms of
 * the GNU General Public License as published by * the Free Software Foundation; either version 2
 * of the License, or * (at your option) any later version. * * Copyright (C) 2005 - Matteo Merli -
 * matteo.merli@gmail.com * *
 ***************************************************************************/

package rtspproxy.rtp.rtcp;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * @author mat
 * 
 */
public class SDESInfo implements RtcpInfo {
  static Logger log = Logger.getLogger(SDESInfo.class);

  public enum Type {
    END(0), CNAME(1), NAME(2), EMAIL(3), PHONE(4), LOC(5), TOOL(6), NOTE(7), PRIV(8);

    public final byte value;

    public static Type fromByte(byte value) {
      for (Type t : Type.values())
        if (t.value == value) return t;
      return END;
    }

    private Type(int value) {
      this.value = (byte) value;
    }
  }

  private class Chunk {

    @SuppressWarnings("unused")
    public int ssrc;
    public Type type;
    @SuppressWarnings("unused")
    public byte[] value;
  }

  private Chunk[] chunkList;

  @SuppressWarnings("incomplete-switch")
  public SDESInfo(RtcpPacket packet, ByteBuffer buffer) {
    // int totalBytesToRead = packet.length * 4;
    byte sourceCount = packet.count;

    chunkList = new Chunk[sourceCount];

    for (byte i = 0; i < sourceCount; i++) {
      chunkList[i] = new Chunk();
      Chunk c = chunkList[i];

      c.ssrc = buffer.getInt();
      c.type = Type.fromByte(buffer.get());

      switch (c.type) {
        case PRIV:
          log.debug("Chunk private...");
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see rtspproxy.rtp.rtcp.RtcpInfo#toBuffer()
   */
  public ByteBuffer toBuffer() {
    // TODO Auto-generated method stub
    return null;
  }

}
