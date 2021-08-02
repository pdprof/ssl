package pdprof.ssl.standalone;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import pdprof.ssl.PdprofTrustManager;

public class SSLSocketClient
{
    public static void main( String[] args )
    {
    	if (args.length < 2)  {
    		printUsage();
    		System.exit(1);
    	}
    	String host = args[0];
    	
    	int port = 0;
    	try {
    		port = Integer.parseInt(args[1]);
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		printUsage();
    		System.exit(1);
    	}
    	
    	String protocol = "TLSv1.2";
    	if(args.length > 2) {
    		protocol = args[2];
    	}
    	
		TreeMap <String, Boolean> map = new TreeMap<String, Boolean>(); 
		try {
			SSLSocketFactory pdprofSF = createSslSocketFactory(protocol);
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
		} catch (Exception e) {
			map.put(e.getClass().getCanonicalName() + ":" + e.getMessage(), Boolean.FALSE);
		}
		
		for(String key: map.keySet()) {
			System.out.println(key + ":" + map.get(key));
		}
    }
    
    private static SSLSocketFactory createSslSocketFactory(String protocol) throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustManagers = { (TrustManager)(new PdprofTrustManager()) };
        SSLContext sslContext = SSLContext.getInstance(protocol);
        sslContext.init(null, trustManagers, null);
        return sslContext.getSocketFactory();
    }
    
    private static void printUsage() {
    	System.out.println ("Usage : java pdprof.ssl.standalone.SSLSocketClient <host_name> <port_number> [ssl_protocol]");
    }

}
