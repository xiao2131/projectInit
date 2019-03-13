package cn.yirs.Utils;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserGroupJson {

	@Value("classpath:userGroup.json")
	private Resource areaRes;

	public String getFileJson() {
		String areaData = null;
		try {
			areaData = IOUtils.toString(areaRes.getInputStream(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return areaData;
	}

}
