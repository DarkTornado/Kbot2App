package com.kbot2app.electricity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.jsoup.Jsoup


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.enableDefaults()
        val layout = LinearLayout(this)
        layout.orientation = 1
        val result: String = getElectricity()
        val txt = TextView(this)
        txt.text = "\n$result"
        txt.textSize = 18f
        txt.setTextColor(Color.BLACK)
        txt.gravity = Gravity.CENTER
        layout.addView(txt)
        setContentView(layout)
    }

    fun getElectricity(): String {
        try {
            var data = Jsoup.connect("https://m.search.naver.com/search.naver?query=전력예비율").get()
                    .select("div.status_box")
            var result = "상태 : " + data.select("span.u_hc").text()
            result += "\n공급예비율 : " + data.select("span.figure").text()
            data = data.select("div.lst_energy").select("span")
            result += "\n공급예비력 : " + data.get(0).text()
            result += "\n공급능력 : " + data.get(1).text()
            result += "\n현재부하 : " + data.get(2).text()
            return result
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
        return "정보 불러오기 실패"
    }
}

