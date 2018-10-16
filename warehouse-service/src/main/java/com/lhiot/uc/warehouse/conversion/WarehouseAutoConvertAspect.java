package com.lhiot.uc.warehouse.conversion;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Jackson;
import com.leon.microx.util.Maps;
import com.lhiot.uc.warehouse.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.entity.WarehouseOverdue;
import com.lhiot.uc.warehouse.entity.WarehouseProduct;
import com.lhiot.uc.warehouse.feign.BasicDataService;
import com.lhiot.uc.warehouse.mapper.WarehouseOverdueMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseProductMapper;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfeng create in 9:03 2018/10/15
 */
@Aspect
@Component
public class WarehouseAutoConvertAspect {

    private final WarehouseProductMapper warehouseProductMapper;
    private final WarehouseOverdueMapper warehouseOverdueMapper;
    private final BasicDataService basicDataService;
    private ExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private ApplicationEventPublisher publisher;

    @Autowired
    public WarehouseAutoConvertAspect(WarehouseProductMapper warehouseProductMapper, WarehouseOverdueMapper warehouseOverdueMapper, BasicDataService basicDataService, ApplicationEventPublisher publisher) {
        this.warehouseProductMapper = warehouseProductMapper;
        this.warehouseOverdueMapper = warehouseOverdueMapper;
        this.basicDataService = basicDataService;
        this.publisher = publisher;
    }

    @Around("@annotation(warehouseProductConvert)")
    public Object around(ProceedingJoinPoint joinPoint, WarehouseProductConvert warehouseProductConvert) throws Throwable {
        if (StringUtils.hasLength(warehouseProductConvert.warehouseId())){
//            warehouseId = #param.id + #userId
//            params[i], args[i] = #param, #userId
//            getParamValue = 1 + 2 = 3
            String realId = this.parseExpression(joinPoint, warehouseProductConvert.warehouseId());
            RuleType rule = warehouseProductConvert.rule();
//            ResponseEntity categoryResponse =  basicDataService.findCategoryByCode(rule.getCgCode(),rule.getCode());
//            if (categoryResponse.getStatusCode().isError()){
//               return ResponseEntity.badRequest().body("仓库转换错误！");
//            }
//            CategoryItem item = (CategoryItem) categoryResponse.getBody();
//            Map<String,Object> map = Jackson.map(item.getAttache());
//            int freshDay = (int) map.get("freshDay");
//            int day = (int) map.get("freshDay")+(int)map.get("convertFirst")+(int)map.get("convertSecond")+(int)map.get("convertThird");
            int freshDay = 7;
            int day = 10;
            WarehouseConvertTaskParam taskParam = new WarehouseConvertTaskParam();
            taskParam.setWarehouseId(realId);
            taskParam.setSystemConvertDay(day);
            taskParam.setFreshDay(freshDay);
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
