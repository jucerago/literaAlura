package com.alura.literAlura.model;

public enum Idioma {
    ENGLISH("EN","ENGLISH"),
    SPANISH("ES","ESPAÑOL"),
    FRENCH("FR","FRENCH");

    private String abbr;
    private String idioma;

    Idioma(String abbr,String idioma){
        this.abbr=abbr;
        this.idioma=idioma;
    }
    public static Idioma fromString(String text){
        for(Idioma l:Idioma.values()){
            if(l.abbr.equalsIgnoreCase(text) || l.idioma.equalsIgnoreCase(text))
                return l;
        }
        throw new IllegalArgumentException("Idioma no encontrado: "+text);
    }
}
