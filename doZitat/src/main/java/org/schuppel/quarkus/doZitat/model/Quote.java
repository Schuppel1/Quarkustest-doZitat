package org.schuppel.quarkus.doZitat.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.Instant;


@Entity
public class Quote extends PanacheEntity {

    public String quote;
    public String profName;
    public String course;
    //private DateFormat dateFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public Instant created = Instant.now();
    public String creator;

    public Quote(String quote, String profName, String course, String creator) {
        this.quote = quote;
        this.profName = profName;
        this.course = course;
        this.creator = creator;
    }

    public Quote(){};
}
