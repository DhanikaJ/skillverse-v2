package com.skillverse.security;

import com.skillverse.demo.DemoProperties;
import com.skillverse.model.Course;
import com.skillverse.model.Role;
import com.skillverse.model.Users;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Central place for object-level authorization rules.
 *
 * The acting user is always resolved from the authenticated JWT principal, never from
 * request parameters. All {@code require*} methods throw {@link AccessDeniedException}
 * (mapped to HTTP 403 by the global exception handler) when the rule is not met.
 */
@Component
public class AccessPolicy {

    private final UsersRepository usersRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final DemoProperties demoProperties;

    public AccessPolicy(UsersRepository usersRepository, EnrollmentRepository enrollmentRepository,
                        DemoProperties demoProperties) {
        this.usersRepository = usersRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.demoProperties = demoProperties;
    }

    /**
     * Returns the user behind the current request.
     *
     * @throws AuthenticationCredentialsNotFoundException if the request is not authenticated
     */
    public Users currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }
        return usersRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Authentication required"));
    }

    public boolean isAdmin(Users user) {
        return user.getRole() == Role.ADMIN;
    }

    /**
     * Rejects the public demo accounts. Their password is public, so they must not trigger
     * actions that cost money or store arbitrary content (such as file uploads).
     */
    public void requireNotDemoAccount() {
        if (demoProperties.isDemoAccount(currentUser().getEmail())) {
            throw new AccessDeniedException("This action is disabled for demo accounts");
        }
    }

    /** Allows only administrators. */
    public void requireAdmin() {
        if (!isAdmin(currentUser())) {
            throw new AccessDeniedException("Administrator role required");
        }
    }

    /** Allows the user identified by {@code userId}, or an administrator. */
    public void requireSelfOrAdmin(Integer userId) {
        Users current = currentUser();
        if (!isAdmin(current) && (userId == null || !userId.equals(current.getId()))) {
            throw new AccessDeniedException("You do not have access to this resource");
        }
    }

    /** Allows the instructor who owns the course, or an administrator. */
    public void requireCourseManager(Course course) {
        if (!canManage(currentUser(), course)) {
            throw new AccessDeniedException("Only the course instructor or an administrator can do this");
        }
    }

    /** Allows learners enrolled in the course, the course instructor, or an administrator. */
    public void requireCourseAccess(Course course) {
        Users current = currentUser();
        if (canManage(current, course)) {
            return;
        }
        if (!enrollmentRepository.existsByUser_IdAndCourse_Id(current.getId(), course.getId())) {
            throw new AccessDeniedException("Enrollment in this course is required");
        }
    }

    private boolean canManage(Users user, Course course) {
        if (isAdmin(user)) {
            return true;
        }
        Users owner = course.getUsers();
        return owner != null && owner.getId() != null && owner.getId().equals(user.getId());
    }
}
