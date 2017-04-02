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
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
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


        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_vyanjan);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(VyanjanActivity.this, word.getAudioResourceId());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
