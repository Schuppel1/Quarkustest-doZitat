package org.schuppel.quarkus.doZitat.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.schuppel.quarkus.doZitat.model.AppUsers;
import org.schuppel.quarkus.doZitat.model.Quote;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<AppUsers> {

    public void persist(AppUsers user){
        AppUsers.persist(user);
    };

    public List<AppUsers> getAllUsers() {
        return AppUsers.findAll().list();
    }



}
