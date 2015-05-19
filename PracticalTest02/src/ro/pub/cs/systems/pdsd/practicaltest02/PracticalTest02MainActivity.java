package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PracticalTest02MainActivity extends Activity {

	private EditText operator1EditText, operator2EditText;
	private TextView addTextView, mulTextView,serverPortTextView;
	private Button addButton, mulButton;
	
	
	private class CalculatorWebServiceThreadAdd extends Thread {
		
		@Override
		public void run() {
			
			String errorMessage = null;
			
			String operator1 = operator1EditText.getText().toString();
			String operator2 = operator2EditText.getText().toString();
			
			if (operator1 == null || operator1.isEmpty()) {
				errorMessage = Constants.ERROR_MESSAGE_EMPTY;
			}

			if (operator2 == null || operator2.isEmpty()) {
				errorMessage = Constants.ERROR_MESSAGE_EMPTY;
			}
			
			if (errorMessage != null) {
				final String finalizedErrorMessage = errorMessage;
				addTextView.post(new Runnable() {
					@Override
					public void run() {
						addTextView.setText(finalizedErrorMessage);
					}
				});
				return;
			}
			String result = null;
			HttpClient httpClient = new DefaultHttpClient();
			
					HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
					List<NameValuePair> params = new ArrayList<NameValuePair>();        
					params.add(new BasicNameValuePair(new String("add"), new String("+")));
					params.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1));
					params.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2));
					try {
						UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
						httpPost.setEntity(urlEncodedFormEntity);
					} catch (UnsupportedEncodingException unsupportedEncodingException) {
						Log.e(Constants.TAG, unsupportedEncodingException.getMessage());
						if (Constants.DEBUG) {
							unsupportedEncodingException.printStackTrace();
						}						
					}
					ResponseHandler<String> responseHandlerPost = new BasicResponseHandler();
					try {
						result = httpClient.execute(httpPost, responseHandlerPost);
					} catch (ClientProtocolException clientProtocolException) {
						Log.e(Constants.TAG, clientProtocolException.getMessage());
						if (Constants.DEBUG) {
							clientProtocolException.printStackTrace();
						}
					} catch (IOException ioException) {
						Log.e(Constants.TAG, ioException.getMessage());
						if (Constants.DEBUG) {
							ioException.printStackTrace();
						}
					}					
					
			
			final String finalizedResult = result;
			addTextView.post(new Runnable() {
				@Override
				public void run() {
					addTextView.setText(finalizedResult);
				}
			});
		}
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_main, menu);
		
		
		
		operator1EditText = (EditText)findViewById(R.id.operator1);
		operator2EditText = (EditText)findViewById(R.id.operator2);
		operator1EditText.setText("0");
		operator2EditText.setText("0");
		
		addTextView = (TextView)findViewById(R.id.addresult);
		mulTextView = (TextView)findViewById(R.id.mulresult);
		
		addButton =(Button)findViewById(R.id.add);
		mulButton = (Button)findViewById(R.id.mul);
		
		serverPortTextView = (TextView)findViewById(R.id.portServer);
		addButton.setText("+");
		mulButton.setText("*");
		serverPortTextView.setText("2651");

		addTextView.setText("0");
		mulTextView.setText("0");
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
