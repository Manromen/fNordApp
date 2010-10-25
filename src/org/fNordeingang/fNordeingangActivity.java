package org.fNordeingang;

// java
import java.io.*;

// android
import android.app.Activity;
import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.TextView;

// json
import org.json.*;

// http
import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import de.mastacode.http.Http;

public class fNordeingangActivity extends Activity implements OnClickListener {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		// update label of fNordStatus
		int status = getfNordStatus();
		TextView statusView = (TextView)findViewById(R.id.fNordStatusLabel);
		switch (status) {
			case 0:
				statusView.setText(R.string.fNordStatusClosed);
				break;
			case 1:
				statusView.setText(R.string.fNordStatusOpen);
				break;
			default: // on error (f.e. no internet connection) just display the label
				statusView.setText(R.string.fNordStatus);
				break;
		}
        
        ImageButton tweetButton = (ImageButton)findViewById(R.id.fNordTweet);
        ImageButton doorButton = (ImageButton)findViewById(R.id.fNordDoor);
		ImageButton statusButton = (ImageButton)findViewById(R.id.fNordStatus);
        tweetButton.setOnClickListener(this);
        doorButton.setOnClickListener(this);
		statusButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fNordTweet) {
            // start fNordTweet
            this.startActivity(new Intent(this, fNordTweetActivity.class));
		} else if (id == R.id.fNordStatus) {
			// Status Action
			print("comming soon!");
        } else if (id == R.id.fNordDoor) {
            // Door Action here
            print("not yet implemented!");
        } else {
            // Error here
            print("Error: Unknown Button pressed!");
        }
    }
	
	public int getfNordStatus() {
		try {
			// get json string
			HttpClient client = new DefaultHttpClient();
			String jsonstring = Http.get("http://fnordeingang.de:4242/status").use(client).asString();
			
			// get status
			JSONObject status = new JSONObject(jsonstring);
			if (status.getBoolean("open")) {
				return 1; // open
			} else {
				return 0; // closed
			}

		} catch (IOException ioe) {
			print(ioe.toString());
			return -1;
		} catch (JSONException jsone) {
			print(jsone.toString());
			return -1;
		}
	}
	
	// helper function
	void print(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
		
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}