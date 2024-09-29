package gr36.clubActiv.repository;


import gr36.clubActiv.domain.entity.ConfirmationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

  Optional<ConfirmationCode> findByCode(String code);

}