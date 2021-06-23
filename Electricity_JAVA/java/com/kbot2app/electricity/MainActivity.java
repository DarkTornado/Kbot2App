package com.kbot2app.electricity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.enableDefaults();
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        String result = getElectricity();
        TextView txt = new TextView(this);
        txt.setText("\n" + result);
        txt.setTextSize(18);
        txt.setTextColor(android.graphics.Color.BLACK);
        txt.setGravity(android.view.Gravity.CENTER);
        layout.addView(txt);
        setContentView(layout);
    }

    public String getElectricity() {
        try {
            Elements data = Jsoup.connect("https://m.search.naver.com/search.naver?query=전력예비율").get()
                    .select("div.status_box");
            String result = "상태 : " + data.select("span.u_hc").text();
            result += "\n공급예비율 : " + data.select("span.figure").text();
            data = data.select("div.lst_energy").select("span");
            result += "\n공급예비력 : " + data.get(0).text();
            result += "\n공급능력 : " + data.get(1).text();
            result += "\n현재부하 : " + data.get(2).text();
            return result;
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return "정보 불러오기 실패";
    }
}

