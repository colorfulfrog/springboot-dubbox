package com.yxhl.platform.common.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

/**
 * JWT工具类
 * <p>
 * 1、用户使用用户名密码来请求服务器</br>
 * 2、服务器进行验证用户的信息 服务器通过验证发送给用户一个token</br>
 * 3、 客户端存储token，并在每次请求时附送上这个token值 服务端验证token值，并返回数据 </br>
 * 4、这个token必须要在每次请求时传递给服务端，它应该保存在请求头里</br>
 * </p>
 */
public class JwtUtils {
    /**
     * 生成jwt token
     * @param key 签发标识
     * @param secret 秘钥 
     * @param expireTime 过期时间
     * @return token
     */
    public static String createToken(String key,final String secret,Date expireTime){
        long nowMillis = System.currentTimeMillis();
        Date now=new Date(nowMillis);//签发时间精度:毫秒
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
            		.withIssuer(key)
            		.withIssuedAt(now)
            		.withExpiresAt(expireTime)
            		.sign(algorithm);
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
        return null;
    }
    
    /**
     * 校验token是否有效
     * @param key 签发标识
     * * @param secret 秘钥 
     * @param token 需要验证的token
     * @return true：token有效，false：token无效
     */
    public static boolean verifyToken(String key,final String secret,String token){
        boolean active = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);//声明签名所用的算法和秘钥
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(key).build();
            verifier.verify(token);
        } catch (TokenExpiredException exception){
            //System.out.println("--- token 过期");
            active = false;
        } catch (JWTDecodeException exception){
            //System.out.println("--- token 无效");
            active = false;
        } catch (JWTVerificationException exception){
            //System.out.println("--- token 错误");
            active = false;
        }
        return active;
    }

    public static void main(String[] args) {
    	//生成token：
        String token=createToken("liwei","master",DateHelper.parseDate("2018-10-24 10:05:00"));
        System.out.println(token);
        
        //验证token
        boolean flag = verifyToken("liwei","master",token);
        System.out.println(flag);
    }
}
