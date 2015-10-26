package com.micocards.cclj.micocards; /*
 * Message.java
 *
 * Version 1
 *
 * 04/03/2015
 *
 * @author Conor, x13343806
 *
 */

import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
