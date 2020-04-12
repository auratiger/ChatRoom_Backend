package user.administrator;

import models.Administrator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RequestScoped
public class AdministratorDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Administrator addAdministrator(Administrator newAdministrator){
        entityManager.persist(newAdministrator);
        return newAdministrator;
    }

}
