package my.home.page;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	public EditText login;
	public EditText pass;
	private ProgressDialog dialog;
	private InputStream is;
	SecondActivity url;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button btn = (Button) findViewById(R.id.button1);
		login = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText2);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:
				new RequestTask().execute("http://myhomepage.hol.es/login.php");
			}
		});
	}

	class RequestTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			try {
				//создаем запрос на сервер
				DefaultHttpClient hc = new DefaultHttpClient();
				ResponseHandler<String> res = new BasicResponseHandler();
				//он у нас будет посылать post запрос
				HttpPost postMethod = new HttpPost(params[0]);
				//будем передавать два параметра
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				//передаем параметры из наших текстбоксов
				//лоигн
				nameValuePairs.add(new BasicNameValuePair("login", login.getText().toString()));
				//пароль
				nameValuePairs.add(new BasicNameValuePair("pass", pass.getText().toString()));
				//собераем их вместе и посылаем на сервер
				postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				//получаем ответ от сервера
				String response = hc.execute(postMethod, res);
				//посылаем на вторую активность полученные параметры
				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
				intent.putExtra(SecondActivity.JsonURL, response.toString());
				startActivity(intent);
			} catch (Exception e) {
				System.out.println("Exp=" + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Загружаюсь...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}
	}
}