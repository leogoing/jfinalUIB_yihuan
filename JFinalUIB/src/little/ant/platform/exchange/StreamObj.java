package little.ant.platform.exchange;

import java.io.Serializable;

/**
 * 为json中用于传递的对象提供接口
 * @author 李望
 *
 */
public interface StreamObj extends Serializable{
	public String getIds();
	public Long getVersion();
}
