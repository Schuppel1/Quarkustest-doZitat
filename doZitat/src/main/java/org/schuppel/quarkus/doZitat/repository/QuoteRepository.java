package org.schuppel.quarkus.doZitat.repository;

import org.schuppel.quarkus.doZitat.model.Quote;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class QuoteRepository {



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

    public Optional<Quote> getQuote(int id) {
        Long longid= Long.valueOf(id);
        return Optional.ofNullable(findById(longid));
    }
}
