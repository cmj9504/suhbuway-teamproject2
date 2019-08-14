package project.suhbuway.dao.complain;

import java.util.List;

import org.springframework.stereotype.Repository;

import project.suhbuway.dto.Complain;

@Repository
public interface ComplainDAO {
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
