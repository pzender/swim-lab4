package com.example.pzend.swim_lab4;

import java.util.Random;

/**
 * Created by pzend on 12.06.2017.
 */

public class Dice {
    final int SIDES;
    Random rand;
    Dice (int sides){
        SIDES = sides;
        rand = new Random();
    }

    public int roll(){
        return rand.nextInt(SIDES)+1;
    }
}
