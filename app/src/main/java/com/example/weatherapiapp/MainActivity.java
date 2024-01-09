package com.example.weatherapiapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    Button btn_cityID;

    public double temperature;
    public String conditionText;
    public String conditionIcon;
    public int conditionCode;



    private static final String API_KEY = "5ca156cb4f8245ee8d6160815230810";
    public static String LOCATION = "Taiwan"; // 或者你所需的台湾地区

    public void on_CLick_Intent(double temperature, String conditionText, String conditionIcon, int conditionCode){
        Intent intent = new Intent(this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("TEMPERATURE", temperature);
        bundle.putString("CONDITION_TEXT", conditionText);
        bundle.putString("CONDITION_ICON", conditionIcon);
        bundle.putInt("CONDITION_CODE", conditionCode);
        bundle.putString("Location", LOCATION);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    //Weather API
    private void getWeatherDataByCityName(String CityName) {
        // 创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建 WeatherApiService
        WeatherApiService service = retrofit.create(WeatherApiService.class);

        // 执行 API 请求
        Call<WeatherResponse> call = service.getCurrentWeather(API_KEY, CityName);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherData = response.body();
                    // 处理获取的天气数据
                    temperature = weatherData.getCurrent().getTempC();
                    conditionText = weatherData.getCurrent().getCondition().getText();
                    conditionIcon = weatherData.getCurrent().getCondition().getIcon();
                    conditionCode = weatherData.getCurrent().getCondition().getCode();

                    // 在获取到数据后调用 Intent 方法
                    on_CLick_Intent(temperature, conditionText, conditionIcon, conditionCode);
                } else {
                    Toast.makeText(MainActivity.this, "API request failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API request failed. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Button btn = (Button) findViewById(R.id.Location);
        final String[] locations  = {"New Taipei City", "Taipei City", "Taoyuan City","Taichung City","Tainan City","Kaohsiung City"
                ,"Keelung City","Hsinchu City","Miaoli County","Changhua County","Nantou County","Yunlin County","Chiayi City","Pingtung County"
                ,"Yilan County","Hualien County","Taitung County"
        };
        if (which >= 0 && which < locations .length) {
            // 设置按钮文本为对应索引的字符串
            btn.setText(locations[which]);
            LOCATION = locations[which];
        }


    }
    public void button_Click(View view) {
        // 建立對話方塊
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇一個確認");
        // 建立選項陣列
        String[] options = {"New Taipei City", "Taipei City", "Taoyuan City","Taichung City","Tainan City","Kaohsiung City"
                ,"Keelung City","Hsinchu City","Miaoli County","Changhua County","Nantou County","Yunlin County","Chiayi City","Pingtung County"
                ,"Yilan County","Hualien County","Taitung County" };
        builder.setItems(options, this); // 指定選項
        builder.setNegativeButton("取消", null);
        builder.show(); // 顯示對話方塊

    }


    public interface WeatherApiService {
        @GET("current.json")
        Call<WeatherResponse> getCurrentWeather(
                @Query("key") String apiKey,
                @Query("q") String location
        );
    }

    public class WeatherResponse {
        private CurrentWeather current;

        public CurrentWeather getCurrent() {
            return current;
        }
    }

    public class CurrentWeather {
        private double temp_c;
        private Condition condition;

        public double getTempC() {
            return temp_c;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    public class Condition {
        private String text;
        private String icon;
        private int code;

        public String getText() {
            return text;
        }

        public String getIcon() {
            return icon;
        }

        public int getCode() {
            return code;
        }
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign values to each control on layout
        btn_cityID = findViewById(R.id.btn_getCityID);

        //click listeners for each button
        btn_cityID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view ){
                getWeatherDataByCityName(LOCATION);
            }
        });

    }
}