package hujiang.sjtu.puppy_spoken_english;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Start extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Timer time = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                startActivity(new Intent(Start.this, Select_Dog.class));
            }
        };
        time.schedule(task, 3000);
    }
}
