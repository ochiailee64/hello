package com.example.csaper6.finalp;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by csaper6 on 5/19/17.
 */

@JsonObject
public class Message{

    /*
     * Annotate a field that you want sent with the @JsonField marker.
     */
    @JsonField
    public String description;


    /*
     * Note that since this field isn't annotated as a
     * @JsonField, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    public int nonJsonField;
}
