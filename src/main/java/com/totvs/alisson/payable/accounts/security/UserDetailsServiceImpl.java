package com.totvs.alisson.payable.accounts.security;

import com.totvs.alisson.payable.accounts.domain.repository.UserRepository;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.totvs.alisson.payable.accounts.domain.entity.User appUser =
        userRepository.findByUsername(username);

    if (appUser == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    return new User(appUser.getUsername(), appUser.getPassword(), Collections.emptyList());
  }
}
