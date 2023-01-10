package com.petspace.dev.repository;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.PostLogInReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    // @Transactional
    public void save(User user){
        em.persist(user);
    }

    public User findOne(Long id){
        return em.find(User.class, id); // 두번째 인자 pk
    }

    // From 의 대상은 테이블이 아니라 Entity
    public List<User> findAll(){

        return em.createQuery("select m from User m", User.class)
                .getResultList();
    }

    public List<User> findByEmail(String email){
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

}
