package by.roman.shootingapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Roma on 13.07.2016.
 */
public class ImageSveAsync extends AsyncTask <String,String,String> {

    private Context context;

    public ImageSaveCallback delegate = null;

    public final int SPLASH_DISPLAY_LENGTH = 1000;

    public ImageSveAsync(AppCompatActivity activity) {
        this.context = activity;
    }
    public ImageSveAsync(String str){

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        switch (params[0]){
            case "25m":{
                SaveImageToGaleryUtils.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.a25m_target), "shootingApp" , "25m");
                break;
            }
            case "30m":{
                SaveImageToGaleryUtils.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.a25m_target1), "shootingApp" , "30m");
                break;
            }
            case "50m":{
                SaveImageToGaleryUtils.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.a25m_target2), "shootingApp" , "50m");
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.imageSaved(s);
    }
}
