package me.puugz.meetup.util;

import lombok.experimental.UtilityClass;

/**
 * @author puugz
 * @since April 16, 2022
 */
@UtilityClass
public class TimeUtil {

    public String formatSecondsIntoHHMMSS(long secs) {
        final long hours = secs / 3600;
        final long minutes = (secs % 3600) / 60;
        final long seconds = secs % 60;

        final String hh = String.format("%02d", hours);
        final String mm = String.format("%02d", minutes);
        final String ss = String.format("%02d", seconds);

        return (hours > 0 ? hh + ":" : "") + mm + ":" + ss;
    }

    public String formatTimeIntoHMS(int secs) {
        final int hours = secs / 3600;
        final int minutes = (secs % 3600) / 60;
        final int seconds = secs % 60;

        return (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + seconds + "s";
    }

    public String formatSeconds(int secs) {
        int hours = secs / 3600;
        int minutes = (secs % 3600) / 60;
        int seconds = secs % 60;

        final StringBuilder builder = new StringBuilder();

        if (hours > 0)
            builder.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        if (minutes > 0)
            builder.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        if (seconds > 0)
            builder.append(seconds).append(" second").append(seconds > 1 ? "s" : "");

        return builder.toString().trim();
    }
}
