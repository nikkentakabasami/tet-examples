package ru.tet.beans;

public class PageOne implements Page {
    public String getPageViewInfo() {
        return "view one info";
    }

    public String getPageModelInfo() {
        return "model one info";
    }

    public String getPath() {
        return "/pageOne";
    }
}