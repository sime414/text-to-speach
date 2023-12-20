package com.example.languagesound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpeakToTrnslate extends AppCompatActivity {


    CircleImageView english_voive,bagla_voice;
    protected static final int RESULT_SPEECH = 100;

    public static int CLICK_BUTTON = 0;
    RecyclerView recyclerView;

    public static TextToSpeech textToSpeech_bangla,textToSpeech_english;
    public static ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    public static HashMap<String,String> hashMap =new HashMap<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaktotrnslate);

        english_voive = findViewById(R.id.english_voive);
        bagla_voice = findViewById(R.id.bagla_voice);
        recyclerView = findViewById(R.id.recyclerView);

        Sqladapter sqladapter = new Sqladapter(this);
        SQLiteDatabase sqLiteDatabase = sqladapter.getWritableDatabase();


        textToSpeech_bangla = new TextToSpeech(SpeakToTrnslate.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //      Toast.makeText(MainActivity.this, "Text to speech bangla", Toast.LENGTH_SHORT).show();

                    int result = textToSpeech_bangla.setLanguage(new Locale("bn_BD"));
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        //           Toast.makeText(MainActivity.this, "Missing data", Toast.LENGTH_SHORT).show();

                    } else {
                        //           Toast.makeText(MainActivity.this, "all success", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    //        Toast.makeText(MainActivity.this, "Can not suppoty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textToSpeech_english = new TextToSpeech(SpeakToTrnslate.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS){
                    //  Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();

                    int language = textToSpeech_english.setLanguage(new Locale("en_US"));
                    if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(SpeakToTrnslate.this, "Language can not support", Toast.LENGTH_SHORT).show();

                    }else {

                    }

                }else {
                    Toast.makeText(SpeakToTrnslate.this, "Language can not support", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //get data

        Cursor cursor = sqladapter.getData();
        while (cursor.moveToNext()){
            hashMap= new HashMap<>();
            hashMap.put("id", cursor.getString(0));
            hashMap.put("text", cursor.getString(1));
            hashMap.put("ortho",cursor.getString(2));
            hashMap.put("language",cursor.getString(3));
            arrayList.add(hashMap);

        }

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(SpeakToTrnslate.this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        Collections.reverse(arrayList);
        recyclerAdapter.notifyDataSetChanged();







        english_voive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CLICK_BUTTON = 1;

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en_US");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en_US");
                intent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, "en_US");
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en_US");
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "en_US");
                intent.putExtra(RecognizerIntent.EXTRA_RESULTS, "en_US");

                try {

                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(SpeakToTrnslate.this, "Can not support speech to text", Toast.LENGTH_SHORT).show();
                }


            }
        });

        bagla_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CLICK_BUTTON = 2;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, "bn_BD");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"bn_BD");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"bn_BD");
                intent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES,"bn_BD");
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,"bn_BD");
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"bn_BD");
                intent.putExtra(RecognizerIntent.EXTRA_RESULTS,"bn_BD");


                try {

                    startActivityForResult(intent, RESULT_SPEECH );

                }catch (ActivityNotFoundException e ){
                    e.printStackTrace();
                    Toast.makeText(SpeakToTrnslate.this, "Can not support speech to text", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }// oncreate bandil end =================================================================================


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case RESULT_SPEECH:
                    if (requestCode == RESULT_SPEECH && data != null) {
                        ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        //translator ------------------------------------>>>>>>>>>>>>>>>>>>>>>>

                        if (CLICK_BUTTON == 1){

                            TranslateAPI translateAPI = new TranslateAPI(
                                    Language.ENGLISH,
                                    Language.BENGALI,
                                    text.get(0)
                            );
                            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    textToSpeech_bangla.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);

                                    Sqladapter sqladapter = new Sqladapter(SpeakToTrnslate.this);
                                    SQLiteDatabase sqLiteDatabase = sqladapter.getWritableDatabase();

                                    long return_data = sqladapter.insettData(text.get(0),translatedText,"English");
                                    if (return_data ==-1){
                                        Toast.makeText(SpeakToTrnslate.this, "data can not insert", Toast.LENGTH_SHORT).show();
                                    }else {

                                        arrayList= new ArrayList<>();
                                        Cursor cursor = sqladapter.getData();
                                        while (cursor.moveToNext()){
                                            hashMap= new HashMap<>();
                                            hashMap.put("id", cursor.getString(0));
                                            hashMap.put("text", cursor.getString(1));
                                            hashMap.put("ortho",cursor.getString(2));
                                            hashMap.put("language",cursor.getString(3));
                                            arrayList.add(hashMap);

                                        }

                                        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
                                        recyclerView.setAdapter(recyclerAdapter);
                                        LinearLayoutManager manager = new LinearLayoutManager(SpeakToTrnslate.this);
                                        manager.setReverseLayout(true);
                                        recyclerView.setLayoutManager(manager);
                                        Collections.reverse(arrayList);
                                        recyclerAdapter.notifyDataSetChanged();

                                    }

                                }

                                @Override
                                public void onFailure(String ErrorText) {

                                    Toast.makeText(SpeakToTrnslate.this, "fall", Toast.LENGTH_SHORT).show();
                                }
                            });




                        }else {

                            TranslateAPI translateAPI = new TranslateAPI(
                                    Language.BENGALI,
                                    Language.ENGLISH,
                                    text.get(0)
                            );
                            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    textToSpeech_english.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);

                                    Sqladapter sqladapter = new Sqladapter(SpeakToTrnslate.this);
                                    SQLiteDatabase sqLiteDatabase = sqladapter.getWritableDatabase();

                                    long return_data = sqladapter.insettData(text.get(0),translatedText,"Bangla");
                                    if (return_data ==-1){
                                        Toast.makeText(SpeakToTrnslate.this, "data can not insert", Toast.LENGTH_SHORT).show();
                                    }else {

                                        arrayList= new ArrayList<>();
                                        Cursor cursor = sqladapter.getData();
                                        while (cursor.moveToNext()){
                                            hashMap= new HashMap<>();
                                            hashMap.put("id", cursor.getString(0));
                                            hashMap.put("text", cursor.getString(1));
                                            hashMap.put("ortho",cursor.getString(2));
                                            hashMap.put("language",cursor.getString(3));
                                            arrayList.add(hashMap);

                                        }

                                        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
                                        recyclerView.setAdapter(recyclerAdapter);
                                        LinearLayoutManager manager = new LinearLayoutManager(SpeakToTrnslate.this);
                                        manager.setReverseLayout(true);
                                        recyclerView.setLayoutManager(manager);
                                        Collections.reverse(arrayList);
                                        recyclerAdapter.notifyDataSetChanged();

                                    }
                                }

                                @Override
                                public void onFailure(String ErrorText) {

                                    Toast.makeText(SpeakToTrnslate.this, "fall", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }




                        //translator ------------------------------------<<<<<<<<<<<<<<<<<<<<<<

                    }
            }


        }


}