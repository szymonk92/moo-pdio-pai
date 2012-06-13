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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends Activity {

	PaymentActivity mThis;
	
	AlertDialog mAlertDialog;
	int amount;
	String description;
	String recipientName;
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
		mThis = this;

		paymentData = "destination:bolek@gmail.com\namount:200$";

		Button create = (Button) findViewById(R.id.createPayment);
		Button view = (Button) findViewById(R.id.viewPayment);
		Button commit = (Button) findViewById(R.id.commitPayment);
		log = (TextView) findViewById(R.id.paymentLog);
		responseResult = (TextView) findViewById(R.id.paymentResponse);

		// createDialog();

		paymentID = new String("acbd");

		create.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				createDialog();
			}
		});

		view.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				ListView mListview = new ListView(mThis);
				mListview.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View view,
							int arg2, long arg3) {
						// Do what you want on List Item Click
						TextView mTextView;
						mTextView = (TextView) view;
						String txtstr;
						txtstr = mTextView.getText().toString();
						
						String[] data = txtstr.split(";");
						
						paymentID = data[0].split("=")[1]; //ID
						recipientName = data[1].split("=")[1]; //Recipient
						description = data[2].split("=")[1]; //Description
						try {
						amount = Integer.parseInt(data[3].split("=")[1]); //Amount
						}catch (NumberFormatException e) {
							Log.i("PaymentActivity.this",""+data[3].split("=")[1]);
							amount = Integer.parseInt(data[3].split("=")[1].split("\\.")[0]); //Amount
						}
						
						log.setText("recipient:"+recipientName+"\namount:"+
						amount+"\ndescription:"+description+"\n");
						
						mAlertDialog.cancel();
						}
				});
				
				

				String[] paymentsList = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"GetAllPaymentTemplate", new String[] {}, new String[] {
								"Destination", "Amount" }, responseResult);
				
			
				
				if ( paymentsList.length < 1) {
					Toast.makeText(PaymentActivity.this, "pusto", 1).show();
					return;
				}
				
				String ret = new String();
				for (int i = 0; i < paymentsList.length; ++i) {
					ret += paymentsList[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + "\n" + ret);
				
				
				paymentsList=  (paymentsList[0].replace("PaymentTemplate=", "\n").replace("{", "").replace("}", "").replace("anyType", "")).split("\n");
				
				
				Log.i("PaymentActivity.this",""+paymentsList.length);
//				paymentsList[0].replace("PaymentTemplate=", "\n").replace("{", "").replace("}", "").replace("anyType", "");
				

				ArrayAdapter adapter = new ArrayAdapter<String>(
						PaymentActivity.this, R.layout.simplelist_item_text,
						paymentsList);
				mListview.setAdapter(adapter);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				AlertDialog.Builder mBuider = new AlertDialog.Builder(
						PaymentActivity.this);
				
				mBuider.setView(mListview);
				mAlertDialog = mBuider.create();
				mAlertDialog.show();

				lp.copyFrom(mAlertDialog.getWindow().getAttributes());
				

			}

		});

		commit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"CommitPayment",
						new String[] {paymentID},
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

		startActivityForResult(new Intent(this, CaptureActivity.class),
				requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			paymentData = data.getStringExtra("msg");
			String[] lines= paymentData.split("\n");
			recipientName = lines[0].split(":")[1];
			description = lines[1].split(":")[1];
			amount = Integer.parseInt(lines[2].split(":")[1]);
			
			
			 String[] response = SoapOperation.op(
			 AppGlobalVariables.getInstance().wsdl,
			 AppGlobalVariables.getInstance().authHeader,
			 "MakePaymentTemplate",
			 // TODO update when query will be changed
			 new String[] {
				 recipientName,
					description,
					Integer.toString(amount)
			 },
			 new String[] { "MakePaymentTemplateResult" }, responseResult);
			 String ret = new String();
			 for (int i = 0; i < response.length; ++i) {
			 ret += response[i] + "\n";
			 }
			 responseResult.setText(responseResult.getText() + ret);
			
			 paymentID = Integer.toString(666);

			
			
			log.setText(paymentData);

		} else {
			paymentData = "";
			log.setText(R.string.paymentAbort);
		}
	}

		
}
