package com.banditcat.example.convert;


import com.banditcat.android.transformer.parser.AbstractParser;

/**
 * Created by banditcat on 15/8/17 09:06.
 */
public class LongToLongParser extends AbstractParser<Long, Long> {

    private static LongToLongParser instance;

    private LongToLongParser() {
    }

    public static LongToLongParser getInstance() {
        if (instance == null) {
            instance = new LongToLongParser();
        }
        return instance;
    }


    @Override
    protected Long onParse(Long value) {


        return (null != value && value > 0L) ? value : 0L;


    }
}

