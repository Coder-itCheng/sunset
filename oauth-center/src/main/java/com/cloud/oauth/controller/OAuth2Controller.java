package com.cloud.oauth.controller;

import com.cloud.log.autoconfigure.LogMqClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class OAuth2Controller {

    /**
     * 当前登陆用户信息<br>
     * @return
     */
    @GetMapping("/user-me")
    public Authentication principal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("user-me:{}", authentication.getName());
        return authentication;
    }

//	@GetMapping("/user-me")
//	public Principal principal(Principal principal) {
//		log.debug("user-me:{}", principal.getName());
//		return principal;
//	}
//
//	@GetMapping("/user-me")
//	public Authentication principal(Authentication authentication) {
//		log.debug("user-me:{}", authentication.getName());
//		return authentication;
//	}
//
//	@GetMapping("/user-me")
//	public OAuth2Authentication principal(OAuth2Authentication authentication) {
//		log.debug("user-me:{}", authentication.getName());
//		return authentication;
//	}

    //	@Autowired
//	private TokenStore tokenStore;
//
//	/**
//	 * 移除access_token和refresh_token
//	 *
//	 * @param access_token
//	 */
//	@DeleteMapping(value = "/remove_token", params = "access_token")
//	public void removeToken(Principal principal, String access_token) {
//		OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
//		if (accessToken != null) {
//			// 移除access_token
//			tokenStore.removeAccessToken(accessToken);
//
//			// 移除refresh_token
//			if (accessToken.getRefreshToken() != null) {
//				tokenStore.removeRefreshToken(accessToken.getRefreshToken());
//			}
//
//			saveLogoutLog(principal.getName());
//		}
//	}

    @Autowired
    private ConsumerTokenServices tokenServices;

    /**
     * 注销登陆/退出
     * @param access_token
     */
    @DeleteMapping(value = "/remove_token", params = "access_token")
    public void removeToken(String access_token) {
        boolean flag = tokenServices.revokeToken(access_token);
        if (flag) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            saveLogoutLog(authentication.getName());
        }
    }

//    @Autowired
//    private LogClient logClient;
    @Autowired
    private LogMqClient logMqClient;

    /**
     * 退出日志
     * @param username
     */
    private void saveLogoutLog(String username) {
        log.info("{}退出", username);
        // 异步
//        CompletableFuture.runAsync(() -> {
//            try {
//                Log log = Log.builder().username(username).module("退出").createTime(new Date()).build();
//                logClient.save(log);
//            } catch (Exception e) {
//                // do nothing
//            }
//
//        });
        // 调整为mq的方式记录退出日志
        logMqClient.sendLogMsg("退出", username, null, null, true);
    }

}
