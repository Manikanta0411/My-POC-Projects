package com.record.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.record.entity.ActionEntity;
import com.record.entity.RoleEntity;
import com.record.exception.BadRequestException;
import com.record.exception.ResourceNotFoundException;
import com.record.model.ActionModel;
import com.record.model.LastOpIndicator;
import com.record.repository.ActionRepository;
import com.record.repository.RoleRepository;
import com.record.util.MapperUtil;

@Service 
public class ActionService {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(ActionService.class);

	private final ActionRepository actionRepository;
	private final RoleRepository roleRepository;

	public ActionService(ActionRepository actionRepository, RoleRepository roleRepository) {
		this.actionRepository = actionRepository;
		this.roleRepository = roleRepository;
	}

	public ActionModel createAction(ActionModel actionModel) throws BadRequestException {

		ActionEntity actionEntity = MapperUtil.convertModelToEntity(actionModel, ActionEntity.class);
		
		Optional<RoleEntity> roleEntity = roleRepository.findByRoleCode(actionModel.getRoleModel().getRoleCode());
        
		/*
		if (roleEntity.isPresent()) {

			if (actionRepository.findByRoleId(roleEntity.get().getId()).isPresent()) {
				LOGGER.error("Action with this Role already exists! "+roleEntity.get().getId());
				throw new BadRequestException("Action with this Role already exists!");
			}

		} else {
			LOGGER.error("Role not exists!! ");
			throw new BadRequestException("Role not exists!");
		}
		
		*/

		if (!roleEntity.isPresent()) {
			LOGGER.error("Role not exists!! ");
			throw new BadRequestException("Role not exists!");

		} 
		

		if (Objects.nonNull(actionRepository.findByActionCode(actionEntity.getActionCode()))) {
			LOGGER.error("Action Code " + actionEntity.getActionCode() + " should be unique!");
			throw new BadRequestException("Action Code " + actionEntity.getActionCode() + " should be unique!");
		}

		actionEntity.setRole(roleEntity.get());
		actionEntity.setLastOpIndicator(LastOpIndicator.I);
		ActionEntity action = actionRepository.save(actionEntity);
		return MapperUtil.convertEntityToModel(action, ActionModel.class);

	}

	/*
	 * 
	 * 
	 * public UserEntity getUserDetailsByEmail(String userName) throws
	 * EntityNotFoundException { UserEntity userEntity =
	 * userRepository.findByEmail(userName); if (userEntity == null) throw new
	 * EntityNotFoundException("No user with email id " + userName + " exists!");
	 * return userEntity; }
	 * 
	 */
	public ActionModel updateAction(Long actionId, ActionModel actionModel) throws EntityNotFoundException, BadRequestException {

		ActionEntity actionEntity = findById(actionId);
		// need to add check if action is associated with role then no update
		actionEntity.setActionName(actionModel.getActionName());
		// actionEntity.setRoleCode(actionModel.getRoleCode());
		actionEntity.setCanCreate(actionModel.getCanCreate());
		actionEntity.setCanDelete(actionModel.getCanDelete());
		actionEntity.setCanRead(actionModel.getCanRead());
		actionEntity.setCanUpdate(actionModel.getCanUpdate());
		actionEntity.setLastOpIndicator(LastOpIndicator.U);
		actionEntity.setExpireDate(actionModel.getExpireDate());
		Optional<RoleEntity> roleEntity = roleRepository.findByRoleCode(actionModel.getRoleModel().getRoleCode());
		/*
		if(actionRepository.findByRoleId(roleEntity.get().getId()).isPresent())
		{   LOGGER.error("Action with this Role already exists!");
			throw new BadRequestException("Action with this Role already exists!");
		}
		*/
		if(roleEntity.isPresent())
		{
		actionEntity.setRole(roleEntity.get());
		}else
		{
			LOGGER.error("Role not exists!! ");
			throw new BadRequestException("Role not exists!");
		}
		
		ActionEntity action = actionRepository.save(actionEntity);
		return MapperUtil.convertEntityToModel(action, ActionModel.class);
	}

