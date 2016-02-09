package little.ant.platform.exchange;

import java.io.Serializable;

public class DepartIdVer implements StreamObj{
	
	private String ids;
	private Long version;
	
	public DepartIdVer(String ids,Long version) {
		this.ids=ids;
		this.version=version;
	}
	
	public DepartIdVer(){
		
	}
	
	@Override
	public String toString() {
		return "DepartIdVer [ids=" + ids + ", version=" + version + "]";
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
}
