package pdprof.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HttpsClient
 */
@WebServlet("/httpsclient")
public class HttpsClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static SSLSocketFactory wasDefaultSF = null;
    private static SSLSocketFactory pdprofSF = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpsClient() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
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
		
		/*
		 * Set customer ssl socket factory
		 */
		String customStr = request.getParameter("custom");
		boolean custom = false;
		if (customStr != null) {
			if (customStr.equalsIgnoreCase("true")) 
				custom = true;
		}
		URL url = new URL("https://" + host + ":" + request.getServerPort());
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		if(custom == true) {
			con.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
            });
			con.setSSLSocketFactory(pdprofSF);
		}
		
		/*
		 * Connect to host read from host and write to client
		 */
        con.connect();
        int rc = con.getResponseCode();
        System.out.println("ResponseCode : " + rc);
        InputStream in = null;
        try {
            in = con.getInputStream();
        } catch (IOException e) {
            in = con.getErrorStream();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        response.setContentType("text/html");        
		PrintWriter out = response.getWriter();
        String body = "";
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            body = body + line;
            //System.out.println(line);
            out.println(line);
        }
        
        con.disconnect();
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
    private static SSLSocketFactory createSslSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustManagers = { (TrustManager)(new PdprofTrustManager()) };
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustManagers, null);
        return sslContext.getSocketFactory();
    }

}