	public void deleteAction(Long actionId) {
		ActionEntity actionEntity = findById(actionId);
		if (actionEntity != null) {
			actionEntity.setStatus(false);
			actionEntity.setLastOpIndicator(LastOpIndicator.D);
			actionRepository.save(actionEntity);
		}
	}
		

	public List<ActionModel> getActions() {
		return actionRepository.findByStatusTrue().stream()
				.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, ActionModel.class))
				.collect(Collectors.toList());
	}
	
	
	public List<ActionModel> getAllActions(String status) {
		
		if(status.equalsIgnoreCase("all"))
		{
			return actionRepository.findAll().stream()
					.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, ActionModel.class))
					.collect(Collectors.toList());
		}else
			if(status.equalsIgnoreCase("true"))
			{
				List<ActionEntity> entitiesList = actionRepository.findByStatusTrue();
				if(entitiesList.isEmpty()) {
					throw new ResourceNotFoundException("Action not found for status "+status);
				}
				return entitiesList.stream()
						.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, ActionModel.class))
						.collect(Collectors.toList());
			}else
			{
				List<ActionEntity> entitiesList = actionRepository.findByStatusFalse();
				if(entitiesList.isEmpty()) {
					throw new ResourceNotFoundException("Action not found for status "+status);
				}
				return entitiesList.stream()
					.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, ActionModel.class))
					.collect(Collectors.toList());
				
			}
		
		
	}
	

	public ActionModel findByActionCode(String actionCode) {
		return MapperUtil.convertEntityToModel(findByCode(actionCode), ActionModel.class);
	}
	
	/*
	public List<ActionResponseModel> findActionByRoleCode(String roles) {
		List<String> list = Stream.of(roles.split(",")).collect(Collectors.toList());
		ArrayList<ActionResponseModel> actionResMode = new ArrayList<>();
		
		list.stream().forEach(role -> {
			Optional<RoleEntity> roleEntity = roleRepository.findByRoleCode(role);
			if (roleEntity.isPresent()) {
				Optional<ActionEntity> acEntity = actionRepository.findByRoleId(roleEntity.get().getId());
				if (!acEntity.isPresent()) {
					LOGGER.error("Role " + role + " not found!");
					throw new EntityNotFoundException("Role " + role + " not found!");
				}

				ActionResponseModel newR = new ActionResponseModel();
				newR.setCanCreate(acEntity.get().getCanCreate());
				newR.setCanDelete(acEntity.get().getCanDelete());
				newR.setCanRead(acEntity.get().getCanRead());
				newR.setCanUpdate(acEntity.get().getCanUpdate());
				actionResMode.add(newR);
			} else {
				LOGGER.error("Role " + role + " not found!");
				throw new EntityNotFoundException("Role " + role + " not found!");
			}

		});

		return actionResMode;
	}
	*/
	
	public List<ActionModel> findActionByRole(String role)
	{
		Optional<RoleEntity> roleEntity = roleRepository.findByRoleCode(role);
		if (roleEntity.isPresent()) {
			
			
			System.out.println("Role Id: "+roleEntity.get().getId());
			
			List<ActionEntity> aa= actionRepository.findByRoleId(roleEntity.get().getId());

			System.out.println(aa.size());
			
			return actionRepository.findByRole(roleEntity.get()).stream()
					.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, ActionModel.class))
					.collect(Collectors.toList());

		} else {
			LOGGER.error("Role " + role + " not found!");
			throw new EntityNotFoundException("Role " + role + " not found!");
		}
	}
	
	
	
	public ActionEntity findByCode(String actionCode) throws EntityNotFoundException {
		ActionEntity actionEntity = actionRepository.findByActionCode(actionCode);
		if (Objects.isNull(actionEntity)) {
			LOGGER.error("Action with action code " + actionCode + " not found!");
			throw new EntityNotFoundException("Action with action code " + actionCode + " not found!");
		}
		return actionEntity;
	}

	public ActionEntity findById(Long actionId) throws EntityNotFoundException {
		ActionEntity actionEntity = actionRepository.findById(actionId).orElse(null);
		if (Objects.isNull(actionEntity)) {
			LOGGER.error("Action with action code " + actionId + " not found!");
			throw new EntityNotFoundException("Action with id " + actionId + " not found");
		}

		return actionEntity;
	}

}
