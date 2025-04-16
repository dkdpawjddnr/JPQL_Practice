package com.hellojpa.jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamA.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m From Member m";

            List<Member> result = em.createQuery(query, Member.class)
                            .getResultList();

            for(Member m : result){
                System.out.println("member = " + m.getUsername() + ", " + m.getTeam().getName());

                //회원1, 팀A(SQL)
                //회원2, 팀A(영속성 컨텍스트 1차캐시)
                //회원3, 팀B(SQL)
                //쿼리가 많이 나가는 것이 N+1 문제!
                // -> 해결 방법? fetch join
            }

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
