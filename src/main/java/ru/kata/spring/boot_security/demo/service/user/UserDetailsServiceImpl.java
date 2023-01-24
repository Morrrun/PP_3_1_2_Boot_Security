package ru.kata.spring.boot_security.demo.service.user;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
   private final UserRepository userRepository;
   @Autowired
   public UserDetailsServiceImpl(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Transactional
   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User person = userRepository.findByEmail(email);

      if(person == null) {
         throw new UsernameNotFoundException("Email not found!");
      }

      Hibernate.initialize(person.getRoles());
      return person;
   }

}
