package repository.users;

import domain.dto.UserDto;

public interface UserRepository {
  boolean registerUser(UserDto user);
  
}
