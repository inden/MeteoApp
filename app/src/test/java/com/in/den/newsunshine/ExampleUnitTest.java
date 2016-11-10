package com.in.den.newsunshine;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void formatdate() throws Exception {
       long l = Calendar.getInstance().getTime().getTime();
        SunshineUtil.formatLongToDate(l);
    }

   // @Test
    public void string2Json() throws Exception {
        Gson gson = new Gson();

        ArrayList<String> cities = new ArrayList<String>();
        cities.add("Paris");
        cities.add("New York");
        cities.add("Rome");

        String s = gson.toJson(cities);

       // assertEquals(s, "abc");


        ArrayList<String> l = gson.fromJson(s, ArrayList.class);

        assertEquals(l.size(), 3);


        ArrayList<String> cities2 = new ArrayList<String>();


        String s2 = gson.toJson(cities2);

        // assertEquals(s, "abc");


        ArrayList<String> l2 = gson.fromJson(s2, ArrayList.class);

        assertNotEquals(l2.size(), 0);


    }
}