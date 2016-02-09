package little.ant.platform.exchange;

import java.io.Serializable;

/**
 * 映射unitId和ver
 * @author 李望
 *
 */
public class UnitIdVer implements Serializable{
	private Long unitid;
	private Long ver;
	private String unitname;
	
	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public UnitIdVer(){
		
	}
	
	public UnitIdVer(Long unitId,Long ver){
		this.unitid=unitId;
		this.ver=ver;
	}
	
	public Long getUnitid() {
		return unitid;
	}
	public void setUnitid(Long unitid) {
		this.unitid = unitid;
	}
	public Long getVer() {
		return ver;
	}
	public void setVer(Long ver) {
		this.ver = ver;
	}
	@Override
	public String toString() {
		return "UnitIdVer [unitid=" + unitid + ", ver=" + ver + ", unitname=" + unitname + "]";
	}
	
}
