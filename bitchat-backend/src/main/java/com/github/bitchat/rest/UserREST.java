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
package com.github.bitchat.rest;

import com.github.bitchat.data.UserRepository;
import com.github.bitchat.model.User;
import com.github.bitchat.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the Users table.
 */
@Path("/user")
@RequestScoped
@Api( value = "/user", description = "Manage user" )
public class UserREST {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private UserRepository repository;

    @Inject
    UserService registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() {
        return repository.findAllOrderedByName();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Verifica se o usuário e senha existem", httpMethod = "POST", notes = "Verifica se o usuário e senha existem", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "login encontrado"),
            @ApiResponse(code = 404, message = "login não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno em função da decodificação dos dados"),
            @ApiResponse(code = 400, message = "Erro no request em função da decodificação dos dados"),
            @ApiResponse(code = 412, message = "Dados obrigatórios não encontrados") })
    public User login(User user){
        User u = repository.findByLoginAndSenha(user.getLogin(), user.getSenha());
        if (u == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return u;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public User lookupUserById(@PathParam("id") long id) {
        User User = repository.findById(id);
        if (User == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return User;
    }

    /**
     * Creates a new User from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates User using bean validation
            validateUser(user);

            registration.register(user);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * <p>
     * Validates the given User variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing User with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param user User to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If User with the same email already exists
     */
    private void validateUser(User user) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the login
        if (emailAlreadyExists(user.getLogin())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a User with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the User class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(String email) {
        User User = null;
        try {
            User = repository.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }
        return User != null;
    }
}
