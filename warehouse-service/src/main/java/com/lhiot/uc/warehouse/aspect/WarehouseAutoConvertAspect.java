package com.lhiot.uc.warehouse.aspect;

import com.leon.microx.util.Jackson;
import com.lhiot.dc.dictionary.DictionaryClient;
import com.lhiot.dc.dictionary.module.Dictionary;
import com.lhiot.uc.warehouse.model.RuleType;
import com.lhiot.uc.warehouse.model.WarehouseConvertRule;
import com.lhiot.uc.warehouse.model.WarehouseConvertTaskParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zhangfeng create in 9:03 2018/10/15
 */
@Aspect
@Component
public class WarehouseAutoConvertAspect {

    private ExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private ApplicationEventPublisher publisher;
    private DictionaryClient dictionaryClient;

    @Autowired
    public WarehouseAutoConvertAspect(ApplicationEventPublisher publisher, DictionaryClient dictionaryClient) {
        this.publisher = publisher;
        this.dictionaryClient = dictionaryClient;
    }

    @Around("@annotation(warehouseProductConvert)")
    public Object around(ProceedingJoinPoint joinPoint, WarehouseProductConvert warehouseProductConvert) throws Throwable {
        if (StringUtils.hasLength(warehouseProductConvert.warehouseId())) {
//            warehouseId = #param.id + #userId
//            params[i], args[i] = #param, #userId
//            getParamValue = 1 + 2 = 3
            String realId = this.parseExpression(joinPoint, warehouseProductConvert.warehouseId());
            RuleType rule = warehouseProductConvert.rule();
            Dictionary.Entry entry = dictionaryClient.dictionary(rule.getDictCode()).entry(rule.getCode());
            if (Objects.isNull(entry)){
                return ResponseEntity.badRequest().body("仓库转换错误！");
            }
            WarehouseConvertRule convertRule = Jackson.object(entry.getAttach(),WarehouseConvertRule.class);
            int day = convertRule.getFreshDay()+convertRule.getConvertFirst()+convertRule.getConvertSecond()+convertRule.getConvertThird();
            WarehouseConvertTaskParam taskParam = new WarehouseConvertTaskParam();
            taskParam.setWarehouseId(realId);
            taskParam.setSystemConvertDay(day);
            taskParam.setFreshDay(convertRule.getFreshDay());
            taskParam.setSystemDiscount(convertRule.getSystemDiscount());
            publisher.publishEvent(taskParam);
        }
        return joinPoint.proceed();
    }

    private String parseExpression(ProceedingJoinPoint joinPoint, String expression) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        if (!ObjectUtils.isEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                context.setVariable(params[i], args[i]);
            }
        }
        Object object = parser.parseExpression(expression).getValue(context, Object.class);
        return String.valueOf(object);
    }
}
