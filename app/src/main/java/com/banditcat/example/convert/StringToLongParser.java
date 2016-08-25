package com.banditcat.example.convert;


import android.text.TextUtils;

import com.banditcat.android.transformer.parser.AbstractParser;

/**
 * Created by banditcat on 15/8/17 09:06.
 */
public class StringToLongParser extends AbstractParser<String, Long> {


    private static StringToLongParser instance;

    private StringToLongParser() {
    }

    public static StringToLongParser getInstance() {
        if (instance == null) {
            instance = new StringToLongParser();
        }
        return instance;
    }

    @Override
    public Long onParse(String value) {


        if (TextUtils.isEmpty(value)) {

            return Long.parseLong(value);
        } else {
            return 0L;
        }

    }
}

