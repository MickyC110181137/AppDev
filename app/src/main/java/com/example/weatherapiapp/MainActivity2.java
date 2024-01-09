package com.example.weatherapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MainActivity2 extends AppCompatActivity {
    TextView TempC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TempC = findViewById(R.id.TempC);
        UiLayOut();

        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回第一页
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void UiLayOut(){
        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView2);
        TextView btnConditionText = findViewById(R.id.btnConditionText);
        Intent intent = getIntent();

        if (intent != null) {
            double temperature = intent.getDoubleExtra("TEMPERATURE", 0.0);
            String conditionText = intent.getStringExtra("CONDITION_TEXT");
            String conditionIcon = intent.getStringExtra("CONDITION_ICON");
            int conditionCode = intent.getIntExtra("CONDITION_CODE", 0);
            String location = intent.getStringExtra("Location");

            String[] parts = conditionIcon.split("/");
            String fileNameWithExtension = parts[parts.length - 1]; // 得到 "116.png"
            // 分割文件名和擴展名
            String[] fileNameParts = fileNameWithExtension.split("\\.");
            String fileNameWithoutExtension = fileNameParts[0]; // 得到 "116"
            // 构建图片路径
            String imagePath = "file:///android_asset/64x64/day/" + fileNameWithoutExtension + ".png";
            // 加载图片
            Picasso.get().load(imagePath).into(imageView);

            String TemperatureString = String.valueOf(temperature);

            String TempCC = TemperatureString +"°C";
            // 使用获取到的值做进一步的处理或显示在 UI 上
            TempC.setText(TempCC);
            textView.setText(fileNameWithoutExtension);
            btnConditionText.setText(conditionText);
        }
    }
}