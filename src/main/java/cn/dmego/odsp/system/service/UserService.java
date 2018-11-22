package cn.dmego.odsp.system.service;

import cn.dmego.odsp.common.PageResult;
import cn.dmego.odsp.common.exception.BusinessException;
import cn.dmego.odsp.system.model.User;
import com.baomidou.mybatisplus.service.IService;


/**
 * class_name: UserService
 * package: cn.dmego.odsp.system.service
 * describe: 用户 Service 接口类
 * creat_user: Dmego
 * creat_date: 2018/10/21
 * creat_time: 22:41
 **/
public interface UserService extends IService<User> {

    User getByUserName(String username);

    PageResult<User> list(Integer pageNum, Integer pageSize,boolean showDelete,String serachKey, String searchValue);

    boolean updateState(Integer userId, Integer state);

    boolean add(User user) throws BusinessException;

    boolean update(User user) throws BusinessException ;

    boolean updPsw(Integer loginUserId, String loginUserName, String newPsw);

    User getById(Integer userId);

    boolean delete(Integer userId);
}

