package com.example.service

import com.example.domain.User
import com.example.repository.UserRepository
import com.example.service.security.BCryptPasswordEncoderService
import groovy.util.logging.Slf4j
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import jakarta.inject.Singleton

import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

/**
 * This is the UserService class. All actions over User should be done via this
 * Service. This takes care of password encoding and other facilities
 */
@Singleton
@Transactional
@Slf4j
class UserService {
    // the UserRepository. This is the low level access to the User table
    UserRepository userRepository
    // EntityManager to control the optimistic locking
    EntityManager entityManager

    // The password encoder
    BCryptPasswordEncoderService passwordEconder = new BCryptPasswordEncoderService()

    UserService(UserRepository userRepository, EntityManager entityManager){
        this.userRepository = userRepository
        this.entityManager = entityManager
    }

    /**
     * This method gets the user by Id
     * @param id is the id of the user
     * @return the user from the DB. Otherwise, it returns null
     */
    User getUser(Long id, Boolean detached=true) {
        def user = entityManager.find(User.class, id, LockModeType.NONE)
        if(!user) return null
        if(detached) entityManager.detach(user)
        user
    }

    /**
     * This method finds a user from the username
     * @return the user from the DB. Otherwise, it returns null
     */
    User findByUsername(String username) {
        try{
            userRepository.findOneByUsername(username)
        } catch (Exception e) {
            null
        }
    }

    /**
     * This method returns users using the start index and the end index
     * @param page is the page number
     * @param size is the size of the page
     * @return a Page of users. This page can continue if needed
     */
    List<User> getUsers(int page, int size){
        // if we need to order the page by a single field, we can do this:
        // def sortOrder = Sort.Order.asc("id")
        // Sort sort = Sort.of(sortOrder)
        // def pages = Pageable.from(page, size, sort)

        // Using multiple order fields requires a list of Sort.Order
        // creating sorting order for the query
        List<Sort.Order> orders = new ArrayList<Sort.Order>()
        // as an example, we are sorting by id
        orders << new Sort.Order("id", Sort.Order.Direction.ASC, true)
        // now create the pageable object
        def pages = Pageable.from(page, size, Sort.of(orders))
        def sliceOfUsers = userRepository.list(pages)

        if(sliceOfUsers == null) return []
        sliceOfUsers.getContent()
    }

    /**
     * Private method to save the properties for a User
     * @param user the user to add the new property to
     * @param props the properties that are added on top of User
     * @return the User entity with the new and old properties
     */
    private User dealWithProps(User user, Map props){
        props.each{ k, v ->
            if(k == "password"){
                v = passwordEconder.encode(v as String)
            }
            user.setProperty(k,v)
        }
        user
    }

    /**
     * Method to save a user with all the necessary properties
     * @param props the properties that are added on top of User
     * @param detached if the user should be detached from the EntityManager
     * @return the user that is saved in the DB
     */
    User saveUser(Map props, Boolean detached=true){
        User user = dealWithProps(new User(), props)
        entityManager.persist(user)
        // flush the value
        entityManager.flush()
        if(detached) entityManager.detach(user)
        user
    }

    /**
     * This method updates a user with new properties
     * @param id the ID of the user to update
     * @param props the property that are updated
     * @param detached if the user should be detached from the EntityManager
     * @return the User information
     */
    User updateUser(Long id, Map props, Boolean detached=true){
        def entity = userRepository.findById(id)

        if(!entity.isPresent()) return null
        if(!props) return entity.get()

        def user = dealWithProps(entity.get(), props)

        //userRepository.update(user) // Does NOT work for version
        // user EntityManager instead; so that optimistic locking can happen
        entityManager.persist(user)
        entityManager.flush()

        // flush the value
        entityManager.flush()

        // detached the user from the EntityManager context
        if(detached) entityManager.detach(user)

        // return it back after save
        userRepository.findById(id).get()
    }

    /**
     * Delete user method based on the user id. Either the user exists or not,
     * the delete always succeed even if it is not found.
     * @param id the user ID to be deleted
     */
    void deleteUser(long id) {
        def entity = userRepository.findById(id)

        if(!entity.isPresent()) return
        entityManager.remove(entity.get())
    }
}
