package wsdldom;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.kxml2.kdom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class WSDLDocument {
	
	String ns;
	public XPath xpath;
	public Document doc;
	HashMap<String, SoapOperation> services;
	
	
	public void setTargetNamespace(String ns) {
		this.ns =ns;
	}
	
	public WSDLDocument(String url) {
		DocumentBuilder docBuild;
		DocumentBuilderFactory docBuildFactory;
		Document domDoc = null;
		URL link;
		URLConnection yc;
		try {  
			docBuildFactory = DocumentBuilderFactory.newInstance();
			docBuildFactory.setNamespaceAware(true);
			docBuild = docBuildFactory.newDocumentBuilder();
			link = new URL(url);
			yc = link.openConnection();
			domDoc = docBuild.parse(yc.getInputStream());
			System.out.println(domDoc.getFirstChild().getNodeName());
			
		} catch (IOException ioe) {ioe.printStackTrace();}
		catch (SAXException e) {e.printStackTrace();}
		catch (ParserConfigurationException e) {e.printStackTrace();}
		
		this.doc=domDoc;
		
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			public Iterator getPrefixes(String namespaceURI) {return null;}
			public String getPrefix(String namespaceURI) {return null;}
			public String getNamespaceURI(String prefix) {
                String uri=null;
                if (prefix.equals("s"))
                    uri = "http://www.w3.org/2001/XMLSchema";
                else if (prefix.equals("wsdl"))
                    uri = "http://schemas.xmlsoap.org/wsdl/";
                else if (prefix.equals("tm"))
                	uri = "http://microsoft.com/wsdl/mime/textMatching/";
                else if (prefix.equals("tns"))
                	uri = "http://tempuri.org/";
                else if (prefix.equals("soap12"))
                	uri="http://schemas.xmlsoap.org/wsdl/soap12/";
                else 
                	uri="http://schemas.xmlsoap.org/wsdl/soap/";
                return uri;
            }

		});
		this.xpath=xpath;
		
		
		this.services  = new HashMap<String, SoapOperation>();
	}
	
	
	/**
	 * 
	 * @param doc - Document
	 * @param xpaths - String xpath query
	 * @return result of query
	 */
	public NodeList getNodesByXPath(Object doc, String xpaths) {
		NodeList nodes=null;
		try {
			XPathExpression expr = xpath.compile(xpaths);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			  nodes = (NodeList) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodes;
	}
	
	public SoapOperation getOperation(String opName) {
		return services.get(opName);
	}
	
	public Set<String> avaiableOperations() {
		return services.keySet();
	}
	
	/**
	 * 
	 * @param doc - Document
	 * @param xpath - String xpath query
	 * @return Element according to wsdl definition
	 */
	public Element buildElementByName(Object doc, String rName) {
		Element e= new Element().createElement(ns, rName);
		e.setPrefix("", ns);
		e.setName(rName);
		
		
		NodeList expr;
			
			expr = getNodesByXPath(doc, "//wsdl:types//s:element[@name='"+rName+"']");
			Node t = expr.item(0).getAttributes().getNamedItem("type");
			String type = (t!=null?t.getTextContent():null);
			if (type != null) {
				String[] prefType = type.split(":");
				type=prefType[1];
				if (prefType[0].equals("tns")) { //complex type
					expr = getNodesByXPath(doc, "//wsdl:types//s:complexType[@name='"+type+"']/s:sequence/s:element");
					for (int j = 0; j < expr.getLength(); ++j) {
						Node n =expr.item(j);
						Element child = buildElementByName(doc, n.getAttributes().getNamedItem("name").getTextContent());
						e.addChild(Element.ELEMENT, child);
					} //else simple type -> return as it is
				} 
			} else {
				expr = getNodesByXPath(doc, "//wsdl:types//s:element[@name='"+ rName + "']//s:element");

				for (int j = 0; j < expr.getLength(); ++j) {
					Node n =expr.item(j);
					Element child = buildElementByName(doc, n.getAttributes().getNamedItem("name").getTextContent());
					e.addChild(Element.ELEMENT, child);
				} //else simple type -> return as it is 
			}
		
		return e;
	}
	
	public void setElemenTextContext(Element r,String property, String value) {
//		Element newChild = new Element().createElement(ns,value );
		Element e = r.getElement(ns, property);
		e.addChild(Element.TEXT,value);
	}
	public void setElemenTextContext(Element r,int index, String value) {
//		Element newChild = new Element().createElement(ns,value );
		Element e = r.getElement(index);
		e.addChild(Element.TEXT,value);
	}
	
	public void printDom(Element e, int lvl) {
		if (e == null)
			return;
		String padding = new String();
		for (int i = 0; i < lvl; ++i)
			padding += "\t";
		System.out.println(padding + e.getName());
		for (int i = 0; i < e.getChildCount(); ++i) {
			lvl++;
			printDom((Element) e.getChild(i), lvl);
			lvl--;
		}

	}
	
	/**
	 * Load WSDL from given url and build operation templates
	 * @param op it empty String given, loads all defined operations
	 */
	public void loadWSDL(String op) {
		NodeList operations;

			operations = getNodesByXPath(doc, "/wsdl:definitions/wsdl:portType/wsdl:operation"+(op.equals("")?"":"[@name='"+op+"']"));
		
		for( int i=0; i<operations.getLength(); ++i) {
			String opName=null, opSoapAction=null,
					inputMsg=null,inputMsgType=null,inputMsgHeader=null,
					outputMsg=null, outputMsgType=null,outputMsgHeader=null;
			Element inputMsgBodyElem=null,inputMsgHeaderElem=null,
					outputMsgBodyElem=null,outputMsgHeaderElem=null;
			//operation
			opName = operations.item(i).getAttributes().getNamedItem("name").getTextContent();
			
			//input Message
			NodeList expr = getNodesByXPath(doc,"//wsdl:operation[@name='"+opName+"']/wsdl:input");
			inputMsg = expr.item(0).getAttributes().getNamedItem("message").getTextContent();
			inputMsg = (inputMsg.split(":"))[1];
			//input Message Type
			expr = getNodesByXPath(doc, "//wsdl:message[@name='"+inputMsg+"']/wsdl:part/@element");
			inputMsgType = expr.item(0).getTextContent();
			inputMsgType = (inputMsgType.split(":"))[1];
			//input body DOM-Element
			inputMsgBodyElem = buildElementByName(doc, inputMsgType);
			
			
			//input  Header 
			expr = getNodesByXPath(doc, "//wsdl:binding[@name='MainWebServiceSoap']/wsdl:operation[@name='"+opName+"']/wsdl:input/soap:header");
			if (expr.getLength()>0) {
				inputMsgHeader = expr.item(0).getAttributes().getNamedItem("part").getTextContent();
				//input header DOM-Element
				inputMsgHeaderElem  = buildElementByName(doc, inputMsgHeader) ;
			}
			
			//output Message
			expr = getNodesByXPath(doc,"//wsdl:operation[@name='"+opName+"']/wsdl:output");
			outputMsg = expr.item(0).getAttributes().getNamedItem("message").getTextContent();
			outputMsg = (outputMsg.split(":"))[1];
			//output Message Type
			expr = getNodesByXPath(doc, "//wsdl:message[@name='"+outputMsg+"']/wsdl:part/@element");
			outputMsgType = expr.item(0).getTextContent();
			outputMsgType = (outputMsgType.split(":"))[1];
			//output body DOM-Element
			outputMsgBodyElem = buildElementByName(doc, outputMsgType);
			
			
			//Output Header 
			expr = getNodesByXPath(doc, "//wsdl:binding[@name='MainWebServiceSoap']/wsdl:operation[@name='"+opName+"']/wsdl:output/soap:header");
			if (expr.getLength()>0) {
				outputMsgHeader = expr.item(0).getAttributes().getNamedItem("part").getTextContent();
				//output header DOM-Element
				outputMsgHeaderElem = buildElementByName(doc, outputMsgHeader);
			}
			
			
			//operation soap Action
			expr = getNodesByXPath(doc, "//wsdl:binding[@name='MainWebServiceSoap']//wsdl:operation[@name='"+opName+"']/soap:operation/@soapAction");
			opSoapAction=expr.item(0).getTextContent();
			
			services.put(opName, new SoapOperation(inputMsgBodyElem,outputMsgBodyElem,inputMsgHeaderElem,outputMsgHeaderElem,opSoapAction));
			
//			System.out.println(opName + " soapAction:"+opSoapAction);
//			
//			System.out.println(inputMsg+":");
//			
//			System.out.println("\tHEADER");
//			printDom(inputMsgHeaderElem, 1);
//			System.out.println("\tBODY");
//			printDom(inputMsgBodyElem, 1);
//			
//			System.out.println(outputMsg+":");
//			System.out.println("\tHEADER");
//			printDom(outputMsgHeaderElem, 1);
//			System.out.println("\tBODY");
//			printDom(outputMsgBodyElem, 1);
			
		}
	}

}
