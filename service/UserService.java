package com.record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.record.entity.LobEntity;
import com.record.entity.RoleEntity;
import com.record.entity.UserEntity;
import com.record.exception.BadRequestException;
import com.record.exception.ResourceNotFoundException;
import com.record.model.LastOpIndicator;
import com.record.model.RoleModel;
import com.record.model.UserModel;
import com.record.repository.LobRepository;
import com.record.repository.RoleRepository;
import com.record.repository.UserRepository;
import com.record.util.MapperUtil;

@Service
public class UserService {

    
    private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final LobRepository lobRepository;
	private final RoleRepository roleRepository;	
	

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,LobRepository lobRepository,RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.lobRepository=lobRepository;
		this.roleRepository=roleRepository;
	}



	public UserModel createUser(UserModel userModel) throws BadRequestException {

		UserEntity userEntity = MapperUtil.convertModelToEntity(userModel, UserEntity.class);
		
		if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
			throw new BadRequestException("Email ID  " + userEntity.getEmail() + " should be unique");
		}
		checkLobExists(userModel.getLobCode());
		checkRoleExists(userModel.getRoleCode());
		userEntity.setUserId(userEntity.getEmail());
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userEntity.setLastOpIndicator(LastOpIndicator.I);
		userEntity.setExpiryDate(userModel.getExpiryDate());
		UserEntity user = userRepository.save(userEntity);
		return MapperUtil.convertEntityToModel(user, UserModel.class);

	}

	public UserEntity getUserDetailsByEmail(String userName) throws EntityNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findByEmail(userName);
		if (!userEntity.isPresent())
			throw new EntityNotFoundException("No user with email id " + userName + " exists!");
		return userEntity.get();
	}

	public UserModel updateUser(Long userId, UserModel userModel) throws EntityNotFoundException, BadRequestException {
		
		checkLobExists(userModel.getLobCode());
		checkRoleExists(userModel.getRoleCode());
		
		UserEntity userData = findById(userId);
		userData.setStatus(userModel.getStatus());
		userData.setUserName(userModel.getUserName());
		userData.setLobCode(userModel.getLobCode());
		userData.setRoleCode(userModel.getRoleCode());
		userData.setLastOpIndicator(LastOpIndicator.U);
		
		UserEntity user = userRepository.save(userData);
		return MapperUtil.convertEntityToModel(user, UserModel.class);
	}

	public void deleteUser(Long userId) {
		UserEntity userEntity = findById(userId);
		if (userEntity != null) {
			userEntity.setStatus(false);
			userEntity.setLastOpIndicator(LastOpIndicator.D);
			userRepository.save(userEntity);
		}

	}

	public List<UserModel> getUsers(String status) {
		
		if (status.equalsIgnoreCase("all")) {
			return userRepository.findAll().stream()
					.map(userEntity -> MapperUtil.convertEntityToModel(userEntity, UserModel.class))
					.collect(Collectors.toList());
		} else if (status.equalsIgnoreCase("true")) {
			
			List<UserEntity> usersList = userRepository.findByStatusTrue();
			if(usersList.isEmpty()) {
				throw new ResourceNotFoundException("User not found with status "+status );
			}
			return userRepository.findByStatusTrue().stream()
					.map(userEntity -> MapperUtil.convertEntityToModel(userEntity, UserModel.class))
					.collect(Collectors.toList());
		} else {
			List<UserEntity> usersList = userRepository.findByStatusFalse();
			if(usersList.isEmpty()) {
				throw new ResourceNotFoundException("User not found with status "+status );
			}
			return userRepository.findByStatusFalse().stream()
					.map(userEntity -> MapperUtil.convertEntityToModel(userEntity, UserModel.class))
					.collect(Collectors.toList());

		}

	}

	public UserModel findByUserId(Long userId) {
		return MapperUtil.convertEntityToModel(findById(userId), UserModel.class);
	}

	public UserEntity findById(Long userId) throws EntityNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findById(userId);
		if (!userEntity.isPresent()) {
			throw new EntityNotFoundException("User with id " + userId + " not found!");
		}
		return userEntity.get();
	}
	
	public UserModel getUserByEmail(String email) {
		Optional<UserEntity> userEntity = userRepository.findByEmail(email);
		if (!userEntity.isPresent()) {
			throw new EntityNotFoundException("User with email " + email + " not found!");
		}

		return MapperUtil.convertEntityToModel(userEntity.get(), UserModel.class);
	}
	
	private void checkLobExists(String lobs) throws BadRequestException
	{
		if(lobs!=null && lobs!="")
		{
			List<String> lobCodeList = Stream.of(lobs.split(",")).collect(Collectors.toList());
			
			if (!lobCodeList.isEmpty()) {
				lobCodeList.stream().forEach(lobCode -> {
					Optional<LobEntity> lobEntity = lobRepository.findByLobCode(lobCode);
					if (!lobEntity.isPresent()) {
						throw new EntityNotFoundException("LOB Code " + lobCode + " not found!");
					}
				});
			} else {
				throw new BadRequestException("Lob can not be empty!");
			}
		}else {
			throw new BadRequestException("Lob can not be empty!");
		}
	}
	

	private void checkRoleExists(String roles) throws BadRequestException
	{
		if(roles!=null && roles!="")
		{
			List<String> roleCodeList = Stream.of(roles.split(",")).collect(Collectors.toList());
			
			if (!roleCodeList.isEmpty()) {
				roleCodeList.stream().forEach(roleCode -> {
					Optional<RoleEntity> roleEntity = roleRepository.findByRoleCode(roleCode);
					if (!roleEntity.isPresent()) {
						throw new EntityNotFoundException("Role Code " + roleCode + " not found!");
					}
				});
			} else {
				throw new BadRequestException("Role can not be empty!");
			}
		}else {
			throw new BadRequestException("Role can not be empty!");
		}
		
	}
	


}
