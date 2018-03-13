package online.pangge.exam.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by jie34 on 2017/4/13.
 */
public class CleanCacheInterceptor implements MethodInterceptor {
    private Logger logger = Logger.getLogger(CleanCacheInterceptor.class);
    private RedisUtil redisUtil;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Set<Serializable> keys = redisUtil.keys("*");
        for(Serializable key : keys){
            System.out.println("all keys = "+key);
            if (redisUtil.exists(key.toString())) {
                System.out.println(redisUtil.get(key.toString()).getClass());
            }
        }
        return null;
    }


    /**
     * 创建缓存key
     *
     * @param targetName
     * @param methodName
     * @param arguments
     */
    private String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuffer sbu = new StringBuffer();
        sbu.append(targetName).append("_").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sbu.append("_").append(arguments[i]);
            }
        }
        System.out.println("cache key = "+sbu.toString());
        return sbu.toString();
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}