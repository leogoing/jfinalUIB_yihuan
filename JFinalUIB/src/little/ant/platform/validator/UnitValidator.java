package little.ant.platform.validator;

import little.ant.platform.model.Role;
import little.ant.platform.service.DepartmentService;
import little.ant.platform.service.UserService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class UnitValidator extends Validator {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UnitValidator.class);
	
	protected void validate(Controller controller) {
		
		String actionKey = getActionKey();
		if (actionKey.equals("/unit/save")){
			
		} else if (actionKey.equals("/unit/update")){
			
		}
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Role.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/unit/save")){
			
		} else if (actionKey.equals("/unit/update")){
			
		}
	}
	
}
