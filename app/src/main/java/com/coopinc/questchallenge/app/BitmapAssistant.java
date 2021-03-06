package com.coopinc.questchallenge.app;

import android.graphics.Bitmap;
import android.view.View;

public class BitmapAssistant {
    public static Bitmap resizeToFit(Bitmap toResize, int fitX, int fitY) {
        int bitY = toResize.getHeight();
        int bitX = toResize.getWidth();
        Bitmap scaledBitmap;
        //This if checks whether the image would overflow the width of the container before the length and then scales the one
        //that would overflow first to the maximum for the fit size given.
        if (bitX/fitX > bitY/fitY) {
            scaledBitmap = Bitmap.createScaledBitmap(toResize, fitX, bitY*fitX/bitX, true);
        } else {
            scaledBitmap = Bitmap.createScaledBitmap(toResize, bitX*fitY/bitY, fitY, true);
        }
        return scaledBitmap;
    }
    public static Bitmap resizeToPixelCount (Bitmap toResize, int totalPixels) {
        double x = toResize.getWidth();
        double y = toResize.getHeight();
        double ratio = x/y;
        x = Math.sqrt(totalPixels/ratio);
        y = x/ratio;
        return Bitmap.createScaledBitmap(toResize, (int) x, (int) y, true);
    }
}
