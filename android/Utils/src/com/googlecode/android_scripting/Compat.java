package com.googlecode.android_scripting;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shinji on 17/05/06.
 */

public class Compat {
    public static Notification getNotification(
            Context context,
            int iconId,
            String title,
            String message,
            long msec,
            PendingIntent pend
    ) {
        Notification ret;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Use new API
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(pend)
                    .setContentTitle(title)
                    .setContentText(message);
            if (iconId > 0) {
                builder.setSmallIcon(iconId);
            }
            if (msec > 0) {
                builder.setWhen(msec);
            }
            ret = builder.build();
        } else {
            ret = new Notification(iconId > 0 ? iconId : -1, message, msec);
            try {
                Method dep = ret.getClass().getMethod(
                        "setLatestEventInfo", Context.class,
                        CharSequence.class, CharSequence.class, PendingIntent.class
                );
                dep.invoke(ret, context, message, null, pend);
            } catch (NoSuchMethodException | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException e) {
                Log.w("Method not found", e);
            }
        }
        return ret;
    }
}
