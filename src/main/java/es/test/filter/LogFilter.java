package es.test.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.TeeOutputStream;

/**
 * Only for debugging.
 * https://stackoverflow.com/a/28298948
 */
//@WebFilter("/rest/*")
public class LogFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			Map<String, String> requestMap = getTypesafeRequestMap(httpServletRequest);
			BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);
			BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);

			chain.doFilter(bufferedReqest, bufferedResponse);

			if (bufferedResponse.getContent() != null) { // Response
				final StringBuilder resMessage = new StringBuilder();
				resMessage.append(" [RESPONSE:").append(bufferedResponse.getContent()).append("]");
				System.out.println(resMessage);
			} else { // Request
				final StringBuilder reqMessage = new StringBuilder("REST Request - ").append("[HTTP METHOD:")
						.append(httpServletRequest.getMethod()).append("] [PATH INFO:")
						.append(httpServletRequest.getPathInfo()).append("] [REQUEST PARAMETERS:").append(requestMap)
						.append("] [REQUEST BODY:").append(bufferedReqest.getRequestBody()).append("] [REMOTE ADDRESS:")
						.append(httpServletRequest.getRemoteAddr()).append("]");
				System.out.println(reqMessage);
			}

		} catch (Throwable a) {
			System.out.println(a);
		}
	}

	@Override
	public void destroy() {
	}

	private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String) requestParamNames.nextElement();
			String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}

	private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

		private ByteArrayInputStream bais = null;
		private ByteArrayOutputStream baos = null;
		private BufferedServletInputStream bsis = null;
		private byte[] buffer = null;

		public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
			super(req);
			// Read InputStream and store its content in a buffer.
			InputStream is = req.getInputStream();
			baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int letti;
			while ((letti = is.read(buf)) > 0) {
				baos.write(buf, 0, letti);
			}
			buffer = baos.toByteArray();
		}

		@Override
		public ServletInputStream getInputStream() {
			bais = new ByteArrayInputStream(buffer);
			bsis = new BufferedServletInputStream(bais);

			return bsis;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		String getRequestBody() throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
			String line = null;
			StringBuilder inputBuffer = new StringBuilder();
			do {
				line = reader.readLine();
				if (null != line) {
					inputBuffer.append(line.trim());
				}
			} while (line != null);
			reader.close();
			return inputBuffer.toString().trim();
		}

	}

	private static final class BufferedServletInputStream extends ServletInputStream {

		private ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		@Override
		public int available() {
			return bais.available();
		}

		@Override
		public int read() {
			return bais.read();
		}

		@Override
		public int read(byte[] buf, int off, int len) {
			return bais.read(buf, off, len);
		}

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener arg0) {
		}
	}

	private static class TeeServletOutputStream extends ServletOutputStream {

		private final TeeOutputStream targetStream;

		public TeeServletOutputStream(OutputStream one, OutputStream two) {
			targetStream = new TeeOutputStream(one, two);
		}

		@Override
		public void write(int arg0) throws IOException {
			targetStream.write(arg0);
		}

		public void flush() throws IOException {
			super.flush();
			targetStream.flush();
		}

		public void close() throws IOException {
			super.close();
			targetStream.close();
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
		}
	}

	private static class BufferedResponseWrapper implements HttpServletResponse {

		HttpServletResponse original;
		TeeServletOutputStream teeStream;
		PrintWriter teeWriter;
		ByteArrayOutputStream bos;

		public BufferedResponseWrapper(HttpServletResponse response) {
			original = response;
		}

		public String getContent() {
			if (bos == null) {
				return null;
			}
			return bos.toString();
		}

		@Override
		public PrintWriter getWriter() throws IOException {

			if (teeWriter == null) {
				teeWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
			}
			return teeWriter;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {

			if (teeStream == null) {
				bos = new ByteArrayOutputStream();
				teeStream = new TeeServletOutputStream(original.getOutputStream(), bos);
			}
			return teeStream;
		}

		@Override
		public String getCharacterEncoding() {
			return original.getCharacterEncoding();
		}

		@Override
		public String getContentType() {
			return original.getContentType();
		}

		@Override
		public void setCharacterEncoding(String charset) {
			original.setCharacterEncoding(charset);
		}

		@Override
		public void setContentLength(int len) {
			original.setContentLength(len);
		}

		@Override
		public void setContentType(String type) {
			original.setContentType(type);
		}

		@Override
		public void setBufferSize(int size) {
			original.setBufferSize(size);
		}

		@Override
		public int getBufferSize() {
			return original.getBufferSize();
		}

		@Override
		public void flushBuffer() throws IOException {
			if (teeStream != null) {
				teeStream.flush();
			}
			if (teeWriter != null) {
				teeWriter.flush();
			}
		}

		@Override
		public void resetBuffer() {
			original.resetBuffer();
		}

		@Override
		public boolean isCommitted() {
			return original.isCommitted();
		}

		@Override
		public void reset() {
			original.reset();
		}

		@Override
		public void setLocale(Locale loc) {
			original.setLocale(loc);
		}

		@Override
		public Locale getLocale() {
			return original.getLocale();
		}

		@Override
		public void addCookie(Cookie cookie) {
			original.addCookie(cookie);
		}

		@Override
		public boolean containsHeader(String name) {
			return original.containsHeader(name);
		}

		@Override
		public String encodeURL(String url) {
			return original.encodeURL(url);
		}

		@Override
		public String encodeRedirectURL(String url) {
			return original.encodeRedirectURL(url);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String encodeUrl(String url) {
			return original.encodeUrl(url);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String encodeRedirectUrl(String url) {
			return original.encodeRedirectUrl(url);
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			original.sendError(sc, msg);
		}

		@Override
		public void sendError(int sc) throws IOException {
			original.sendError(sc);
		}

		@Override
		public void sendRedirect(String location) throws IOException {
			original.sendRedirect(location);
		}

		@Override
		public void setDateHeader(String name, long date) {
			original.setDateHeader(name, date);
		}

		@Override
		public void addDateHeader(String name, long date) {
			original.addDateHeader(name, date);
		}

		@Override
		public void setHeader(String name, String value) {
			original.setHeader(name, value);
		}

		@Override
		public void addHeader(String name, String value) {
			original.addHeader(name, value);
		}

		@Override
		public void setIntHeader(String name, int value) {
			original.setIntHeader(name, value);
		}

		@Override
		public void addIntHeader(String name, int value) {
			original.addIntHeader(name, value);
		}

		@Override
		public void setStatus(int sc) {
			original.setStatus(sc);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void setStatus(int sc, String sm) {
			original.setStatus(sc, sm);
		}

		@Override
		public void setContentLengthLong(long arg0) {
			original.setContentLengthLong(arg0);

		}

		@Override
		public String getHeader(String arg0) {
			return original.getHeader(arg0);
		}

		@Override
		public Collection<String> getHeaderNames() {
			return original.getHeaderNames();
		}

		@Override
		public Collection<String> getHeaders(String arg0) {
			return original.getHeaders(arg0);
		}

		@Override
		public int getStatus() {
			return original.getStatus();
		}
	}
}