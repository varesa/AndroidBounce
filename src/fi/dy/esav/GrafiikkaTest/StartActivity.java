package fi.dy.esav.GrafiikkaTest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Button start = (Button) findViewById(R.id.start_button);
		start.setOnClickListener(new StartbuttonListener(this));
	}
	
	private class StartbuttonListener implements OnClickListener {

		private StartbuttonListener() {
			
		}
		
		private StartActivity main = null;
		
		public StartbuttonListener(StartActivity main) {
			this.main = main;
		}
		
		@Override
		public void onClick(View v) {
			Intent newactivity = new Intent(main, MainActivity.class);
			startActivity(newactivity);
			
		}
		
	}

}
