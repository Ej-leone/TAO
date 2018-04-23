package com.ejleone.finesoftafrika.ezzysacco.Synchronise;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by EJ on 6/5/2016.
 */
public class ParseSoapresponse
{
    /**
     * Parses a single business object containing primitive types from the response
     * @param input soap message, one element at a time
     * @param
     * @return the values parsed
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void parseBusinessObject(String input, Object output) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InstantiationException {


        Class theClass = output.getClass();
        Field[] fields = theClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Type type = fields[i].getType();
            fields[i].setAccessible(true);

            // detect String
            if (fields[i].getType().equals(String.class)) {
                String tag = "" + fields[i].getName() + "";
                if (input.contains(tag)) {
                    String strValue = "";
                    strValue = input.substring(input.indexOf(tag)
                            + tag.length() + 2);

                    if (getValueLength(strValue) > 0) {
                        strValue = getValue(strValue);
                    } else {
                        strValue = "";
                        Log.d("parser",strValue);
                        Log.d("parser",fields[i].getName());
                    }

                    fields[i].set(output, strValue);
                }
            }


        }
    }

    public static String getValue(String substring) {

        String str = new String(substring);

        final Pattern pattern = Pattern.compile("\"(.+?)\"");
        final Matcher matcher = pattern.matcher(str);
        matcher.find();

        return matcher.group(0);
    }

    public static int getValueLength(String substring) {

        final Pattern pattern = Pattern.compile(":(.+?):");
        final Matcher matcher = pattern.matcher(substring);
        matcher.find();
        int count = 0;

        count = Integer.parseInt(matcher.group(1));

        return count;
    }

}


