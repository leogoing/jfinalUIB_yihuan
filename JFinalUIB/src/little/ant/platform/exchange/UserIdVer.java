package little.ant.platform.exchange;

import java.io.Serializable;

public class UserIdVer implements StreamObj{
	private String userids;
	private Long version;
	
	public UserIdVer(){
		
	}
	
	public UserIdVer(String userids,Long version){
		this.userids=userids;
		this.version=version;
	}
	
	public String getUserids() {
		return userids;
	}
	public void setUserids(String userids) {
		this.userids = userids;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "UserIdVer [userids=" + userids + ", version=" + version + "]";
	}

	@Override
	public String getIds() {
		return getUserids();
	}
}
