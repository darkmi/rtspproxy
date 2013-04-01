/**
 * 
 */
package rtspproxy.filter.rewrite;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import rtspproxy.rtsp.RtspMessage;
import rtspproxy.rtsp.RtspRequest;
import rtspproxy.rtsp.RtspResponse;

/**
 * @author bieniekr
 * 
 */
@SuppressWarnings("unused")
public class RequestUrlRewritingImpl extends IoFilterAdapter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(RequestUrlRewritingImpl.class);

	// the filter instance
	private RequestUrlRewritingFilter filter;

	/**
	 * construct the IoFilter around the filter class denoted by the clazz name
	 * parameter.
	 * 
	 * TODO: This may become obsolete if moving to OSGi bundles TODO: Make
	 * filter parametrizeable. Could be done by moving from properties to XML
	 * config file.
	 */
	@SuppressWarnings("rawtypes")
	public RequestUrlRewritingImpl(String clazzName)
			throws Exception {

		try {
			Class filterClazz = Class.forName(clazzName);

			this.filter = (RequestUrlRewritingFilter) filterClazz.newInstance();
			logger.info("using request URL rewriter " + clazzName);
		} catch (Exception e) {
			logger.error(e);

			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.common.IoFilterAdapter#messageReceived(org.apache.mina.common.IoFilter.NextFilter,
	 *      org.apache.mina.common.IoSession, java.lang.Object)
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		RtspMessage rtspMessage = (RtspMessage) message;

		logger.debug("Received (pre-rewriting) message:\n" + message);
		if (rtspMessage.getType() == RtspMessage.Type.TypeRequest) {
			RtspRequest request = (RtspRequest) rtspMessage;
			URL rewritten = this.filter.rewriteRequestUrl(request.getUrl());

			if (rewritten != null) {
				logger.debug("changed request URL from '" + request.getUrl()
						+ "' to '" + rewritten + "'");

				request.setUrl(rewritten);
			}
		} else if (rtspMessage.getType() == RtspMessage.Type.TypeResponse) {
			RtspResponse resp = (RtspResponse) rtspMessage;

			switch (resp.getRequestVerb()) {
			case DESCRIBE:
				rewriteUrlHeader("Content-base", resp);
				break;
			case PLAY:
				// rewriteUrlHeader("RTP-Info", resp);
				break;
			}
		}
		logger.debug("Sent (post-rewriting) message:\n" + message);

		nextFilter.messageReceived(session, message);
	}

	/**
	 * rewrite a header
	 */
	private void rewriteUrlHeader(String headerName, RtspResponse resp) {
		String oldHeader = resp.getHeader(headerName);

		if (oldHeader != null) {
			logger.debug("old content " + headerName + " header value: "
					+ oldHeader);

			try {
				URL header = this.filter.rewriteResponseHeaderUrl(new URL(
						oldHeader));

				if (header != null) {
					logger.debug("changed header " + headerName + " to "
							+ header);

					resp.setHeader(headerName, header.toString());
				}
			} catch (MalformedURLException mue) {
				logger.error("failed to parse " + headerName + " header", mue);
			}
		}

	}
}
