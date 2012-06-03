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
import android.widget.Toast;

public class ChequeActivity extends Activity {

	int amount;
	AlertDialog.Builder alert;
	 ChequeActivity mThis;
	Dialog amountAsk;
	Dialog chequeView;
	TextView log;
	TextView responseResult;
	String chequeID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mThis = this;
		
		setContentView(R.layout.cheque);

		Button create = (Button) findViewById(R.id.createCheque);
		Button view = (Button) findViewById(R.id.viewCheque);
		Button commit = (Button) findViewById(R.id.commitCheque);
		log = (TextView) findViewById(R.id.chequeLog);
		responseResult = (TextView) findViewById(R.id.ChequeResponse);

		createDialog();

		chequeID = new String("acbd");

		create.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				amountAsk.show();
			}
		});

		view.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"ViewCheque", new String[] { chequeID },
						new String[] { "Amount" }, responseResult);

				// TODO print result in log
				String ret = new String();
				for (int i = 0; i < response.length; ++i) {
					ret += response[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + "\n" + ret);

				checkViewer(chequeID);
			}

		});

		commit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"AcceptCheque", new String[] { chequeID },
						new String[] { "AcceptChequeResult" }, responseResult);

				String ret = new String();
				for (int i = 0; i < response.length; ++i) {
					ret += response[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + ret);

			}
		});

	}

	private void createDialog() {

		alert = new AlertDialog.Builder(this);

		alert.setTitle("Amount");
		alert.setMessage("input amount");
		alert.setCancelable(true);
		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				try {
				amount = Integer.parseInt(value);
				log.setText("name:"
						+ AppGlobalVariables.getInstance().getUsername() + "\n"
						+ "amount:" + amount + "\n");

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"MakeCheque",
						// TODO update when query will be changed
						new String[] {
								AppGlobalVariables.getInstance().username,
								Integer.toString(amount) },
						new String[] { "MakeChequeResult" }, responseResult);
				String ret = new String();
				for (int i = 0; i < response.length; ++i) {
					ret += response[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + ret);

				chequeID = ret;
				} catch(NumberFormatException e) {
					Toast.makeText(mThis, R.string.errInputFormat,1);
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						amount = 0;

					}
				});

		amountAsk = alert.create();

	}

	private void checkViewer(String id) {

		alert = new AlertDialog.Builder(this);

		final ImageView img = new ImageView(this);

		Object content = null;
		try {
			
			URL url = new URL(
					"http://chart.apis.google.com/chart?cht=qr&chs=230x230&chld=L&choe=UTF-8&chl="+id);
			content = url.getContent();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		InputStream is = (InputStream) content;
		Drawable image = Drawable.createFromStream(is, "src");
		img.setImageDrawable(image);

		alert.setView(img);

		chequeView = alert.create();
		chequeView.show();
	}

}
