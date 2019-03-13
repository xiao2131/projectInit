package cn.yirs.Common.Entity;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Employee implements Serializable  {

	private static final long serialVersionUID = 2108258816737360679L;

	private Integer id;

    private String userId;

    private String birthTime;

    private String sexType;

    private String marriage;

    private String address;

    private String major;

    private String name;

    private String mobile;

    private String highestEdu;

    private String graduateSchool;

    private String graduationTime;


}
