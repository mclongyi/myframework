package com.longyi.csjl.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggerCallAspect {

    /**
     * spring spel表达式
     */
    ExpressionParser parser = new SpelExpressionParser();

    /**
     * 通过本地变量表来获取参数信息
     */
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 定义切面表达式
     */
    @Pointcut("@annotation(com.longyi.csjl.aop.LoggerCallAnnotation)")
    public void loggerPointCut() {
    }

    @Around(value = "loggerPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String recordCode=this.getRecordCode(point);
        // 获取请求url
        HttpServletRequest httpServletRequest = this.getHttpServletRequest();
        String url = httpServletRequest.getRequestURL().toString();
        String ip=httpServletRequest.getLocalAddr();

        //获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String methodName=method.getName();
        // 获取注解
        LoggerCallAnnotation loggerCallAnnotation = method.getAnnotation(LoggerCallAnnotation.class);

        //获取参数信息
        Object[] args = point.getArgs();
        String[] paramNames = ((CodeSignature) point
                .getSignature()).getParameterNames();
        Map<String, Object> param = new HashMap<>();
        for(int i= 0; i < args.length; i++) {
            param.put(paramNames[i], args[i]);
        }
        Object result = point.proceed();
        if(loggerCallAnnotation.needDB()){
            //保存数据库业务
        }
        System.out.println("url=" + url + " ip=" + ip + " param=" + JSON.toJSONString(param)+" result="+JSON.toJSONString(result));
        return result;
    }

    /**
     * 构建httprequest对象
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        return httpServletRequest;
    }


    /**
     * 解析方法上面定义的参数
     * @param point
     * @return
     */
    private String getRecordCode(ProceedingJoinPoint point){
        String res="";
        try{
            //获取参数对象数组
            Object[] args = point.getArgs();
            //获取方法
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            //获取注解
            LoggerCallAnnotation loggerCallAnnotation = method.getAnnotation(LoggerCallAnnotation.class);
            //获取spring sple表达式
            String spel = loggerCallAnnotation.recordCode();
            //获取方法参数名
            String[] params = discoverer.getParameterNames(method);
            //将参数纳入Spring管理
            EvaluationContext context = new StandardEvaluationContext();
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], args[len]);
            }
            Expression expression = parser.parseExpression(spel);
            res = expression.getValue(context, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

}
