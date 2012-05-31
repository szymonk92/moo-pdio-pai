package pai.androidapp;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParserException;

import wsdldom.SoapOperation;
import wsdldom.WSDLDocument;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PAI_AndroidAppActivity extends Activity {

	private static String SOAP_ACTION = "http://lukaszm.servehttp.com/GetBalance";

	private static String NAMESPACE = "http://lukaszm.servehttp.com/";
	private static String METHOD_NAME = "GetBalance";

	private static String URL = "http://lukaszm.servehttp.com/";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b = (Button) findViewById(R.id.TrySOAP);
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				WSDLDocument w = new WSDLDocument("http://lukaszm.servehttp.com/MainWebService.asmx?WSDL");
				w.setTargetNamespace(NAMESPACE);
				w.loadWSDL("GetBalance");
				SoapOperation so= w.getOperation("GetBalance");
				w.setElemenTextContext(so.getRequestHeader(), 0, "1989-11-13");
				w.setElemenTextContext(so.getRequestHeader(),1 , "2100-11-13");
				w.setElemenTextContext(so.getRequestHeader(), 2, "6c23f312cce35c1ca1264b57ffc5b0d4");
				w.setElemenTextContext(so.getRequestBody(), 0, "gentoo");
				
				TextView tv = (TextView) findViewById(R.id.response);
				
		        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		        
		        request.addProperty(((Element)so.getRequestBody().getChild(0)).getName(), "gentoo");
		        
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.dotNet = true;
		        
		        envelope.setOutputSoapObject(request);
//		        envelope.bodyOut = so.getRequestBody();
		        envelope.headerOut = new Element[1];
		        envelope.headerOut[0]=so.getRequestHeader();
		        
		        
		        
		        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		        

		        try {
		        	androidHttpTransport.call(so.getSopaAction(), envelope);

		        	 SoapObject result = (SoapObject) envelope.bodyIn;
		        	 String response = new String();
		        	 for( int i=0; i<so.getResponseBody().getChildCount(); ++i) {
		        		 Element ch = (Element) so.getResponseBody().getChild(i);
		        		 response+= result.getPropertyAsString(ch.getName())+"\n";
		        	 }

		        	tv.setText( ""+response);

		        
		        } catch ( IOException err) {
		        	err.printStackTrace();
		        } catch (XmlPullParserException err2) {
					err2.printStackTrace();
				}
				
			}
			
		});

	}


}