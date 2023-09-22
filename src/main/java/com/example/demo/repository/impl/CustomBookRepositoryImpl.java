package com.example.demo.repository.impl;

import com.example.demo.common.BookStatus;
import com.example.demo.exception.BadRequestResponseException;
import com.example.demo.model.BookCriteria;
import com.example.demo.entity.Book;
import com.example.demo.repository.CustomBookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CustomBookRepositoryImpl implements CustomBookRepository {

    @Autowired
    private EntityManager entityManager;
    @Override
    public List<Book> findByCriteria(BookCriteria bookCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();

        if (bookCriteria.getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), bookCriteria.getId()));
        }
        if (bookCriteria.getIsbn() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isbn"), bookCriteria.getIsbn()));
        }
        if (bookCriteria.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), Enum.valueOf(BookStatus.class, bookCriteria.getStatus())));
        }
        if (bookCriteria.getKey() != null) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%"+bookCriteria.getKey()+"%"));
            predicates.add(criteriaBuilder.like(root.get("author"), "%"+bookCriteria.getKey()+"%"));
        } else {
            if (bookCriteria.getTitle() != null ||
                    (bookCriteria.getTitle() == null && bookCriteria.getAuthor() == null)) {
                predicates.add(criteriaBuilder.
                        like(root.get("title"), "%"+(bookCriteria.getTitle() == null ? "" : bookCriteria.getTitle())+"%"));
                if (bookCriteria.getOrder() != null) {
                    criteriaQuery.select(root);
                    if (bookCriteria.getOrder().equals("asc")) {
                        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("title")));
                    } else if (bookCriteria.getOrder().equals("desc")) {
                        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("title")));
                    } else {
                        throw new BadRequestResponseException("Invalid criteria field: 501");
                    }
                }
            }
            if (bookCriteria.getAuthor() != null) {
                predicates.add(criteriaBuilder.like(root.get("author"), "%"+bookCriteria.getAuthor()+"%"));
                if (bookCriteria.getOrder() != null) {
                    criteriaQuery.select(root);
                    if (bookCriteria.getOrder().equals("asc")) {
                        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("author")));
                    } else if (bookCriteria.getOrder().equals("desc")) {
                        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("author")));
                    } else {
                        throw new BadRequestResponseException("Invalid criteria field: 501");
                    }
                }
            }
        }

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
