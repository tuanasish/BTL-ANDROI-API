package com.example.btl.activities;

import android.Manifest;
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
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.btl.R;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.models.Field;

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

public class EditFieldActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 101;
    private static final int PICK_IMAGE_REQUEST = 102;

    private ImageView fieldImage;
    private Uri imageUri;

    private EditText fieldName, fieldLocation, fieldPrice, fieldCapacity;
    private Spinner fieldTypeSpinner;
    private Button btnSelectImage, btnUpdateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        Field field = (Field) getIntent().getSerializableExtra("FIELD");
        fieldImage = findViewById(R.id.fieldImage);
        fieldName = findViewById(R.id.fieldName);
        fieldLocation = findViewById(R.id.fieldLocation);
        fieldPrice = findViewById(R.id.fieldPrice);
        fieldCapacity = findViewById(R.id.fieldCapacity);
        fieldTypeSpinner = findViewById(R.id.fieldTypeSpinner);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUpdateField = findViewById(R.id.btnSaveChanges);
        fieldName.setText(field.getName());
        fieldLocation.setText(field.getLocation());
        fieldPrice.setText(String.valueOf(field.getPrice()));
        fieldCapacity.setText(String.valueOf(field.getCapacity()));
        // Spinner loại sân
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.field_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldTypeSpinner.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> checkAndRequestPermission());

        btnUpdateField.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = fieldName.getText().toString();
                String location = fieldLocation.getText().toString();
                String type = fieldTypeSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(fieldPrice.getText().toString());
                int capacity = Integer.parseInt(fieldCapacity.getText().toString());

                RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
                RequestBody locationBody = RequestBody.create(MediaType.parse("text/plain"), location);
                RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), type);
                RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
                RequestBody capacityBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(capacity));

                MultipartBody.Part imagePart = null;
                if (imageUri != null) {
                    try {
                        File imageFile = getFileFromUri(imageUri);
                        RequestBody imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile);
                        imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequest);
                    } catch (IOException e) {
                        Toast.makeText(this, "Không thể đọc ảnh", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiFieldInterface api = ApiClient.getClient().create(ApiFieldInterface.class);
                Call<ResponseBody> call = api.updateFieldToServer(field.getField_id(), nameBody, locationBody, typeBody, priceBody, capacityBody, imagePart);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditFieldActivity.this, "Cập nhật sân thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditFieldActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditFieldActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("EditField", "API thất bại", t);
                    }
                });
            }
        });

    }

    private void checkAndRequestPermission() {
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION);
            }
        }
    }

    private void openGallery() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Log.d("EditFieldActivity", "Ảnh được chọn: " + imageUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                fieldImage.setImageBitmap(bitmap);
                inputStream.close();
            } catch (IOException e) {
                Toast.makeText(this, "Không thể hiển thị ảnh", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Bạn cần cấp quyền truy cập ảnh!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (fieldName.getText().toString().trim().isEmpty()) {
            fieldName.setError("Nhập tên sân");
            isValid = false;
        }
        if (fieldLocation.getText().toString().trim().isEmpty()) {
            fieldLocation.setError("Nhập địa chỉ sân");
            isValid = false;
        }
        if (fieldPrice.getText().toString().trim().isEmpty()) {
            fieldPrice.setError("Nhập giá sân");
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

        return isValid;
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
}
