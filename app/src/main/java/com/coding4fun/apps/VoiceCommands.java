package com.coding4fun.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import com.coding4fun.utils.SpeechRecognizerManager;

import java.util.ArrayList;

/**
 * Created by coding4fun on 14-May-17.
 */

public class VoiceCommands extends AppCompatActivity /*implements RecognitionListener*/ {

    //private SpeechRecognizer speech;
    private SpeechRecognizerManager mSpeechManager;
    private Intent recognizerIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_commands);
        SwitchCompat voiceCommandsSwitch = (SwitchCompat) findViewById(R.id.voice_commands_switch);
        voiceCommandsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    if(mSpeechManager==null){
                        setSpeechListener();
                    }
                    else if(!mSpeechManager.ismIsListening()){
                        mSpeechManager.destroy();
                        setSpeechListener();
                    }
                } else {
                    if(mSpeechManager != null){
                        mSpeechManager.destroy();
                        mSpeechManager = null;
                    }
                }
            }
        });

        /*speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 20000);*/

        setSpeechListener();

    }

    private void setSpeechListener()
    {
        mSpeechManager = new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {
                if(results!=null && results.size()>0)
                {
                    StringBuilder sb = new StringBuilder();
                    /*if (results.size() > 5) {
                        results = (ArrayList<String>) results.subList(0, 5);
                    }*/
                    for (String result : results) {
                        sb.append(result).append(" ");
                    }
                    Log.e(SpeechRecognizerManager.TAG,"result : " + sb.toString());
                    //if((sb.toString().contains("5") || sb.toString().contains("five")) && sb.toString().contains("minutes"))
                    if(sb.toString().contains("minutes")){
                        String[] ss = sb.toString().split(" ");
                        for(int i=0; i < ss.length-1; i++){
                            if(ss[i+1].equals("minutes")) Log.e(SpeechRecognizerManager.TAG,"FOUND MINS: " + ss[i] + " " + ss[i+1]);
                        }
                    }
                }
                else
                    Log.e(SpeechRecognizerManager.TAG,"no results !!!");
            }
        });
    }

    @Override
    protected void onDestroy() {
        //if(speech != null) speech.destroy();
        if(mSpeechManager != null){
            mSpeechManager.destroy();
            mSpeechManager = null;
        }
        super.onDestroy();
    }

    /*@Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.e("VoiceCommands","onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e("VoiceCommands","onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {
        //Log.e("VoiceCommands","onRmsChanged : " + v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.e("VoiceCommands","onBufferReceived : " + bytes);
    }

    @Override
    public void onEndOfSpeech() {
        Log.e("VoiceCommands","onEndOfSpeech");
    }

    @Override
    public void onError(int i) {
        Log.e("VoiceCommands","onError : " + i + " : " + getErrorText(i));
    }

    @Override
    public void onResults(Bundle bundle) {
        Log.e("VoiceCommands","onResults");
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        Log.e("VoiceCommands","onPartialResults : " + text);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.e("VoiceCommands","onEvent");
    }

    private String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }*/
}