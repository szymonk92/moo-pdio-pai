package pai.androidapp;

import org.kxml2.kdom.Element;

import wsdldom.WSDLDocument;
import android.app.Application;

public class AppGlobalVariables extends Application {
	
	private static AppGlobalVariables instance;
	
	public static AppGlobalVariables getInstance() {
		if ( instance == null)
			instance = new AppGlobalVariables();
		return instance;
	}
	
	WSDLDocument wsdl;
	Element authHeader;
	String username; 
	
	public static final String NAMESPACE = "http://lukaszm.servehttp.com/";
	

	//change http to https for ssl connection
	public static final String URL = "http://lukaszm.servehttp.com/";
	
	
	public WSDLDocument getWsdl() {
		return wsdl;
	}
	
	public void setWsdl(WSDLDocument wsdl) {
		this.wsdl = wsdl;
	}
	
	public Element getAuthHeader() {
		return authHeader;
	}
	
	public void setAuthHeader(Element authHeader) {
		this.authHeader = authHeader;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
