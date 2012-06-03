package wsdldom;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import pai.androidapp.AppGlobalVariables;
import android.widget.TextView;

public class SoapOperation {

	Element requestBody, responseBody;
	Element requestHeader, responseHeader;
	String sopaAction;

	public SoapOperation(Element requestBody, Element responseBody,
			Element requestHeader, Element responseHeader, String sopaAction) {
		super();
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.requestHeader = requestHeader;
		this.responseHeader = responseHeader;
		this.sopaAction = sopaAction;
	}

	public Element getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Element requestBody) {
		this.requestBody = requestBody;
	}

	public Element getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Element responseBody) {
		this.responseBody = responseBody;
	}

	public Element getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Element requestHeader) {
		this.requestHeader = requestHeader;
	}

	public Element getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(Element responseHeader) {
		this.responseHeader = responseHeader;
	}

	public String getSopaAction() {
		return sopaAction;
	}

	public void setSopaAction(String sopaAction) {
		this.sopaAction = sopaAction;
	}

	static public SoapObject kDomWalk(Element e, SoapObject parent, String ns) {
		if (e == null)
			return null;
		parent = new SoapObject(ns, e.getName());
		System.out.println(e.getName());
		for (int i = 0; i < e.getChildCount(); ++i) {
			if (!e.isText(i)) {
				Element el = (Element) e.getChild(i);
				if (el.getChildCount() == 1) {
					parent.addProperty(el.getName(), el.getText(0));
					System.out.println("<<" + el.getName() + "<<"
							+ el.getText(0));
				} else {
					
					SoapObject child = null;
					kDomWalk(el, child, ns);
					parent.addSoapObject(child);
				}
			} else {
				continue;
			}
		}
		return parent;

	}

	static public SoapObject convertBody2SoapObject(SoapOperation op, String ns) {

		return kDomWalk(op.getRequestBody(), null, ns);
	}

	public static String[] op(WSDLDocument w, Element authHeader,
			String opName, String[] opInArgs, String[] opOutArgs,
			TextView responseLogger) {
		
		
		w.loadWSDL(opName);

		String[] ret = new String[opOutArgs.length];

		final String NAMESPACE = AppGlobalVariables.getInstance().NAMESPACE;
		final String URL = AppGlobalVariables.getInstance().URL;

		SoapOperation o = w.getOperation(opName);

		o.setRequestHeader(authHeader);
		w.printDom(o.getRequestBody(), 0, null);
		System.out.println(o.getRequestBody().getChildCount()+" "+opInArgs.length);
		for (int i = 0; i < opInArgs.length; ++i) {
			w.setElemenTextContext(o.getRequestBody(), i, opInArgs[i]);
		}

		SoapObject request = null;
		request = SoapOperation.convertBody2SoapObject(o, NAMESPACE);

		System.out.println(request.toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		envelope.headerOut = new Element[1];
		envelope.headerOut[0] = o.getRequestHeader();

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			String response = new String();
			androidHttpTransport.call(o.getSopaAction(), envelope);

			if (envelope.bodyIn instanceof SoapFault) {

				SoapFault result = (SoapFault) envelope.bodyIn;
				response += result.getMessage();
			} else {

				SoapObject result = (SoapObject) envelope.bodyIn;
				response=result.toString();
				for( int i=0; i< result.getPropertyCount(); ++i) {
					ret[i]=result.getPropertyAsString(i);
//				for (int i = 0; i < opOutArgs.length; ++i) {
//					ret[i] = result.getPropertyAsString(opOutArgs[i]);
				}

			}
			if (responseLogger != null)
				responseLogger.setText("" + response);

		} catch (IOException err) {
			err.printStackTrace();
		} catch (XmlPullParserException err2) {
			err2.printStackTrace();
		}
		return ret;
	}
	

}
