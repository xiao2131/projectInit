package cn.yirs.Common.Entity;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User implements Serializable {

	private static final long serialVersionUID = 7818806252658184777L;
	private Integer id;
	private String username;
	private String password;

}
