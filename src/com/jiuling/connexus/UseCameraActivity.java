package com.jiuling.connexus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


// TODO remember to release the camera finally
public class UseCameraActivity extends Activity {

	private Camera mCamera;
    private CameraPreview mPreview;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_camera);
		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		Button cameraCaptureButton = (Button) findViewById(R.id.camera_capture_button);
		cameraCaptureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCamera.takePicture(null, null, mPicture);
			}
		});
		
		Button cameraSelectButton = (Button) findViewById(R.id.camera_select_button);
		cameraSelectButton.setEnabled(false);
		cameraSelectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				Long streamId= intent.getLongExtra(UploadImageActivity.STREAMID, 0);
			    String streamName = intent.getStringExtra(UploadImageActivity.STREAMNAME);
			    Button uploadImageButton = (Button)findViewById(R.id.upload_image_button);
				uploadImageButton.setEnabled(true);
			    Intent newIntent = new Intent(v.getContext(),UploadImageActivity.class);
			    newIntent.putExtra(UploadImageActivity.STREAMID, streamId);
			    newIntent.putExtra(UploadImageActivity.STREAMNAME, streamName);
				startActivity(newIntent);
				
			}
		});
		
		Button cameraBackButton = (Button) findViewById(R.id.camera_back_button);
		cameraBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),ViewAllStreamsActivity.class);
				startActivity(intent);
			}
		});
		
		
		
	}


	PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
             
            WebUtility.path=pictureFile.getPath();
            WebUtility.needRotate="yes";
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }finally {
            	    
            	Button cameraSelectButton = (Button) findViewById(R.id.camera_select_button);
        		cameraSelectButton.setEnabled(true);
                camera.startPreview();
               
                
              }
        }
        
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
    

    public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.use_camera, menu);
		return true;
	}

}
