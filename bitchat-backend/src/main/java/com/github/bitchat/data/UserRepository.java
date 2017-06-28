/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.bitchat.data;

import com.github.bitchat.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/** Repository for User
 * 
 * @author leandrolimadasilva
 *
 */
@ApplicationScoped
public class UserRepository {

    @Inject
    private EntityManager em;

    /** find user by id
     * 
     * @param id
     * @return
     */
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    /** find user by id
     * 
     * @param login
     * @return
     */
    public User findByLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user).where(cb.equal(user.get("login"), login));
        List<User> l = em.createQuery(criteria).getResultList();
        if (l != null && !l.isEmpty()){
            return l.get(0);
        }
        return null;
    }

    /** find user by login and password
     * 
     * @param login
     * @param password
     * @return
     */
    public User findByLoginAndPassword(String login, String password) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user).where(cb.equal(user.get("login"), login), cb.equal(user.get("password"), password));
        List<User> l = em.createQuery(criteria).getResultList();
        if (l != null && !l.isEmpty()){
            return l.get(0);
        }
        return null;
    }

    /** find all users
     * 
     * @return
     */
    public List<User> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user).orderBy(cb.asc(user.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
