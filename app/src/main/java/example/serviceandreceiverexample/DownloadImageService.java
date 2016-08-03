package example.serviceandreceiverexample;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class DownloadImageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (MainActivity.ACTION_DOWNLOAD_IMAGE.equals(intent.getAction())) {


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {

                        String path = Environment.getExternalStorageDirectory().toString();
                        OutputStream fOut = null;
                        File file = new File(path, "Android.jpg"); // the File to save to
                        fOut = new FileOutputStream(file);

                        URL url = new URL("http://cdn3.pcadvisor.co.uk/cmsdata/features/3628675/android_nougat_release_date_new_features_thumb800.jpg");
                        Bitmap pictureBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                        fOut.flush();
                        fOut.close(); // do not forget to close the stream

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(MainActivity.ACTION_BROADCAST_BITMAP);
                        broadcastIntent.putExtra(MainActivity.EXTRA_IMAGE_PATH, file.getPath());
                        sendBroadcast(broadcastIntent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }.execute();

        }

        return START_REDELIVER_INTENT;
    }
}


