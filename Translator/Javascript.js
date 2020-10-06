var ctx;
var lang1, lang2;

function dip2px(ctx, dips) {
    return Math.ceil(dips * ctx.getResources().getDisplayMetrics().density);
}

function onCreate(sis, act) {
    ctx = act;
    var layout = new android.widget.LinearLayout(ctx);
    layout.setOrientation(1);
    var btn1 = new android.widget.Button(ctx);
    btn1.setText("출발 언어 선택");
    btn1.setOnClickListener(new android.view.View.OnClickListener() {
        onClick: function(v) {
            selectLang(btn1, true);
        }
    });
    layout.addView(btn1);
    var btn2 = new android.widget.Button(ctx);
    btn2.setText("도착 언어 선택");
    btn2.setOnClickListener(new android.view.View.OnClickListener() {
        onClick: function(v) {
            selectLang(btn2, false);
        }
    });
    layout.addView(btn2);
    var txt1 = new android.widget.EditText(ctx);
    txt1.setHint("번역할 내용을 입력하세요...");
    layout.addView(txt1);
    var trans = new android.widget.Button(ctx);
    trans.setText("번역");
    trans.setOnClickListener(new android.view.View.OnClickListener() {
        onClick: function(v) {
            var input = txt1.getText();
            android.os.StrictMode.enableDefaults();
            var output = translate(lang1, lang2, input);
            txt2.setText(output);
        }
    });
    layout.addView(trans);
    var txt2 = new android.widget.EditText(ctx);
    txt2.setHint("번역된 내용이 출력되는 곳...");
    layout.addView(txt2);
    var pad = dip2px(ctx, 20);
    layout.setPadding(pad, pad, pad, pad);
    act.setContentView(layout);
}

function selectLang(btn, isInput) {
    try {
        var dialog = new android.app.AlertDialog.Builder(ctx);
        dialog.setTitle("언어 선택");
        var langs = ["한국어", "영어", "일본어", "중국어 (간체)", "중국어 (번체)"];
        var codes = ["ko", "en", "ja", "zh-CN", "zh-TW"];
        dialog.setItems(langs, new android.content.DialogInterface.OnClickListener({
            onClick: function(m, w) {
                if (isInput) {
                    btn.setText("출발 언어 : " + langs[w]);
                    lang1 = codes[w];
                } else {
                    btn.setText("도착 언어 : " + langs[w]);
                    lang2 = codes[w];
                }
            }
        }));
        dialog.setNegativeButton("취소", null);
        dialog.show();
    } catch (e) {
        Log.error(e);
    }
}

function translate(lang1, lang2, value) {
    try {
        var data = org.jsoup.Jsoup.connect("https://openapi.naver.com/v1/papago/n2mt")
            .header("X-Naver-Client-Id", "안알랴줌")
            .header("X-Naver-Client-Secret", "안알랴줌")
            .data("source", lang1)
            .data("target", lang2)
            .data("text", value)
            .ignoreContentType(true)
            .ignoreHttpErrors(true);
        data = data.post().text();
        data = JSON.parse(data);
        return data["message"]["result"]["translatedText"];
    } catch (e) {
        return e;
    }
}

