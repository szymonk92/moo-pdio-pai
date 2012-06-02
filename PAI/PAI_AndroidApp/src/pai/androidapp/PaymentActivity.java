package pai.androidapp;

import java.io.InputStream;
import java.net.URL;

import wsdldom.SoapOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentActivity extends Activity {

	int amount;
	AlertDialog.Builder alert;
	Dialog scanner;
	TextView log;
	TextView responseResult;
	String paymentID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment);

		Button create = (Button) findViewById(R.id.createPayment);
		Button view = (Button) findViewById(R.id.viewPayment);
		Button commit = (Button) findViewById(R.id.commitPayment);
		log = (TextView) findViewById(R.id.paymentLog);
		responseResult = (TextView) findViewById(R.id.paymentResponse);

//		createDialog();

		paymentID = new String("acbd");

		create.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				scanner.show();
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
		
		//TODO add QR scanner

		log.setText("name:" + AppGlobalVariables.getInstance().getUsername()
				+ "\n" + "amount:" + amount + "\n");

		String[] response = SoapOperation.op(
				AppGlobalVariables.getInstance().wsdl,
				AppGlobalVariables.getInstance().authHeader,
				"MakePaymentTemplate",
				// TODO update when query will be changed
				new String[] { "amount" },
				new String[] { "MakePaymentTemplateResult" }, responseResult);
		String ret = new String();
		for (int i = 0; i < response.length; ++i) {
			ret += response[i] + "\n";
		}
		responseResult.setText(responseResult.getText() + ret);

		paymentID = ret;

	}

}
