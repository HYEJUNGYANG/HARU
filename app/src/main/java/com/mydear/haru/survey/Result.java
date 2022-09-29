package com.mydear.haru.survey;

public class Result {
    private static int group1Result;
    private static int group2Result;
    private static int group3Result;
    private static int group4Result;

    private static boolean group1IsChecked = false;
    private static boolean group2IsChecked = false;
    private static boolean group3IsChecked = false;
    private static boolean group4IsChecked = false;

    public Result() {
    }

    public int getGroup1Result() {
        return group1Result;
    }

    public void setGroup1Result(int group1Result) {
        this.group1Result = group1Result;
    }

    public int getGroup2Result() {
        return group2Result;
    }

    public void setGroup2Result(int group2Result) {
        this.group2Result = group2Result;
    }

    public int getGroup3Result() {
        return group3Result;
    }

    public void setGroup3Result(int group3Result) {
        this.group3Result = group3Result;
    }

    public int getGroup4Result() {
        return group4Result;
    }

    public void setGroup4Result(int group4Result) {
        this.group4Result = group4Result;
    }

    public boolean isGroup1IsChecked() {
        return group1IsChecked;
    }

    public void setGroup1IsChecked(boolean group1IsChecked) {
        Result.group1IsChecked = group1IsChecked;
    }

    public boolean isGroup2IsChecked() {
        return group2IsChecked;
    }

    public void setGroup2IsChecked(boolean group2IsChecked) {
        Result.group2IsChecked = group2IsChecked;
    }

    public boolean isGroup3IsChecked() {
        return group3IsChecked;
    }

    public void setGroup3IsChecked(boolean group3IsChecked) {
        Result.group3IsChecked = group3IsChecked;
    }

    public boolean isGroup4IsChecked() {
        return group4IsChecked;
    }

    public void setGroup4IsChecked(boolean group4IsChecked) {
        Result.group4IsChecked = group4IsChecked;
    }
}
