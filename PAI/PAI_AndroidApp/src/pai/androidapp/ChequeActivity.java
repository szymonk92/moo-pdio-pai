package pai.androidapp;

import wsdldom.SoapOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChequeActivity extends Activity {

	int amount;
	AlertDialog.Builder alert;
	Dialog amountAsk;
	TextView log;
	TextView responseResult;
	String chequeID;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheque);

		Button create = (Button) findViewById(R.id.createCheque);
		Button view = (Button) findViewById(R.id.viewCheque);
		Button commit = (Button) findViewById(R.id.commitCheque);
		log = (TextView) findViewById(R.id.chequeLog);
		responseResult = (TextView) findViewById(R.id.ChequeResponse);
		
		createDialog();
		amountAsk=alert.create();

		create.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				amountAsk.show();
			}
		});

		view.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl, AppGlobalVariables.getInstance().authHeader,
						"ViewCheque",
						new String[]{chequeID},
						new String[]{"Source","Amount"},
						responseResult);
				
				//TODO print result in log
				
				responseResult.setText(responseResult.getText()+response.toString());

			}
		});

		commit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				
				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl, AppGlobalVariables.getInstance().authHeader,
						"AcceptCheque",
						new String[]{chequeID},
						new String[]{"AcceptChequeResult"},
						responseResult);
				
				
				responseResult.setText(responseResult.getText()+response.toString());

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
				amount = Integer.parseInt(value);
				log.setText("name:"+AppGlobalVariables.getInstance().getUsername()+
						"\n"+"amount:"+amount+"\n");
				
				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl, AppGlobalVariables.getInstance().authHeader,
						"MakeCheque",
						//TODO update when query will be changed
						new String[]{AppGlobalVariables.getInstance().username,Integer.toString(amount)},
						new String[]{"MakeChequeResult"},
						responseResult);
				
				responseResult.setText(responseResult.getText()+response.toString());
			}	
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						amount = 0;

					}
				});

		
	}

}
