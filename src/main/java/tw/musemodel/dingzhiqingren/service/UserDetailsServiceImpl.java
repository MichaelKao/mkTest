package tw.musemodel.dingzhiqingren.service;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Privilege;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.entity.User;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.PrivilegeRepository;
import tw.musemodel.dingzhiqingren.repository.UserRepository;

/**
 * 服务层：加载用户的特定数据
 *
 * @author p@musemodel.tw
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findOneByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		Lover mofo = loverRepository.findById(user.getId()).orElseThrow();

		Collection<Role> roles = new ArrayList<>();
		for (Privilege privilege : privilegeRepository.findByLover(mofo)) {
			roles.add(privilege.getRole());
		}
		return org.springframework.security.core.userdetails.User.builder().
			username(user.getUsername()).
			password(user.getPassword()).
			disabled(!user.isEnabled()).
			authorities(authorities(roles)).
			build();
	}

	private static Collection<GrantedAuthority> authorities(Collection<Role> roles) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role -> {
			authorities.add(
				new SimpleGrantedAuthority(
					role.getTextualRepresentation()
				)
			);
		});
		return authorities;
	}
}
