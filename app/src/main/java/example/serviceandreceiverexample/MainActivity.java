package example.serviceandreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_DOWNLOAD_IMAGE = "example.serviceandreceiverexample.action.DOWNLOAD_IMAGE";
    public static final String ACTION_BROADCAST_BITMAP = "example.serviceandreceiverexample.action.ACTION_BROADCAST_BITMAP";
    public static final String EXTRA_IMAGE_PATH = "extra_bitmap";

    private IntentFilter intentFilter;
    private BroadcastReceiver receiver;
    private ImageView imageView;
    private Button downloadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        downloadImageButton = (Button) findViewById(R.id.button);

        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BROADCAST_BITMAP);

        receiver = new MyBroadcastReceiver();

        assert downloadImageButton != null;
        downloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), DownloadImageService.class);
                intent.setAction(ACTION_DOWNLOAD_IMAGE);
                startService(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_BROADCAST_BITMAP.equals(intent.getAction())) {
                String path = intent.getStringExtra(EXTRA_IMAGE_PATH);

                Bitmap bitmap = BitmapFactory.decodeFile(path);

                imageView.setImageBitmap(bitmap);

                Intent stopIntent = new Intent(MainActivity.this, DownloadImageService.class);
                stopService(stopIntent);
            }

        }
    }

}
