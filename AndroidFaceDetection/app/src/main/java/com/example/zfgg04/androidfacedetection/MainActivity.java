package com.example.zfgg04.androidfacedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        button = findViewById(R.id.btn_process);

        final Bitmap bitmap =  BitmapFactory.decodeResource(getApplicationContext().getResources()
                ,R.drawable.tom);
        imageView.setImageBitmap(bitmap);

        //Made a rect frame by rectPaint
        final Paint rectPaint = new Paint();
        rectPaint.setStrokeWidth(10);
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStyle(Paint.Style.STROKE);


        //To make a temporary bitmap to draw on canvas
        final Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getWidth(),Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if(!faceDetector.isOperational()){
                    Toast.makeText(MainActivity.this,"Face detector is not ready",Toast.LENGTH_LONG).show();
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face>  faces = faceDetector.detect(frame);

                for(int i=0;i < faces.size(); i++){
                    Face face = faces.valueAt(i);
                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1+face.getWidth();
                    float y2 = y1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);
                }
                imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
            }
        });

    }
}
