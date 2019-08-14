package project.suhbuway.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Member {
	
  private String id;
  private String password;
  private String name;
  private String email;
  
  private String userType;// 회원가입할때 받는 타입
}
