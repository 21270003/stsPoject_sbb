package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	public SiteUser create( String username, String password, String email ){
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setPassword( this.passwordEncoder.encode(password) );
		user.setEmail(email);
		this.userRepository.save( user );
		return user;
	}
	
	
	public SiteUser getUser( String username ){
		Optional<SiteUser> siteuser = this.userRepository.findByusername(username) ;
		if( siteuser.isPresent() ) {
			return siteuser.get();
		}else {
			throw new DataNotFoundException(" siteuser not found ");
		}
	}
	
}
