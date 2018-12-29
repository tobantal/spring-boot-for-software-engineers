package com.yourname.learningspringboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.yourname.learningspringboot.clientproxy.UserResourceV1;
import com.yourname.learningspringboot.model.User;
import com.yourname.learningspringboot.model.User.Gender;

import javax.ws.rs.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LearningSpringBootApplicationTest {

	@Autowired
	private UserResourceV1 userResourceV1;
	
	@Test
	public void itShouldFetchAllUsers() {
		List<User> users = userResourceV1.fetchUsers(null);
		assertThat(users).hasSize(1);
		
		User joe = new User(null, "Joe", "Jones", Gender.MALE, 22, "joe.jones@gmail.com");
		assertThat(users.get(0)).isEqualToIgnoringGivenFields(joe, "userUid");
		assertThat(users.get(0).getUserUid()).isInstanceOf(UUID.class);
		assertThat(users.get(0).getUserUid()).isNotNull();
	}
	
	@Test
	public void shouldInsertUser() {
		//Given
		UUID joeUserUid = UUID.randomUUID();
		User user = new User(joeUserUid, "Joe", "Jones", Gender.MALE, 22, "joe.jones@gmail.com");
		
		//When
		userResourceV1.insertNewUser(user);
		
		//Then
		User joe = userResourceV1.fetchUser(joeUserUid);
		assertThat(joe).isEqualToComparingFieldByField(user);
	}
	
	@Test
	public void shouldDeleteUser() {
		//Given
		UUID joeUserUid = UUID.randomUUID();
		User user = new User(joeUserUid, "Joe", "Jones", Gender.MALE, 22, "joe.jones@gmail.com");
		
		//When
		userResourceV1.insertNewUser(user);
				
		//When
		userResourceV1.deleteUser(joeUserUid);
		
		//Then
		assertThatThrownBy(() -> userResourceV1.fetchUser(joeUserUid))
		.isInstanceOf(NotFoundException.class);
	}

}