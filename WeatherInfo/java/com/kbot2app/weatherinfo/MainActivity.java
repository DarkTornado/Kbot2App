package com.kbot2app.weatherinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        TextView txt1 = new TextView(this);
        txt1.setText("지역 : ");
        txt1.setTextSize(18);
        layout.addView(txt1);

        final EditText txt2 = new EditText(this);
        txt2.setHint("지역을 입력하세요...");
        layout.addView(txt2);

        Button btn = new Button(this);
        btn.setText("날씨 정보 불러오기");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWeather(txt2.getText().toString());
            }
        });
        layout.addView(btn);

        TextView maker = new TextView(this);
        maker.setText("\n© 2020 Dark Tornado, All rights reserved.\n");
        maker.setTextSize(13);
        maker.setGravity(Gravity.CENTER);
        layout.addView(maker);

        int pad = dip2px(20);
        layout.setPadding(pad, pad, pad, pad);
        setContentView(layout);
    }

    private void loadWeather(final String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = getWeatherInfo(input);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result == null) toast("날씨 정보 불러오기 실패");
                        else showDialog(input + " 날씨 정보", result);
                    }
                });
            }
        }).start();
    }

    private String getWeatherInfo(String pos) {
        try {
            String url = "https://m.search.naver.com/search.naver?query=" + pos.replace(" ", "+") + "+날씨";
            Elements data = Jsoup.connect(url).get().select("div.status_wrap");

            String temp = data.select("strong").get(0).text();
            temp = temp.replace("현재 온도", "온도 : ").replace("°", "℃");
            String hum = data.select("li.type_humidity").select("span").get(0).text();
            String state = data.select("div.weather_main").get(0).text();

            Element _dust = data.select("li.sign1").get(0);
            String dust = _dust.select("span.figure_text").text() + " (" + _dust.select("span.figure_result").text() + ")";

            return "상태 : " + state + "\n" + temp + "\n습도 : " + hum + "%\n미세먼지 : " + dust;
        } catch (Exception e) {
            toast(e.toString());
            return null;
        }
    }

    public void showDialog(String title, String msg) {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(title);
            dialog.setMessage(msg);
            dialog.setNegativeButton("닫기", null);
            dialog.show();
        } catch (Exception e) {
            toast(e.toString());
        }
    }

    private void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * this.getResources().getDisplayMetrics().density);
    }


}

