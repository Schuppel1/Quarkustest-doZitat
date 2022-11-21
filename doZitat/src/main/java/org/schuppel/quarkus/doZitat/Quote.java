package org.schuppel.quarkus.doZitat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Quote {

    public int id;
    public String quote;
    public String profName;
    public String course;
    //private DateFormat dateFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public Date created;
    public String creator;

    public Quote(int id, String quote, String profName, String course, String creator) {
        this.id = id;
        this.quote = quote;
        this.profName = profName;
        this.course = course;
        this.creator = creator;
        this.created = new Date();
        //System.out.println(dateFormat.format(created));
    }
}
