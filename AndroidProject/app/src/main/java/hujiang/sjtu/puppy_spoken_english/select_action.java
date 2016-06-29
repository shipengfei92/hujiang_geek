package hujiang.sjtu.puppy_spoken_english;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class select_action extends Activity implements View.OnClickListener{
    private ImageView shaking_tail;
    private ImageView sleeping;
    private ImageView background;
    private ImageView btn_choose1;
    private ImageView btn_choose2;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_action);
        shaking_tail = (ImageView) findViewById(R.id.shaking_tail);
        sleeping = (ImageView)findViewById(R.id.sleeping);
        background = (ImageView)findViewById(R.id.background);
        btn_choose1 = (ImageView)findViewById(R.id.btn_choose1);
        btn_choose2 = (ImageView)findViewById(R.id.btn_choose2);
        btn_choose1.setOnClickListener(this);
        btn_choose2.setOnClickListener(this);
        sleeping.setOnClickListener(this);
        animationDrawable = (AnimationDrawable) shaking_tail.getDrawable();
        //开始动画
        animationDrawable.start();
        animationDrawable = (AnimationDrawable) sleeping.getDrawable();
        //开始动画
        animationDrawable.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sleeping:
                background.setVisibility(View.INVISIBLE);
                sleeping.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_choose1:
                startActivity(new Intent(select_action.this, reconization.class));
                break;
            case R.id.btn_choose2:
                startActivity(new Intent(select_action.this, Dictionary.class));
                break;
        }
    }
}
