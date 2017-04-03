package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Sahajanand Swami on 13-03-2017.
 */

public class VyanjanActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("ka","क",R.drawable.ka,R.raw.ka));
        words.add(new Word("kha", "ख", R.drawable.kha, R.raw.kha));
        words.add(new Word("ga", "ग", R.drawable.ga, R.raw.ga));
        words.add(new Word("gha", "घ", R.drawable.gha, R.raw.gha));
        words.add(new Word("ang", "ङ", R.drawable.ang, R.raw.ang));
        words.add(new Word("cha", "च", R.drawable.cha, R.raw.cha));
        words.add(new Word("chha", "छ", R.drawable.chha, R.raw.chha));
        words.add(new Word("ja", "ज", R.drawable.ja, R.raw.ja));
        words.add(new Word("jha", "झ", R.drawable.jha, R.raw.jha));
        words.add(new Word("nya", "ञ", R.drawable.nya, R.raw.nya));
        words.add(new Word("ta", "ट", R.drawable.ta, R.raw.ta));
        words.add(new Word("tha","ठ",R.drawable.tha,R.raw.tha));
        words.add(new Word("da", "ड", R.drawable.da, R.raw.da));
        words.add(new Word("dha", "ढ", R.drawable.dha, R.raw.dha));
        words.add(new Word("rna", "ण", R.drawable.rna, R.raw.rna));
        words.add(new Word("taa", "त", R.drawable.taa, R.raw.taa));
        words.add(new Word("thaa", "थ", R.drawable.thaa, R.raw.thaa));
        words.add(new Word("daa", "द", R.drawable.daa, R.raw.daa));
        words.add(new Word("dhaa", "ध", R.drawable.dhaa, R.raw.dhaa));
        words.add(new Word("na", "न", R.drawable.na, R.raw.na));
        words.add(new Word("pa", "प", R.drawable.pa, R.raw.pa));
        words.add(new Word("pha", "फ", R.drawable.pha, R.raw.pha));
        words.add(new Word("ba","ब",R.drawable.ba,R.raw.ba));
        words.add(new Word("bha", "भ", R.drawable.bha, R.raw.bha));
        words.add(new Word("ma", "म", R.drawable.ma, R.raw.ma));
        words.add(new Word("ya", "य", R.drawable.ya, R.raw.ya));
        words.add(new Word("ra", "र", R.drawable.ra, R.raw.ra));
        words.add(new Word("la", "ल", R.drawable.la, R.raw.la));
        words.add(new Word("va", "व", R.drawable.va, R.raw.va));
        words.add(new Word("sha", "श", R.drawable.sha, R.raw.sha));
        words.add(new Word("shha", "ष", R.drawable.shha, R.raw.shha));
        words.add(new Word("sa", "स", R.drawable.sa, R.raw.sa));
        words.add(new Word("ha", "ह", R.drawable.ha, R.raw.ha));


        WordAdapter adapter = new WordAdapter(this, words, R.color.category_vyanjan);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                releaseMediaPlayer();

                Word word = words.get(position);

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(VyanjanActivity.this, word.getAudioResourceId());

                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();

            mMediaPlayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
