package com.github.stagirs.lingvo.morpho.model;


/**
 * Created by must on 12.12.2016.
 */
public enum Attr {
    NOUN("имя существительное", Type.POST),
    ADJF("имя прилагательное (полное)", Type.POST),
    ADJS("имя прилагательное (краткое)", Type.POST),
    COMP("компаратив", Type.POST),
    VERB("глагол (личная форма)", Type.POST),
    INFN("глагол (инфинитив)", Type.POST),
    PRTF("причастие (полное)", Type.POST),
    PRTS("причастие (краткое)", Type.POST),
    GRND("деепричастие", Type.POST),
    NUMR("числительное", Type.POST),
    ADVB("наречие", Type.POST),
    NPRO("местоимение-существительное", Type.POST),
    PRED("предикатив", Type.POST),
    PREP("предлог", Type.POST),
    CONJ("союз", Type.POST),
    PRCL("частица", Type.POST),
    INTJ("междометие", Type.POST),
    
    ANim("одушевлённость / одушевлённость не выражена", Type.ANim),
    anim("одушевлённое", Type.ANim),
    inan("неодушевлённое", Type.ANim),
    
    GNdr("род / род не выражен", Type.GNdr),
    masc("мужской род", Type.GNdr),
    femn("женский род", Type.GNdr),
    neut("средний род", Type.GNdr),
    Ms_f("общий род", Type.GNdr),
    
    NMbr("число", Type.NMbr),
    sing("единственное число", Type.NMbr),
    plur("множественное число", Type.NMbr),
    Sgtm("singularia tantum", Type.NMbr),
    Pltm("pluralia tantum", Type.NMbr),
    Fixd("неизменяемое", Type.NMbr),
    
    CAse("категория падежа", Type.CAse),
    nomn("именительный падеж", Type.CAse),
    gent("родительный падеж", Type.CAse),
    datv("дательный падеж", Type.CAse),
    accs("винительный падеж", Type.CAse),
    ablt("творительный падеж", Type.CAse),
    loct("предложный падеж", Type.CAse),
    voct("звательный падеж", Type.CAse),
    gen1("первый родительный падеж", Type.CAse),
    gen2("второй родительный (частичный) падеж", Type.CAse),
    acc2("второй винительный падеж", Type.CAse),
    loc1("первый предложный падеж", Type.CAse),
    loc2("второй предложный (местный) падеж", Type.CAse),
    
    Abbr("аббревиатура", Type.Other),
    Name("имя", Type.Other),
    Surn("фамилия", Type.Other),
    Patr("отчество", Type.Other),
    Geox("топоним", Type.Other),
    Orgn("организация", Type.Other),
    Trad("торговая марка", Type.Other),
    Subx("возможна субстантивация", Type.Other),
    Supr("превосходная степень", Type.Other),
    Qual("качественное", Type.Other),
    Apro("местоименное", Type.Other),
    Anum("порядковое", Type.Other),
    Poss("притяжательное", Type.Other),
    V_ey("форма на -ею", Type.Other),
    V_oy("форма на -ою", Type.Other),
    Cmp2("сравнительная степень на по-", Type.Other),
    V_ej("форма компаратива на -ей", Type.Other),
    ASpc("категория вида", Type.Other),
    perf("совершенный вид", Type.Other),
    impf("несовершенный вид", Type.Other),
    TRns("категория переходности", Type.Other),
    tran("переходный", Type.Other),
    intr("непереходный", Type.Other),
    Impe("безличный", Type.Other),
    Impx("возможно безличное употребление", Type.Other),
    Mult("многократный", Type.Other),
    Refl("возвратный", Type.Other),
    PErs("категория лица", Type.Other),
    N1per("1 лицо", Type.Other),
    N2per("2 лицо", Type.Other),
    N3per("3 лицо", Type.Other),
    TEns("категория времени", Type.Other),
    pres("настоящее время", Type.Other),
    past("прошедшее время", Type.Other),
    futr("будущее время", Type.Other),
    MOod("категория наклонения", Type.Other),
    indc("изъявительное наклонение", Type.Other),
    impr("повелительное наклонение", Type.Other),
    INvl("категория совместности", Type.Other),
    incl("говорящий включён (идем, идемте)", Type.Other),
    excl("говорящий не включён в действие (иди, идите)", Type.Other),
    VOic("категория залога", Type.Other),
    actv("действительный залог", Type.Other),
    pssv("страдательный залог", Type.Other),
    Infr("разговорное", Type.Other),
    Slng("жаргонное", Type.Other),
    Arch("устаревшее", Type.Other),
    Litr("литературный вариант", Type.Other),
    Erro("опечатка", Type.Other),
    Dist("искажение", Type.Other),
    Ques("вопросительное", Type.Other),
    Dmns("указательное", Type.Other),
    Prnt("вводное слово", Type.Other),
    V_be("форма на -ье", Type.Other),
    V_en("форма на -енен", Type.Other),
    V_ie("отчество через -ие-", Type.Other),
    V_bi("форма на -ьи", Type.Other),
    Fimp("деепричастие от глагола несовершенного вида", Type.Other),
    Prdx("может выступать в роли предикатива", Type.Other),
    Coun("счётная форма", Type.Other),
    Coll("собирательное числительное", Type.Other),
    V_sh("деепричастие на -ши", Type.Other),
    Af_p("форма после предлога", Type.Other),
    Inmx("может использоваться как одуш. / неодуш. ", Type.Other),
    Vpre("Вариант предлога ( со, подо, ...)", Type.Other),
    Anph("Анафорическое (местоимение)", Type.Other),
    Init("Инициал", Type.Other),
    Adjx("может выступать в роли прилагательного", Type.Other);

    private final String description;
    private final Type type;

    Attr(String description, Type type) {
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }
}