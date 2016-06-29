package hujiang.sjtu.puppy_spoken_english;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class reconization extends Activity implements View.OnClickListener{
    private SurfaceView sView;
    private  SurfaceHolder surfaceHolder;
    private  ImageView imageView;
    private ImageView back;
    private ImageView dictionary;
    private ImageView sound;
    private ImageView next;
    private ImageView word_bg;
    private TextView word;
    private ImageView finish_bg;
    private ImageView go;
    private ImageView feeding;
    private AnimationDrawable animationDrawable;
    private Camera camera;
    private String wordshow[] = {"Chair", "Desk","Apple","Cup"};
    private int progressId[] = {R.drawable.progress_bar1,R.drawable.progress_bar2,R.drawable.progress_bar3,R.drawable.progress_bar4};
    // 是否在预览中
    private boolean isPreview = false;
    private int state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reconization);
        sView= (SurfaceView) findViewById(R.id.camera);
        imageView=(ImageView) findViewById(R.id.preview);
//        feeding = (ImageView)findViewById(R.id.feedingBorn);
//        animationDrawable = (AnimationDrawable) feeding.getDrawable();
        surfaceHolder = sView.getHolder();
        isPreview = false;
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height)
            {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                // 打开摄像头
                initCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                 //如果camera不为null ,释放摄像头
                if (camera != null)
                {
                    if (isPreview) camera.stopPreview();

                    isPreview=false;
                }
            }
        });
        init();
    }

    private void init(){
        back = (ImageView)findViewById(R.id.btn_r_back);
        dictionary = (ImageView)findViewById(R.id.btn_r_dictionary);
        sound = (ImageView)findViewById(R.id.btn_r_sound);
        next = (ImageView)findViewById(R.id.next);
        word_bg = (ImageView)findViewById(R.id.text_bg);
        word = (TextView)findViewById(R.id.word);
        finish_bg = (ImageView)findViewById(R.id.finish_bg);
        go = (ImageView)findViewById(R.id.btn_r_go);
        feeding = (ImageView)findViewById(R.id.feedingBorn);
        back.setOnClickListener(this);
        dictionary.setOnClickListener(this);
        sound.setOnClickListener(this);
        next.setOnClickListener(this);
        word_bg.setVisibility(View.INVISIBLE);
        word.setVisibility(View.INVISIBLE);
        finish_bg.setVisibility(View.INVISIBLE);
        animationDrawable = (AnimationDrawable) feeding.getDrawable();
        feeding.setVisibility(View.INVISIBLE);
        go.setOnClickListener(this);
        go.setVisibility(View.INVISIBLE);
        state = 1;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_r_back:
                startActivity(new Intent(reconization.this, select_action.class));
                break;
            case R.id.btn_r_dictionary:
                startActivity(new Intent(reconization.this, Dictionary.class));
                break;
            case R.id.btn_r_sound:
                //添加发音部分代码
                break;
            case R.id.next:
                word_bg.setVisibility(View.VISIBLE);
                word.setVisibility(View.VISIBLE);
                switch (state){
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        word.setText(wordshow[(int)state/2]);
                        //试音部分代码
                        break;
                    case 2:
                    case 4:
                    case 6:
                        next.setImageResource(progressId[state/2]);
                        word_bg.setVisibility(View.INVISIBLE);
                        word.setVisibility(View.INVISIBLE);
                        break;
                    case 8:
                        finish_bg.setVisibility(View.VISIBLE);
                        feeding.setVisibility(View.VISIBLE);
                        go.setVisibility(View.VISIBLE);
                        animationDrawable.start();
                        break;
                    default:
                        break;

                }
                state++;
                break;
            case R.id.btn_r_go:
                startActivity(new Intent(reconization.this, select_action.class));
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            camera.release();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        camera = null;
    }

    private void initCamera() {
        if (camera==null)
        {
            // 此处默认打开后置摄像头。
            // 通过传入参数可以打开前置摄像头
            camera = Camera.open(0);  //①
            //camera.setDisplayOrientation(90);
        }
        if (camera != null && !isPreview)
        {
            try
            {
                Camera.Parameters parameters = camera.getParameters();
                // 设置预览照片的大小
                parameters.setPreviewSize(sView.getWidth(),sView.getHeight());
                // 设置预览照片时每秒显示多少帧的最小值和最大值
                parameters.setPreviewFpsRange(4, 10);
                // 设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                // 设置JPG照片的质量
                parameters.set("jpeg-quality", 85);
                // 设置照片的大小
                parameters.setPictureSize(sView.getWidth(),sView.getHeight());
                // 通过SurfaceView显示取景画面
                camera.setPreviewDisplay(surfaceHolder);  //②
                // 开始预览
                priviewCallBack pre = new priviewCallBack();//建立预览回调对象
                camera.setPreviewCallback(pre); //设置预览回调对象
                camera.startPreview();  //③
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }

    class priviewCallBack implements Camera.PreviewCallback {

        @Override
        //每次获取预览图像时调用
        public void onPreviewFrame(byte[] data, Camera camera) {
            decodeToBitMap(data, camera);
        }
        public void decodeToBitMap(byte[] data, Camera _camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
                        size.height, null);
                if (image != null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height),
                            80, stream);
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            stream.toByteArray(), 0, stream.size());
                    imageView.setImageBitmap(bmp);
                    stream.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

//    /**
//     * 此方法在布局文件中调用
//     * */
//    public void capture(View source)
//    {
//        if (camera != null)
//        {
//            // 控制摄像头自动对焦后才拍照
//            camera.autoFocus(autoFocusCallback);  //④
//        }
//    }
//
//    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback()
//    {
//        // 当自动对焦时激发该方法
//        @Override
//        public void onAutoFocus(boolean success, Camera camera)
//        {
//            if (success)
//            {
//                // takePicture()方法需要传入3个监听器参数
//                // 第1个监听器：当用户按下快门时激发该监听器
//                // 第2个监听器：当相机获取原始照片时激发该监听器
//                // 第3个监听器：当相机获取JPG照片时激发该监听器
//                camera.takePicture(new Camera.ShutterCallback()
//                {
//                    public void onShutter()
//                    {
//                        // 按下快门瞬间会执行此处代码
//                    }
//                }, new Camera.PictureCallback()
//                {
//                    public void onPictureTaken(byte[] data, Camera c)
//                    {
//                        // 此处代码可以决定是否需要保存原始照片信息
//                    }
//                }, myJpegCallback);  //⑤
//            }
//        }
//    };
//
//    Camera.PictureCallback myJpegCallback = new Camera.PictureCallback()
//    {
//        @Override
//        public void onPictureTaken(byte[] data, Camera camera)
//        {
//            // 根据拍照所得的数据创建位图
//            final Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
//                    data.length);
//            String fileName=getFileNmae();
//            if (fileName==null) return;
//            // 创建一个位于SD卡上的文件
//            File file = new File(fileName);
//            FileOutputStream outStream = null;
//            try
//            {
//                // 打开指定文件对应的输出流
//                outStream = new FileOutputStream(file);
//                // 把位图输出到指定文件中
//                bm.compress(Bitmap.CompressFormat.JPEG, 100,
//                        outStream);
//                outStream.close();
//                Toast.makeText(reconization.this, "照片以保存到"+fileName,
//                        Toast.LENGTH_SHORT).show();
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//
//            // 重新浏览
//            camera.stopPreview();
//            camera.startPreview();
//            isPreview = true;
//        }
//    };
//
//    /**
//     * 返回摄取照片的文件名
//     * @return 文件名
//     * */
//    protected String getFileNmae() {
//        // TODO Auto-generated method stub
//        String fileName;
//        if (!Environment.getExternalStorageState().equals
//                (Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this, "监测到你的手机没有插入SD卡，请插入SD卡后再试",
//                    Toast.LENGTH_LONG).show();
//            return null;
//        }
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.getDefault());
//        fileName=Environment.getExternalStorageDirectory()+File.separator
//                +sdf.format(new Date())+".JPG";
//        return fileName;
//    }