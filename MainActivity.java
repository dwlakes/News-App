package com.example.webscrapingtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity<nprArticles> extends AppCompatActivity {
    ImageView img1;
    TextView texx;
    ImageView logo;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img1 = findViewById(R.id.img1);
        texx= findViewById(R.id.tex1);
        logo = findViewById(R.id.logo);

        new doit().execute();

    }

    public class doit extends AsyncTask<Void, Void, Void >{


        String words;
        String nprImageURL;
        String nprStoryLink;
        String logoLink;
        ArrayList<Element> nprImageLinks;
        ArrayList<Elements> nprStoryLinks = new ArrayList<Elements>();
        ArrayList<String> nprStoryDirect= new ArrayList<String>();
        ArrayList<Element> imageLinksEachStory = new ArrayList<Element>();
        ArrayList<String> nprImageDirect= new ArrayList<String>();
        ArrayList<Article> nprArticles = new ArrayList<Article>();
        ArrayList<Button> articleButton = new ArrayList<Button>();

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            Document docForImageLinks = null;
            Elements storyLinks;
            Elements imageLinks = null;


            // Finds NPR site and scrapes it
            try {
                doc = Jsoup.connect("https://www.npr.org/sections/news/").get();

            } catch (IOException e) {
                e.printStackTrace();
            }


            // searches for articles
            Elements elements = doc.getElementsByClass("title");


            //gets href class and extra stuff
            for(Element e : elements){
                nprStoryLinks.add(e.getElementsByAttribute("href"));
            }
            //trims down to just the hyper link to the story
            for (Elements e : nprStoryLinks){
                nprStoryDirect.add(e.attr("href"));

            }
            String imageLink;
            int count = 0;
            //Gets image links for each npr story
            for (String links : nprStoryDirect){
                //System.out.println(links);

                try {
                    docForImageLinks = Jsoup.connect(nprStoryDirect.get(count)).get();
                    count++;


                } catch (IOException e) {
                    e.printStackTrace();
                }
                //gets basically all image links for npr stories
                imageLinks = docForImageLinks.getElementsByAttribute("data-original");
                if(imageLinks.first().toString()==null){
                    System.out.println("found null");
                }

                //gets the first image data for each npr story in array
                imageLinksEachStory.add(imageLinks.first());
            }

            //trims down to just the hyper link to the image
            for (Element e : imageLinksEachStory){
                if (e != null) {
                    nprImageDirect.add(e.attr("data-original"));
                }
            }

            /* Converts the elements and imageLinks arraylists into
            string arrays to be be able to use a random number generator */
            Object nprStoryTextStrings [] = elements.toArray();
            Object nprImageLinkStrings [] = nprImageDirect.toArray();

            for (String e : nprStoryDirect){

            }
            int counter = 0;
            // Creates article objects and stores them into a list
            for(String e : nprStoryDirect){
                Article nprArticle = new Article(nprStoryDirect.get(counter),elements.get(counter).text(),
                        nprImageLinkStrings[counter].toString(), "NPR");
                nprArticles.add(nprArticle);
                counter++;
            }

            /*for (Article a : nprArticles){
                articleButton.add(new Button());
            }*/

            Random r = new Random();
            int newRandomNumber= r.nextInt(nprArticles.size());

            nprStoryLink = nprArticles.get(newRandomNumber).storyLink;
            nprImageURL = nprArticles.get(newRandomNumber).imageUrl;
            words= nprArticles.get(newRandomNumber).storyText;
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Picasso.get().load(nprImageURL).fit().centerCrop().into(img1);
            drawable = getResources().getDrawable(R.drawable.nprlogo);
            logo.setImageDrawable(drawable);

            texx.setText(words);


            Button btn1 = findViewById(R.id.button2);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(nprStoryLink);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                }
            });

        }

    }


}

