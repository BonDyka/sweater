package loc.abondarev.sweater.service;

import loc.abondarev.sweater.domain.Role;
import loc.abondarev.sweater.domain.User;
import loc.abondarev.sweater.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;


public class UserServiceTest {
    private MailSender mailSender = Mockito.mock(MailSender.class);
    private UserRepo userRepo = Mockito.mock(UserRepo.class);
    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    private UserService userService = new UserService(userRepo, mailSender, passwordEncoder);

    @Test
    public void addUser() {
        final User user = new User();
        user.setEmail("some@mail.ru");

        final boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("John");

        Mockito.doReturn(new User()).when(userRepo).findByUsername("John");

        final boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0)).send(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    public void activateUser() {
        final User user = new User();
        user.setActivationCode("bingo");
        Mockito.doReturn(user).when(userRepo).findByActivationCode("activate");

        final boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        final boolean isUserActivated = userService.activateUser("activate");

        Assert.assertFalse(isUserActivated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}