package online.pangge.exam.util;

import com.google.gson.Gson;
import online.pangge.exam.domain.Subject;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis cache 工具类
 */
public final class RedisUtil {
    private Logger logger = Logger.getLogger(RedisUtil.class);
    private RedisTemplate<Serializable, Object> redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            logger.info("remove keys = " + key);
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            logger.info("remove pattern = " + pattern);
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            logger.info("remove sinle key = " + key);
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate
                .opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate
                    .opsForValue();
            logger.info("set key - value,key = " + key + ",value=" + value.getClass());
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate
                    .opsForValue();
            logger.info("set key - value,key = " + key + ",value=" + value.getClass() + ",expire time = " + expireTime);
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setSubject(final String key, Subject subject) {
        boolean result = false;
        try {
            ListOperations<Serializable, Object> list = redisTemplate.opsForList();
            Gson g = new Gson();
            list.rightPush(key, g.toJson(subject));
            result = true;
        } catch (Exception e) {
            logger.error("insert subject error : ", e);
            e.printStackTrace();
        }
        return result;
    }

    public Subject getSubject(final String key) {
        ListOperations<Serializable, Object> list = redisTemplate.opsForList();
        String subjects = list.rightPop(key).toString();
        if (subjects == null) {
            return null;
        }
        Gson g = new Gson();
        Subject s = g.fromJson(subjects, Subject.class);
        return s;
    }

    /**
     * get subject list
     *
     * @param key
     * @return
     */
    public List<Subject> getSubjects(final String key) {
        List<Subject> subjectList = new ArrayList<>();
        ListOperations<Serializable, Object> list = redisTemplate.opsForList();
        if (list.size(key) <= 0) {
            return Collections.EMPTY_LIST;
        }
        while (redisTemplate.hasKey(key)) {
            String subjects = list.rightPop(key).toString();
            if (subjects == null) {
                return null;
            }
            Gson g = new Gson();
            Subject s = g.fromJson(subjects, Subject.class);
            subjectList.add(s);
        }
        return subjectList;
    }

    public void setRedisTemplate(
            RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
