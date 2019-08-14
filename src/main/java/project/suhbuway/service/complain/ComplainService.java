package project.suhbuway.service.complain;

import java.util.List;

import project.suhbuway.dto.Complain;

public interface ComplainService {
	
	/*
	 * �Խñ� �˻�(�ڱⰡ �� �۸� ����)
	 * */
	List<Complain> selectAll(String userId);
	
	/*
	 * �Խñ� �߰�
	 * */
	int insert(Complain complain);
	
	/*
	 * �Խñ� ����
	 * */
	int delete(String userId);
	
	/*
	 * �Խñ� ����
	 * */
	int update(String userId);
	
	/*
	 * ��� �ޱ�(admin�� ����)
	 * */
	int insertAdmin(Complain complain, String adminId);

}
