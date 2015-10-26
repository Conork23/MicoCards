package com.micocards.cclj.micocards;
/*
 * Audio.java
 *
 * Version 1
 *
 * 30/03/2015
 *
 * @author Conor, x13343806
 *
 * This application uses these sounds from freesound:
 * pop 4 by esperri ( http://www.freesound.org/people/esperri/ ),
 * Book Turn Page 2 by greenvwbeetle ( http://www.freesound.org/people/greenvwbeetle/ )
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Audio {
    SoundPool sounds;
    int popSound, pageTurn;


    public Audio() {

        sounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);


    }

    public void loadSounds(Context c) {

        popSound = sounds.load(c, R.raw.pop, 1);
        pageTurn = sounds.load(c, R.raw.page_turn, 1);


    }


    public void playPop() {
        sounds.play(popSound, 1, 1, 1, 0, 1);
    }

    public void playPageTurn() {
        sounds.play(pageTurn, 1, 1, 1, 0, 2);
    }
}
