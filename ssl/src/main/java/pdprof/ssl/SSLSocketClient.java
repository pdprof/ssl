package pdprof.ssl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SSLSocketClient
 */
@WebServlet("/socket")
public class SSLSocketClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static SSLSocketFactory wasDefaultSF = null;
    private static SSLSocketFactory pdprofSF = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSLSocketClient() {
        super();
    	wasDefaultSF = HttpsURLConnection.getDefaultSSLSocketFactory();
    	try {
			pdprofSF =  createSslSocketFactory();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String host = request.getParameter("host");
		if (host == null)
			host = "localhost";
		String portStr = request.getParameter("port");
		int port = 9443;
		if (portStr != null) {
			try {
				port = Integer.parseInt(portStr);
			} catch (NumberFormatException e) {}
		}
		request.setAttribute("host", host);
		request.setAttribute("port", new Integer(port));

		TreeMap <String, Boolean> map = new TreeMap<String, Boolean>(); 
		try {
			for(String cipher : pdprofSF.getSupportedCipherSuites()) {
				SSLSocket socket = (SSLSocket) pdprofSF.createSocket(host, port);
				String[] ciphers = { cipher };
				socket.setEnabledCipherSuites(ciphers);
				try {
					socket.startHandshake();
					if(socket.isConnected())
						map.put(cipher, Boolean.TRUE);
					else 
						map.put(cipher, Boolean.FALSE);
				} catch (SSLException e) {
					map.put(cipher, Boolean.FALSE);
				}
				socket.close();
			}
			for(String key: map.keySet()) {
				System.out.println(key + ":" + map.get(key));
			}
		} catch (Exception e) {
			map.put(e.getClass().getCanonicalName() + ":" + e.getMessage(), Boolean.FALSE);
		}
		request.setAttribute("ciphers", map);
		
		request.getRequestDispatcher("ciphers.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
    private static SSLSocketFactory createSslSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustManagers = { (TrustManager)(new PdprofTrustManager()) };
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustManagers, null);
        return sslContext.getSocketFactory();
    }

}
