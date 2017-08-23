package com.video.upload;

import com.video.upload.model.User;
import com.video.upload.repository.UserRepository;
import com.video.upload.service.UserServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFindUserByUserNameSuccess(){
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test@email.com");

        when(userRepository.findByUsername(anyString())).thenReturn(mockUser);

        User returned = userService.findByUsername("test");

        assertEquals(returned.getId().longValue(), 1L);
        assertEquals(returned.getUsername(), "test@email.com");
    }
    @Test
    public void testCreateDuplicateUserNameFail() throws ValidationException {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test@email.com");

        when(userRepository.findByUsername(anyString())).thenReturn(mockUser);

        expectedException.expectMessage("User name already exist!");
        expectedException.expect(ValidationException.class);

        User returned = userService.createNewUser(mockUser);

        assertEquals(returned.getId().longValue(), 1L);
        assertEquals(returned.getUsername(), "test@email.com");
    }

    @Test
    public void testCreateUserNameSuccess() throws ValidationException {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test@email.com");

        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User returned = userService.createNewUser(mockUser);

        assertEquals(returned.getId().longValue(), 1L);
        assertEquals(returned.getUsername(), "test@email.com");
    }

}
