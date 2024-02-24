package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // JPQL
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o From Order o join o.member m";
        List<String> criteria = new ArrayList<>();
        if (orderSearch.getOrderStatus() != null) {
            criteria.add("o.status = :status");
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            criteria.add("m.name like :name");
        }
        if (!criteria.isEmpty()) {
            jpql += " where " + String.join(" and ", criteria);
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class);
        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query.setParameter("name", "%" + orderSearch.getMemberName() + "%");
        }
        return query.getResultList();
    }
}