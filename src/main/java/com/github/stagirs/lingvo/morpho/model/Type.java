package com.github.stagirs.lingvo.morpho.model;

/**
 * Created by must on 12.12.2016.
 */
public enum Type {
    POST("Часть речи"),
    ANim("Одушевлённость"),
    GNdr("Род"),
    NMbr("Число"),
    CAse("Падеж"),
    Abbr("Аббревиатура"),
    ASpc("Вид"),
    TRns("Категория переходности"),
    PErs("Лицо"),
    TEns("Время"),
    MOod("Категория наклонения"),
    INvl("Категория совместности"),
    VOic("Категория залога"),
    Other("Другие характеристики");

    String description;

    Type(String des) {
        description = des;
    }
}