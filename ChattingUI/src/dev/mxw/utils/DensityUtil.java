package dev.mxw.utils;

import android.content.res.Resources;

public class DensityUtil {  
    
    public static float fromDpToPx(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }
    
} 
