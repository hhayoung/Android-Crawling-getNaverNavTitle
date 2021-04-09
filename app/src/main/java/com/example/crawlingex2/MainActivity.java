package com.example.crawlingex2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * 네이버 사이트 nav_item 가져오기
 * (메일 카페 블로그 지식인 쇼핑 페이 티비 사전 뉴스 증권 부동산 지도 영화 vibe 책 웹툰)
*/

public class MainActivity extends AppCompatActivity {
    String cafeName = "";
    TextView cafe_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cafe_name = (TextView) findViewById(R.id.cafe_name);
        final Bundle bundle = new Bundle();

        new Thread(){
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.naver.com").get();

                    // 브라우저 전체 html 내용 확인
                    // doc.html();
//                    Log.e("doc", doc.html());

                    Elements contents = doc.select("#NM_FAVORITE > div.group_nav");

//                    Log.e("contents", contents.html());

                    /**
                     ✔ 메일
                     #NM_FAVORITE > div.group_nav > ul.list_nav.type_fix > li:nth-child(1) > a > i

                     ✔ 카페 ~ 페이
                     #NM_FAVORITE > div.group_nav > ul.list_nav.type_fix > li:nth-child(2) > a
                     ...
                     #NM_FAVORITE > div.group_nav > ul.list_nav.type_fix > li:nth-child(6) > a

                     ✔ TV
                     #NM_FAVORITE > div.group_nav > ul.list_nav.type_fix > li:nth-child(7) > a > i

                     ✔ 사전 ~ 웹툰
                     #NM_FAVORITE > div.group_nav > ul.list_nav.NM_FAVORITE_LIST > li:nth-child(1) > a
                     #NM_FAVORITE > div.group_nav > ul.list_nav.NM_FAVORITE_LIST > li:nth-child(2) > a
                     ...
                     #NM_FAVORITE > div.group_nav > ul.list_nav.NM_FAVORITE_LIST > li:nth-child(9) > a
                     */

                    for(Element e : contents) {
                        // 데이터가 있는지 없는지 체크할 때 방법 2가지
                        // 방법1 : isEmpty() 함수 사용하기
                        if(!e.select("ul.list_nav.type_fix").isEmpty()) {
                            for(int i=1;i<=7;i++) {
                                cafeName += e.select("ul.list_nav.type_fix > li:nth-child("+i+") > a").text();
                                /*
                                    cafeName += e.select("li:nth-child("+i+") > a").text(); 라고 해버리면
                                    밑에 if 문을 통해 가져올 데이터의 css도 동일하기 때문에 순서가 뒤죽박죽 되어버림.
                                */

                            }
                        }
                        // 방법2 : size() 함수 사용하기
                        if(!(e.select("ul.list_nav.NM_FAVORITE_LIST").size() == 0)) {
                            for(int i=1;i<=9;i++) {
                                cafeName += e.select("ul.list_nav.NM_FAVORITE_LIST > li:nth-child("+i+") > a").text();
                            }
                        }
                    }


                    bundle.putString("cafe", cafeName);
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            cafe_name.setText(bundle.getString("cafe"));
        }
    };
}