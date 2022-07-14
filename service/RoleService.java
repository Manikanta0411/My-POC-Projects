package com.record.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.record.entity.RoleEntity;
import com.record.entity.UserEntity;
import com.record.exception.BadRequestException;
import com.record.exception.ResourceNotFoundException;
import com.record.model.LastOpIndicator;
import com.record.model.LobModel;
import com.record.model.RoleModel;
import com.record.repository.RoleRepository;
import com.record.repository.UserRepository;
import com.record.util.MapperUtil;

@Service
public class RoleService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	public RoleModel createRole(RoleModel roleModel) throws BadRequestException {

		RoleEntity roleEntity = MapperUtil.convertModelToEntity(roleModel, RoleEntity.class);
		if (roleRepository.findByRoleCode(roleEntity.getRoleCode()).isPresent()) {
			throw new BadRequestException("Role Code " + roleEntity.getRoleCode() + " should be unique");
		}
		roleEntity.setLastOpIndicator(LastOpIndicator.I);
		RoleEntity role = roleRepository.save(roleEntity);
		return MapperUtil.convertEntityToModel(role, RoleModel.class);

	}

	public UserEntity getUserDetailsByEmail(String userName) throws EntityNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findByEmail(userName);
		if (!userEntity.isPresent())
			throw new EntityNotFoundException("No user with email id " + userName + " exists!");
		return userEntity.get();
	}

	public RoleModel updateRole(Long userId, RoleModel roleModel) throws EntityNotFoundException {
		RoleEntity roleData = findById(userId);
		roleData.setRoleName(roleModel.getRoleName());
		roleData.setStatus(roleModel.getStatus());
		// roleData.setUserId(roleModel.getUserId());
		roleData.setLastOpIndicator(LastOpIndicator.U);
		roleData.setExpireDate(roleModel.getExpireDate());
		RoleEntity role = roleRepository.save(roleData);
		return MapperUtil.convertEntityToModel(role, RoleModel.class);
	}

	public void deleteRole(Long roleId) {
		RoleEntity roleEntity = findById(roleId);
		if (roleEntity != null)
		{	roleEntity.setLastOpIndicator(LastOpIndicator.D);
			roleEntity.setStatus(false);
		    roleRepository.save(roleEntity);
	    }
	}

	public List<RoleModel> getRoles(String status) {

		if (status.equalsIgnoreCase("all")) {
			return roleRepository.findAll().stream()
					.map(roleEntity -> MapperUtil.convertEntityToModel(roleEntity, RoleModel.class))
					.collect(Collectors.toList());
		} else if (status.equalsIgnoreCase("true")) {
			List<RoleEntity> entitiesList = roleRepository.findByStatusTrue();
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("Role not found with status "+status);
			}
			return entitiesList.stream()
					.map(roleEntity -> MapperUtil.convertEntityToModel(roleEntity, RoleModel.class))
					.collect(Collectors.toList());
		} else {
			List<RoleEntity> entitiesList = roleRepository.findByStatusFalse();
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("Role not found with status "+status);
			}
			return entitiesList.stream()
					.map(roleEntity -> MapperUtil.convertEntityToModel(roleEntity, RoleModel.class))
					.collect(Collectors.toList());

		}

	}

	public RoleModel findByRoleId(Long roleId) {
		return MapperUtil.convertEntityToModel(findById(roleId), RoleModel.class);
	}

	public RoleModel findByRoleCode(String roleCode) {
		return MapperUtil.convertEntityToModel(findByCode(roleCode), RoleModel.class);
	}

	/*
	 * public List<RoleModel> findRoleByUser(Long userId) { return
	 * findByUser(userId).stream() .map(roleEntity ->
	 * MapperUtil.convertEntityToModel(roleEntity, RoleModel.class))
	 * .collect(Collectors.toList());
	 * 
	 * }
	 * 
	 * public List<RoleEntity> findByUser(Long userId) throws
	 * EntityNotFoundException { List<RoleEntity> roleEntity =
	 * roleRepository.findByUserId(userId); if (roleEntity.isEmpty()) { throw new
	 * EntityNotFoundException("Role with User Id " + userId + " not found!"); }
	 * return roleEntity; }
	 */
	public RoleEntity findByCode(String roleCode) throws EntityNotFoundException {
		Optional<RoleEntity> roleEntity = roleRepository.findByRoleCode(roleCode);

		if (!roleEntity.isPresent()) {
			throw new EntityNotFoundException("Role with role code " + roleCode + " not found!");
		}

		return roleEntity.get();
	}

	public RoleEntity findById(Long roleId) throws EntityNotFoundException {
		RoleEntity roleEntity = roleRepository.findById(roleId).orElse(null);
		if (Objects.isNull(roleEntity)) {
			throw new EntityNotFoundException("Role with id " + roleId + " not found");
		}
		return roleEntity;
	}

}
