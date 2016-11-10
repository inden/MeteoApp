package com.in.den.newsunshine;

import com.in.den.newsunshine.sync.OpenWeatherService;

import java.io.IOException;

/**
 * Created by harumi on 19/10/2016.
 */

public class TestWithMain {

    public static void main(String[] args) {
        try {
            String s = OpenWeatherService.queryServer("paris");
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
