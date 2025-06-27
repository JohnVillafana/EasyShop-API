package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    // GET /profile - Get current user's profile
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Profile getProfile(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            Profile profile = profileDao.getByUserId(userId);

            if (profile == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
            }

            return profile;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving the profile");
        }
    }

    // PUT /profile - Update current user's profile
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public void updateProfile(@RequestBody Profile profile, Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Make sure the profile belongs to the authenticated user
            profile.setUserId(userId);

            profileDao.update(userId, profile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while updating the profile");
        }
    }
}
