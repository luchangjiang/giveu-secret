/**
 * 
 */
package com.giveu.dao.mysql;

import com.giveu.entity.SecretManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 529017
 *
 */
public interface SecretDao {

	List<SecretManage> getMaxSecretGroup(@Param("version") Integer version);

	List<SecretManage> getSecretList();

	List<SecretManage> getSecretGroup(@Param("secretType") int secretType);

	SecretManage getSecretManage(SecretManage secretManage);

	Integer updateTime(SecretManage secretManage);
}
