module voidbreaker.prolog {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.sql;
    requires javafx.swing;
    requires webcam.capture;
    uses java.sql.Driver;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.zaxxer.hikari;
    requires json.simple;
    requires org.json;
    requires java.management;
    requires jdk.management;


    opens voidbreaker.prolog to javafx.fxml;
    exports voidbreaker.prolog;
}