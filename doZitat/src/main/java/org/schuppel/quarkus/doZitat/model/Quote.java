package org.schuppel.quarkus.doZitat.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;


@Entity
public class Quote extends PanacheEntity {

    public String quote;
    public String profName;
    public String course;
    public Instant created;

    public Long creatorId;

    public Quote(String quote, String profName, String course, Long creator) {
        this.quote = quote;
        this.profName = profName;
        this.course = course;
        this.creatorId = creator;
    }

    public Quote(){};
}
