package com.sunrun.sunrunframwork.http.quest;

import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

/**
 * Created by cnsunrun on 2016/11/25.
 */

public interface Header {
    String getName();

    String getValue();

    HeaderElement[] getElements() throws ParseException;
}
