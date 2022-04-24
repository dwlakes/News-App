package com.example.webscrapingtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView img1;
    TextView texx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = findViewById(R.id.img1);
        texx= findViewById(R.id.tex1);

        // String url = "https://media.npr.org/assets/img/2022/04/20/ap22092671261813_wide-3fdb74b2dbc1abee2c83d53ed35f8c4865ab9cdd-s600-c85.webp";


        //Button but = findViewById(R.id.but1);

        new doit().execute();

    }

    public class doit extends AsyncTask<Void, Void, Void >{
        String words;
        String imageURL;
        ArrayList<Element> nprImageLinks;

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.npr.org/sections/news/").get();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = doc.getElementsByClass("title");
            /*for (Element e : elements){
                nprImageLinks.add(e).getElementsByAttribute("src");;

            }*/
            Elements imageClass = doc.getElementsByAttribute("data-original");
            System.out.println(imageClass);
            Elements imagesURLS = imageClass.first().getElementsByAttribute("src");
            imageURL = imageClass.get(0).attr("data-original");
            words= elements.get(0).text();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Picasso.get().load(imageURL).into(img1);

            texx.setText(words);

        }
    }
}
