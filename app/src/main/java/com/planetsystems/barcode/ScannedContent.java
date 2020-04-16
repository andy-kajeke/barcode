package com.planetsystems.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ScannedContent extends AppCompatActivity {

    TextView content;
    String content_extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_content);
        setTitle("Scanned Content");

        content = findViewById(R.id.scan_content);

        Bundle bundle = getIntent().getExtras();
        content_extra = bundle.getString("scan_content");

        content.setText(content_extra);
    }
}
