package com.biblebot;

public record BibleVerse(String book, int chapter, int verse, String text) {
    public String reference() {
        return book + " " + chapter + ":" + verse;
    }
}
