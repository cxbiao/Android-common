package com.bryan.daogenerator;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    private static final String desPath=new File("").getAbsolutePath()+"/app/src/main/java-gen";
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.bryan.greendao");
        //Keep sections 防止覆盖
        schema.enableKeepSectionsByDefault();
        Entity note= schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
        //写绝对路径
        new DaoGenerator().generateAll(schema, desPath);
    }
}
