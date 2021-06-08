package com.company.enums;

public enum Languages {
    ru_RU("Русский язык"),
    bel_BEL("Беларуская Мова"),
    lit_LIT("Lietùvių Kalbà"),
    en_NZ("English language");

    private String title;

    Languages (String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static String[] getLanguages(){
        String[] languages = new String[Languages.values().length];
        Languages[] languagesEnum = Languages.values();
        for (int i = 0; i < languages.length; i++) {
            languages[i] = languagesEnum[i].getTitle();
        }
        return languages;
    }

    @Override
    public String toString() {
        return title;
    }
}
