/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   Copyright (C) 2005 - Matteo Merli - matteo.merli@gmail.com            *
 *                                                                         *
 ***************************************************************************/

/*
 * $Id: RtspFilters.java 336 2005-12-08 20:48:05Z merlimat $
 * 
 * $URL: http://svn.berlios.de/svnroot/repos/rtspproxy/tags/3.0-ALPHA2/src/main/java/rtspproxy/filter/RtspFilters.java $
 * 
 */

package rtspproxy.filter;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import rtspproxy.Config;
import rtspproxy.Reactor;
import rtspproxy.filter.authentication.AuthenticationFilter;
import rtspproxy.filter.ipaddress.IpAddressFilter;
import rtspproxy.filter.rewrite.RequestUrlRewritingImpl;
import rtspproxy.rtsp.RtspDecoder;
import rtspproxy.rtsp.RtspEncoder;

/**
 * Base class for filter chains based on configuration settings.
 * 
 * @author Matteo Merli
 */
public abstract class RtspFilters implements IoFilterChainBuilder {

	private static ProtocolCodecFactory codecFactory = new ProtocolCodecFactory() {

		// Decoders can be shared
		private ProtocolEncoder rtspEncoder = new RtspEncoder();
		private ProtocolDecoder rtspDecoder = new RtspDecoder();


		public ProtocolEncoder getEncoder(IoSession session) throws Exception {
			return rtspEncoder;
		}

		public ProtocolDecoder getDecoder(IoSession session) throws Exception {
			return rtspDecoder;
		}
	};

	private static IoFilter codecFilter = new ProtocolCodecFilter(codecFactory);

	// These filters are instanciated only one time, when requested
	private static IpAddressFilter ipAddressFilter = null;
	private static AuthenticationFilter authenticationFilter = null;

	/**
	 * IP Address filter.
	 * <p>
	 * This needs to be the first filter in the chain to block blacklisted host
	 * in the early stage of the connection, preventing network and computation
	 * load from unwanted hosts.
	 */
	protected void addIpAddressFilter(IoFilterChain chain) {
		boolean enableIpAddressFilter = Config.getBoolean("proxy.filter.ipaddress.enable", false);

		if (enableIpAddressFilter) {
			if (ipAddressFilter == null)
				ipAddressFilter = new IpAddressFilter();
			chain.addLast("ipAddressFilter", ipAddressFilter);
		}
	}

	/**
	 * The RTSP codec filter is always present. Translates the incoming streams
	 * into RTSP messages.
	 */
	protected void addRtspCodecFilter(IoFilterChain chain) {
		chain.addLast("codec", codecFilter);
	}

	/**
	 * Authentication filter.
	 */
	protected void addAuthenticationFilter(IoFilterChain chain) {
		boolean enableAuthenticationFilter = Config.getBoolean("proxy.filter.authentication.enable", false);

		if (enableAuthenticationFilter) {
			if (authenticationFilter == null)
				authenticationFilter = new AuthenticationFilter();
			chain.addLast("authentication", authenticationFilter);
		}
	}

	protected void addRewriteFilter(IoFilterChain chain) {
		String rewritingFilter = Config.get("filter.requestUrlRewriting.implementationClass", null);

		try {
			if (rewritingFilter != null)
				chain.addLast("requestUrlRewriting", new RequestUrlRewritingImpl(rewritingFilter));
		} catch (Exception e) {
			// already logged
			Reactor.stop();
		}
	}
}
