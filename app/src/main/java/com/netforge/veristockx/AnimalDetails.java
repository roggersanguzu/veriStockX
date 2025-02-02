package com.netforge.veristockx;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnimalDetails extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText animalName, animalDescription;
    private TextView selectedDateText, animalDescriptionHeader;
    private Button selectDateButton, uploadPhotoButton, saveDetailsButton;
    private ImageView animalPhotoPreview;
    private Spinner animalTypeSpinner, gender;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        // Initialize views
        animalDescriptionHeader = findViewById(R.id.animalDescriptionHeader);
        animalName = findViewById(R.id.animalName);
        animalTypeSpinner = findViewById(R.id.spinner1);
        gender = findViewById(R.id.spinner2);
        animalDescription = findViewById(R.id.animalDescription);
        selectedDateText = findViewById(R.id.selectedDateText);
        selectDateButton = findViewById(R.id.selectDateButton);
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        saveDetailsButton = findViewById(R.id.save);
        animalPhotoPreview = findViewById(R.id.animalPhotoPreview);

        // Setup Spinner for animal types
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.animal_types, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalTypeSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter2);

        // Date Picker
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, month, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            selectedDateText.setText(sdf.format(newDate.getTime()));
                        }
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Photo Upload
        uploadPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Save Details 
        saveDetailsButton.setOnClickListener(v -> {
            String name = animalName.getText().toString();
            String type = animalTypeSpinner.getSelectedItem().toString();
            // Note: Changed from animalSex to gender spinner
            String sex = gender.getSelectedItem().toString();
            String desc = animalDescription.getText().toString();
            String date = selectedDateText.getText().toString();

            if (!name.isEmpty() && !sex.isEmpty() && !desc.isEmpty() && !date.equals("No date selected")) {
                Toast.makeText(this, "Animal details saved!", Toast.LENGTH_SHORT).show();
                // Here you would save the data to a database or file
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                animalPhotoPreview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}