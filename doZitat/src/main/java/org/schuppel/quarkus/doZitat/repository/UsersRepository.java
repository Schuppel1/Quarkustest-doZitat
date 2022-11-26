package org.schuppel.quarkus.doZitat.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.schuppel.quarkus.doZitat.model.AppUsers;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class UsersRepository implements PanacheRepository<AppUsers> {

    public void save(AppUsers user){
        persist(user);
    }

    public List<AppUsers> getAllUsers() {
        return findAll().list();
    }

    public boolean userNameIsFree(String userName) {
        for (AppUsers registeredUser : getAllUsers()) {
            if(registeredUser.getName().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    public AppUsers findByName(String userName) {
        for (AppUsers registeredUser : getAllUsers()) {
            if(registeredUser.getName().equals(userName)) {
                return  registeredUser;
            }
        }
        return null;
    }

}
