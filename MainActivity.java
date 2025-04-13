// Güncellenmiş: MainActivity.java
package com.example.kulubecioglu_lab3_gles;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView gLView;

    private myWorkMode wmRef = null;
    private TextView cameraInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FrameLayout layout = new FrameLayout(this);
        gLView = new MyGLSurfaceView(this);

        cameraInfoText = new TextView(this);
        cameraInfoText.setText("a=0 b=0 x=0.0 y=0.0 z=0.0");
        cameraInfoText.setTextSize(14);
        cameraInfoText.setTextColor(0xFFFFFFFF);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.topMargin = 10;
        cameraInfoText.setLayoutParams(params);

        layout.addView(gLView);
        layout.addView(cameraInfoText);

        setContentView(layout);

        gLView.setCameraInfoTextView(cameraInfoText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Pyramid Rotation");
        menu.add(0, 2, 0, "Nine Cubes");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                setTitle("Pyramid Rotation");
                cameraInfoText.setText(""); // Koordinat metnini gizle
                myModeStart(new myPyramidRotationMode(), MyGLSurfaceView.RENDERMODE_CONTINUOUSLY);


                return true;
            case 2:
                myNineCubesMode mode = new myNineCubesMode();

                setTitle("Nine Cubes"); // Nine Cubes metnini sil
                myModeStart(mode, MyGLSurfaceView.RENDERMODE_WHEN_DIRTY);
                gLView.updateCameraInfoText();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void myModeStart(myWorkMode wmode, int rendermode) {
        wmRef = wmode;

        if (wmRef instanceof myPyramidRotationMode) {
            ((myPyramidRotationMode) wmRef).setCameraUpdateListener(info ->
                    runOnUiThread(() -> setTitle(info))
            );
        } else if (wmRef instanceof myNineCubesMode) {
            ((myNineCubesMode) wmRef).setCameraUpdateListener(info ->
                    runOnUiThread(() -> setTitle(info))
            );
        }

        gLView.setWorkMode(wmode);
        gLView.setRenderMode(rendermode);
        gLView.requestRender();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (wmRef instanceof myNineCubesMode) {
            myNineCubesMode mode = (myNineCubesMode) wmRef;


            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    mode.adjustAlpha(-5);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    mode.adjustAlpha(5);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    mode.adjustBeta(5);
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    mode.adjustBeta(-5);
                    break;
            }
            gLView.updateCameraInfoText();
            gLView.requestRender();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}