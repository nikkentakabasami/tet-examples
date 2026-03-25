package ru.tet.beans;

public class PageTwo implements Page {
    public String getPageViewInfo() {
        return "view two info";
    }

    public String getPageModelInfo() {
        return "model two info";
    }

    public String getPath() {
        return "/pageTwo";
    }
}