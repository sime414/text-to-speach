package com.example.languagesound;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text,ortho,language_text,language_ortho;
        ImageView flag_text,flag_ortho,sound,copy;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            ortho = itemView.findViewById(R.id.ortho);
            language_text = itemView.findViewById(R.id.language_text);
            language_ortho = itemView.findViewById(R.id.language_ortho);
            flag_text = itemView.findViewById(R.id.flag_text);
            flag_ortho = itemView.findViewById(R.id.flag_ortho);
            sound = itemView.findViewById(R.id.sound);
            copy = itemView.findViewById(R.id.copy);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View my_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlayout,parent,false);


        return new ViewHolder(my_view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            HashMap<String,String> hashMap = SpeakToTrnslate.arrayList.get(position);

            String stext = hashMap.get("text");
            String sortho = hashMap.get("ortho");
            String slanguage = hashMap.get("language");

            holder.text.setText(stext);
            holder.ortho.setText(sortho);

            if (slanguage.contains("English")){
                holder.language_text.setText("English");
                holder.flag_text.setImageResource(R.drawable.usa);
                holder.language_ortho.setText("Bangla");
                holder.flag_ortho.setImageResource(R.drawable.bangladesh);
            }else {
                holder.language_text.setText("Bangla");
                holder.flag_text.setImageResource(R.drawable.bangladesh);
                holder.language_ortho.setText("English");
                holder.flag_ortho.setImageResource(R.drawable.usa);
            }


            holder.sound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (slanguage.contains("English")){
                        String text = holder.ortho.getText().toString();

                        SpeakToTrnslate.textToSpeech_bangla.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

                    }else {
                        String text = holder.ortho.getText().toString();

                        SpeakToTrnslate.textToSpeech_english.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

                    }


                }
            });


            holder.copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  text = holder.ortho.getText().toString();
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Copied Text",text);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, "Copy", Toast.LENGTH_SHORT).show();



                }
            });





    }



    @Override
    public int getItemCount() {
        return SpeakToTrnslate.arrayList.size();
    }


}
