package com.hellojpa.jpql;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 반환 타입이 명확할 때 : TypeQuery
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            // 이렇게 하면 반환 타입이 두 개라서 명확하지 않음 -> Query타입으로 반환 받아야 함.
            Query query2 = em.createQuery("select m.username, m.age from Member m");
            
            // 결과가 하나 이상일 때, 리스트 반환 결과가 없으면 빈 리스트 반환
            List<Member> resultList = query1.getResultList();
            // 결과가 정확히 하나, 단일 객체 반환 결과가 없거나 둘 이상이면 Exception 발생
            Member singleResult = query1.getSingleResult();
            
            System.out.println(singleResult);

            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
