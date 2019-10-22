package com.resto.shop.web.aspect;

import com.resto.shop.web.model.TableGroup;
import com.resto.shop.web.producer.MQMessageProducer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2017/9/26.
 */
@Component
@Aspect
public class GroupAspect  {

    @Autowired
    MQMessageProducer mqMessageProducer;

    @Pointcut("execution(* com.resto.shop.web.service.TableGroupService.insertGroup(..))")
    public void insertGroup(){};



    @AfterReturning(value = "insertGroup()", returning = "tableGroup")
    public void insertGroup(TableGroup tableGroup) {
        //创建组后 如果 15分钟内 没有 买单 ，则组自动消失
        mqMessageProducer.removeTableGroup(tableGroup,15 * 60 * 1000);
    }
}
