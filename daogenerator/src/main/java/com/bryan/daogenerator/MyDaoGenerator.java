package com.bryan.daogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import java.io.File;



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
