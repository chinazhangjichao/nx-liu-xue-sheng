package cn.zjc.app.config;

import cn.zjc.app.bean.SysUser;
import cn.zjc.app.bean.SysUserRole;
import cn.zjc.app.dao.SysRoleRightDao;
import cn.zjc.app.dao.SysUserDao;
import cn.zjc.app.dao.SysUserRoleDao;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.ServletUtil;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJC
 * @decription:
 * @date: 2022-08-04 13:45
 * @since JDK 1.8
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private SysUserRoleDao sysUserRoleDao;
    @Resource
    private SysRoleRightDao sysRoleRightDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = this.sysUserDao.queryByName(username);
        if(null==sysUser)
            throw  new UsernameNotFoundException("用户不存在！");
        if(sysUser.getUserStatus()!=1)
            throw  new DisabledException("用户已停用！");

        //用来存储当前用户的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<SysUserRole> userRoles = sysUserRoleDao.queryByUser(sysUser.getUserId());
        if (null==userRoles || userRoles.isEmpty()) {
            throw new InternalAuthenticationServiceException("用户未设置角色！");
        }
        List<String> roleIds = new ArrayList<>();
        for (SysUserRole sur : userRoles) {
            roleIds.add(sur.getUrRole().getRoleId().toString());
        }
        List<String> roleRights = this.sysRoleRightDao.queryByRoles(roleIds);
        if (null != roleRights && roleRights.size() > 0) {
            for (String s : roleRights) {
                //权限字符串必须不能为空，否则出现异常
                if (StringUtils.hasLength(s)) {
                    authorities.add(new SimpleGrantedAuthority(s));
                }
            }
        }
        authorities.add(new SimpleGrantedAuthority("/auth/admin"));
        return new User(sysUser.getUserName(),sysUser.getUserPwd(),authorities);
    }
}
