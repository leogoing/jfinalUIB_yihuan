package little.ant.platform.validator;

import little.ant.platform.model.Role;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class UnitPicValidator extends Validator {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UnitPicValidator.class);
	
	protected void validate(Controller controller) {
		String actionKey = getActionKey();
		if (actionKey.equals("/unitpic/save")){
			
		} else if (actionKey.equals("/unitpic/update")){
			
		}
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Role.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/unitpic/save")){
			
		} else if (actionKey.equals("/unitpic/update")){
			
		}
	}
}
