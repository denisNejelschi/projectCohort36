package gr36.clubActiv.services;


import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.repository.RoleRepository;
import gr36.clubActiv.services.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  public final RoleRepository repository;

  public RoleServiceImpl(RoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public Role getRoleUser() {
    return repository.findByRole("ROLE_USER").orElseThrow(
        () -> new RuntimeException("Database doesn't contain ROLE_USER")
    );
  }
}
