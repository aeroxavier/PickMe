package kylejimer.lapiz.com.pickme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;


    private String selectedImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void toGallery(View v){
        ImageButton btn = (ImageButton) findViewById(R.id.button1);
        ImageButton btn2 =(ImageButton)findViewById(R.id.button2);
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
        btn.setEnabled(false);
        btn.setVisibility(View.GONE);
        btn2.setVisibility(View.VISIBLE);
    }

public void ToShare(View v){
    ImageView myImageView1 = (ImageView) findViewById(R.id.imageView1);
    Drawable mDrawable = myImageView1.getDrawable();
    Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
    String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image I want to share", null);
    Uri uri = Uri.parse(path);
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
    shareIntent.setType("image/*");
    startActivity(Intent.createChooser(shareIntent, "Share Image"));



}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
        filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);

        cursor.close();

        ImageView imageView = (ImageView) findViewById(R.id.imageView1);

        Bitmap bmp = null;
        try {
        bmp = getBitmapFromUri(selectedImage);
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        imageView.setImageBitmap(bmp);

        }


        }



private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
        getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
        }


        }




