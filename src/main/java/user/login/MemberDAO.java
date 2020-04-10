package user.login;

import models.Member;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RequestScoped
public class MemberDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Member addMember(Member member){
        entityManager.persist(member);
        return member;
    }

}
