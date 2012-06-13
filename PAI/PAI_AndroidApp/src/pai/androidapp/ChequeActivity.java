package pai.androidapp;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wsdldom.SoapOperation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChequeActivity extends Activity {

	int amount;
	AlertDialog.Builder alert;
	AlertDialog mAlertDialog;
	 ChequeActivity mThis;
	Dialog amountAsk;
	Dialog chequeView;
	TextView log;
	TextView responseResult;
	
	
	String description;
	String chequeID;
	String date;


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
						
						////TODO!!!!!!!!!!!!!!!!!!
						////!!!!!!!!!!!!!!!
						chequeID = data[0].split("=")[1]; //ID
						description = data[3].split("=")[1]; //Description
						date = data[6].split("=")[1]; //Date
						try {
						amount = Integer.parseInt(data[4].split("=")[1]); //Amount
						}catch (NumberFormatException e) {
							Log.i("PaymentActivity.this",""+data[4].split("=")[1]);
							amount = Integer.parseInt(data[4].split("=")[1].split("\\.")[0]); //Amount
						}
						
						log.setText(chequeID+"\namount:"+
						amount+"\ndescription:"+description+"\n");
						
						mAlertDialog.cancel();
						
						
						checkViewer("recipientName:"+AppGlobalVariables.getInstance().username+"\ndescription:"+description+"\namount:"+amount);
						}
				});
				
				

				String[] paymentsList = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"GetAvailableCheques",
						new String[] {}, new String[] {}
						, responseResult);
				
			
				
				if ( paymentsList.length < 1) {
					Toast.makeText(ChequeActivity.this, "pusto", 1).show();
					return;
				}
				
				String ret = new String();
				for (int i = 0; i < paymentsList.length; ++i) {
					ret += paymentsList[i] + "\n";
				}
				responseResult.setText(responseResult.getText() + "\n" + ret);
				
				
				paymentsList=  (paymentsList[0].replace("GetAvailableChequesResult=", "")
						.replace("Cheque=", "\n")
						.replace("{", "").replace("}", "").replace("anyType", "")).split("\n");
				
				
				Log.i("PaymentActivity.this",""+paymentsList.length);
//				paymentsList[0].replace("PaymentTemplate=", "\n").replace("{", "").replace("}", "").replace("anyType", "");
				

				ArrayAdapter adapter = new ArrayAdapter<String>(
						ChequeActivity.this, R.layout.simplelist_item_text,
						paymentsList);
				mListview.setAdapter(adapter);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				AlertDialog.Builder mBuider = new AlertDialog.Builder(
						ChequeActivity.this);
				
				mBuider.setView(mListview);
				mAlertDialog = mBuider.create();
				mAlertDialog.show();

				lp.copyFrom(mAlertDialog.getWindow().getAttributes());
				
				
				

			}

		});

		commit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date d = null;
				try {
					d =  sdf.parse(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"CashCheque", new String[] { chequeID, ""+d.getSeconds() },
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

		alert.setCancelable(true);
		// Set an EditText view to get user input
		
		final LinearLayout linl = new LinearLayout(this);
		linl.setOrientation(1);
		final TextView tva = new TextView(this);
		tva.setText(R.string.chequeDialogAmount);
		final TextView tvd = new TextView(this);
		tvd.setText(R.string.chequeDialogDesc);
		final EditText inputa = new EditText(this);
		final EditText inputd = new EditText(this);
		linl.addView(tva); 
		linl.addView(inputa);
		linl.addView(tvd);
		linl.addView(inputd);
		
		alert.setView(linl);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = inputa.getText().toString();
				description = inputd.getText().toString();
				try {
				amount = Integer.parseInt(value);
				
				log.setText("desc:"
						+ description + "\n"
						+ "amount:" + amount + "\n");

				String[] response = SoapOperation.op(
						AppGlobalVariables.getInstance().wsdl,
						AppGlobalVariables.getInstance().authHeader,
						"GenerateCheque",
						// TODO update when query will be changed
						new String[] {
								description,
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
