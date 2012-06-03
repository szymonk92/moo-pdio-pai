package pai.androidapp;

import java.io.InputStream;
import java.net.URL;

import com.google.zxing.client.android.CaptureActivity;



import wsdldom.SoapOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends Activity {

	int amount;
	AlertDialog.Builder alert;
	Dialog scanner;
	TextView log;
	TextView responseResult;
	String paymentID;
    int requestCode;
    String paymentData;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment);
		
		paymentData="destination:bolek@gmail.com\namount:200$";

		Button create = (Button) findViewById(R.id.createPayment);
		Button view = (Button) findViewById(R.id.viewPayment);
		Button commit = (Button) findViewById(R.id.commitPayment);
		log = (TextView) findViewById(R.id.paymentLog);
		responseResult = (TextView) findViewById(R.id.paymentResponse);

//		createDialog();

		paymentID = new String("acbd");

		create.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				createDialog();
			}
		});

		view.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"ViewPaymentTemplate",
						new String[] { paymentID },
						new String[] { "Destination", "Amount" }, responseResult);

				// TODO print result in log
				String ret = new String();
				for (int i = 0; i < response.length; ++i) {
					ret += response[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + "\n" + ret);

			}

		});

		commit.setOnClickListener(new OnClickListener() {
			
			

			public void onClick(View arg0) {

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"CommitPayment",
						new String[] { AppGlobalVariables.getInstance().username, paymentID },
						new String[] { "CommitPaymentResult" }, responseResult);

				String ret = new String();
				for (int i = 0; i < response.length; ++i) {
					ret += response[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + ret);

			}
		});

	}

	private void createDialog() {
		
		startActivityForResult(new Intent(this, CaptureActivity.class), requestCode);
		
		//TODO add QR scanner

//		log.setText("name:" + AppGlobalVariables.getInstance().getUsername()
//				+ "\n" + "amount:" + amount + "\n");
//
//		String[] response = SoapOperation.op(
//				AppGlobalVariables.getInstance().wsdl,
//				AppGlobalVariables.getInstance().authHeader,
//				"MakePaymentTemplate",
//				// TODO update when query will be changed
//				new String[] { "amount" },
//				new String[] { "MakePaymentTemplateResult" }, responseResult);
//		String ret = new String();
//		for (int i = 0; i < response.length; ++i) {
//			ret += response[i] + "\n";
//		}
//		responseResult.setText(responseResult.getText() + ret);
//
//		paymentID = ret;

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
	        if(resultCode==1){
	        	paymentData = data.getStringExtra("msg");
	        	log.setText(paymentData);
	        	
	        }
	        else{
	        	paymentData="";
	            log.setText(R.string.paymentAbort);
	        }
	}
	
	

}
