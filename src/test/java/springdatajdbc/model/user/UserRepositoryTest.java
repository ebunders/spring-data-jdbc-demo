package springdatajdbc.model.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static springdatajdbc.util.TestUtil.optionalValue;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void TestAddUser() {
        final User user = userRepository.save(User.of("Ernst"));
        assertThat(user.getId(), notNullValue());

        final User user2 = optionalValue(userRepository.findById(user.getId()));
        assertThat(user2, is(user));
    }

    @Test
    public void TestremoveUser() {
        final User user = userRepository.save(User.of("Ernst"));
        userRepository.delete(user);

        assertThat(userRepository.findById(user.getId()), is(empty()));
    }

    @Test
    public void TestUpdateUser() {
        final User user = userRepository.save(User.of("Ernst"));
        final User updatedUser = userRepository.save(user.withName("foo"));

        assertThat(user.getId(), is(updatedUser.getId()));
        assertThat(updatedUser.getName(), is("foo"));

    }

}
