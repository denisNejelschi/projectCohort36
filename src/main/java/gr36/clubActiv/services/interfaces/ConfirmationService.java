package gr36.clubActiv.services.interfaces;


import gr36.clubActiv.domain.entity.User;

public interface ConfirmationService {

  String generateConfirmationCode(User user);

  User getUserByConfirmationCode(String code);

  void deleteByUserId(Long userId);
}
