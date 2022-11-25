package org.schuppel.quarkus.doZitat.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import org.schuppel.quarkus.doZitat.model.Quote;

import javax.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class QuoteRepository implements PanacheRepository<Quote> {

    public void persist(Quote quote) {
        Quote.persist(quote);
    };

    public Quote findById(Long id) {
        return Quote.findById(id);
    }

    public List<Quote> getAllQuotes() {

        // Quelle https://www.baeldung.com/hibernate-select-all
/*        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Quote> cq = cb.createQuery(Quote.class);
        Root<Quote> rootEntry = cq.from(Quote.class);
        CriteriaQuery<Quote> all = cq.select(rootEntry);

        TypedQuery<Quote> allQuery = em.createQuery(all);
        return allQuery.getResultList();*/

        //mit Panache

        return Quote.findAll().list();

    }

    public Quote getQuote(int id) {
        Long longId= Long.valueOf(id);
        return findById(longId);
    }


}
