package com.example.listugas;

public class ExampleModel {

    private final long mId;
    private final String mText;

    public ExampleModel(long id, String text) {
        mId = id;
        mText = text;
    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }
}
