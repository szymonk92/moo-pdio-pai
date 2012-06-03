package pai.androidapp;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import wsdldom.SoapOperation;
import wsdldom.WSDLDocument;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends Activity {

	StartActivity mThis;
	EditText logname;
	EditText logpass;
	Button chequeSwitch, paymentSwitch;
	Button logIn, logOut;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		mThis = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		logIn = (Button) findViewById(R.id.actButtonLogIn);
		logOut = (Button) findViewById(R.id.actButtonLogOut);
		logname  = (EditText) findViewById(R.id.login);
		logpass  = (EditText) findViewById(R.id.pass);
		
		logOut.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
//				SoapOperation.op(AppGlobalVariables.getInstance().wsdl,
//						AppGlobalVariables.getInstance().authHeader,
//						opName,
//						opInArgs,
//						opOutArgs,
//						null);
			}
		});
		
		
		logIn.setOnClickListener(new OnClickListener() {
			
			String NAMESPACE = AppGlobalVariables.getInstance().NAMESPACE;
			

			public void onClick(View arg0) {
				WSDLDocument w = new WSDLDocument("http://lukaszm.servehttp.com/MainWebService.asmx?WSDL");
				w.setTargetNamespace(NAMESPACE);
//				w.loadWSDL("");
//				Element authHeader= openSession(w,  "bolek@gmail.com", "Haslobolka_2");
				Element authHeader= openSession(w, logname.getText().toString() , logpass.getText().toString());
				
				AppGlobalVariables.getInstance().setAuthHeader(authHeader);
				AppGlobalVariables.getInstance().setWsdl(w);
				
			}	
			
		});
		
		chequeSwitch = (Button) findViewById(R.id.switchCheque);
		chequeSwitch.setEnabled(false);
		chequeSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startActivity(new Intent(mThis, ChequeActivity.class));
               
            }
        });
		
		paymentSwitch = (Button) findViewById(R.id.switchPayment);
		paymentSwitch.setEnabled(false);
		paymentSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startActivity(new Intent(mThis, PaymentActivity.class));
            }
        });
		
	}
		
	
	private Element openSession(WSDLDocument w, String login, String pass) {
		Element header = null;
		if (login.equals("") && pass.equals("")) {
			login = "bolek@gmail.com";
			pass = "Haslobolka_2"; 
		}
		w.loadWSDL("Authenticate");
		SoapOperation auth= w.getOperation("Authenticate");
		w.setElemenTextContext(auth.getRequestBody(), "login", login);
		w.setElemenTextContext(auth.getRequestBody(), "password", pass);
		
		TextView tv = (TextView) findViewById(R.id.response);
		
		final String NAMESPACE = AppGlobalVariables.getInstance().NAMESPACE;
		final String URL = AppGlobalVariables.getInstance().URL;
		
        SoapObject request = new SoapObject(NAMESPACE, "Authenticate");
        request = SoapOperation.convertBody2SoapObject(auth, NAMESPACE);
        
//        System.out.println(request.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        
        envelope.setOutputSoapObject(request); 
        
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        

        try {
        	String response = new String();
        	
        	androidHttpTransport.call(auth.getSopaAction(), envelope);
        	
        	if (envelope.bodyIn instanceof SoapFault)
        	{
        	
				SoapFault result = (SoapFault) envelope.bodyIn;
				response+=result.getMessage();
			}
        	else
        	{
        	 Element[] result =  envelope.headerIn;
        	 final StringBuilder build= new StringBuilder();
        	 auth.setResponseHeader(result[0]);
        	 WSDLDocument.printDom(result[0], 0,build);
        	 response =build.toString(); 
			}
        	
        	tv.setText( ""+response);
        	
        	
        	//if all was ok - save username to application globals
        	AppGlobalVariables.getInstance().setUsername(login);
        	
        	//enable Cheque, PAymeny
        	chequeSwitch.setEnabled(true);
        	paymentSwitch.setEnabled(true);
        	//disable login
        	logIn.setEnabled(false);
        	logname.setEnabled(false);
        	logpass.setEnabled(false);
        	logOut.setEnabled(true);
        	
        	
        
        } catch ( IOException err) {
        	err.printStackTrace();
        } catch (XmlPullParserException err2) {
			err2.printStackTrace();
		}
		
		header = auth.getResponseHeader();
		
		return header;
	}


}