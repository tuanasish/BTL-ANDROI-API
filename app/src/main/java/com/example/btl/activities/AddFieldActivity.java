package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFieldActivity extends AppCompatActivity {

    private EditText fieldName, fieldLocation, fieldPrice, fieldCapacity, fieldType;
    private ImageView fieldImage;
    private Button btnAddField, btnSelectImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Spinner fieldTypeSpinner;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        // Ánh xạ
        fieldName = findViewById(R.id.fieldName);
        fieldLocation = findViewById(R.id.fieldLocation);
        fieldPrice = findViewById(R.id.fieldPrice);
        fieldCapacity = findViewById(R.id.fieldCapacity);
        fieldTypeSpinner = findViewById(R.id.fieldTypeSpinner);


        fieldTypeSpinner = findViewById(R.id.fieldTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.field_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldTypeSpinner.setAdapter(adapter);

        fieldImage = findViewById(R.id.fieldImage);
        btnAddField = findViewById(R.id.btnAddField);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnAddField.setOnClickListener(v -> {
            if (validateInputFields()) {
                String name = fieldName.getText().toString();
                String location = fieldLocation.getText().toString();
                String type = fieldTypeSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(fieldPrice.getText().toString());
                int capacity = Integer.parseInt(fieldCapacity.getText().toString());

                addFieldToServer(name, location, type, price, capacity, imageUri);
            }
        });
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        }

    }


    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private boolean validateInputFields() {
        boolean isValid = true;

        if (fieldName.getText().toString().trim().isEmpty()) {
            fieldName.setError("Nhập tên sân");
            isValid = false;
        }
        if (fieldLocation.getText().toString().trim().isEmpty()) {
            fieldLocation.setError("Nhập địa chỉ");
            isValid = false;
        }
        if (fieldPrice.getText().toString().trim().isEmpty()) {
            fieldPrice.setError("Nhập giá");
            isValid = false;
        }
        if (fieldCapacity.getText().toString().trim().isEmpty()) {
            fieldCapacity.setError("Nhập sức chứa");
            isValid = false;
        }
        if (fieldTypeSpinner.getSelectedItem() == null || fieldTypeSpinner.getSelectedItem().toString().trim().isEmpty()) {
            Toast.makeText(this, "Chọn loại sân!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Chọn ảnh sân!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void openFileChooser() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("AddField", " URI ảnh đã chọn: " + imageUri.toString());
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                fieldImage.setImageBitmap(bitmap);
                inputStream.close();
            } catch (IOException e) {
                Toast.makeText(this, "Không thể hiển thị ảnh", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public File getFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("upload", ".jpg", getCacheDir());
        tempFile.deleteOnExit();

        OutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();
        return tempFile;
    }

    private void addFieldToServer(String name, String location, String type, double price, int capacity, Uri imageUri) {
        Log.d("AddField", "Uploading field: " + name + ", type: " + type + ", imageUri: " + imageUri);

        RequestBody nameRequest = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody locationRequest = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody typeRequest = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody priceRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody capacityRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(capacity));

        try {
            File imageFile = getFileFromUri(imageUri);
            RequestBody imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequest);

            ApiFieldInterface apiService = ApiClient.getClient().create(ApiFieldInterface.class);
            Call<ResponseBody> call = apiService.addField(nameRequest, locationRequest, typeRequest, priceRequest, capacityRequest, imagePart);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("AddField", "Server response: " + response.code());
                    if (response.isSuccessful()) {
                        Toast.makeText(AddFieldActivity.this, "Sân đã được thêm!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddFieldActivity.this, "Lỗi server: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AddFieldActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("AddField", "Kết nối thất bại", t);
                }
            });

        } catch (IOException e) {
            Log.e("AddField", "Không đọc được ảnh", e);
            Toast.makeText(this, "Không thể đọc ảnh", Toast.LENGTH_SHORT).show();
        }
    }


}
