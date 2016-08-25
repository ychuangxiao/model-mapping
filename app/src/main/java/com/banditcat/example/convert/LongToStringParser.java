package com.banditcat.example.convert;


import com.banditcat.android.transformer.parser.AbstractParser;

/**
 * Created by banditcat on 15/8/17 09:06.
 */
public class LongToStringParser extends AbstractParser<Long, String> {


    private static LongToStringParser instance;

    private LongToStringParser() {
    }

    public static LongToStringParser getInstance() {
        if (instance == null) {
            instance = new LongToStringParser();
        }
        return instance;
    }

    @Override
    public String onParse(Long value) {




        if(null != value && value>0L)
        {
            return Long.toString(value);
        }
        else
        {
            return "";
        }

    }
}

