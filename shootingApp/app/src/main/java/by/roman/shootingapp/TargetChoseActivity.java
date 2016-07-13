package by.roman.shootingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Roma on 13.07.2016.
 */
public class TargetChoseActivity extends AppCompatActivity implements View.OnClickListener, ImageSaveCallback{

    Button a25mButton;
    Button a30mButton;
    Button a50mButton;

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_chose_layout);

        context = this;

        a25mButton = (Button)findViewById(R.id.precisionMode25mButton);
        a30mButton = (Button)findViewById(R.id.precisionMode30mButton);
        a50mButton = (Button)findViewById(R.id.precisionMode50mButton);

        a25mButton.setOnClickListener(this);
        a30mButton.setOnClickListener(this);
        a50mButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.precisionMode25mButton:{
                showDialog();
                ImageSveAsync imageSveAsync = new ImageSveAsync(this);
                imageSveAsync.delegate = this;
                imageSveAsync.execute("25m");
                break;
            }
            case R.id.precisionMode30mButton:{
                showDialog();
                ImageSveAsync imageSveAsync = new ImageSveAsync(this);
                imageSveAsync.delegate = this;
                imageSveAsync.execute("30m");
                break;
            }
            case R.id.precisionMode50mButton:{
                showDialog();
                ImageSveAsync imageSveAsync = new ImageSveAsync(this);
                imageSveAsync.delegate = this;
                imageSveAsync.execute("50m");
                break;
            }
        }
    }

    @Override
    public void imageSaved(String s) {
        System.gc();
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TargetChoseActivity.this);
        builder.setTitle("Image saved")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
