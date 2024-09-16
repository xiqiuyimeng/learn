package org.demo.learn.util.function;

import org.demo.learn.service.MyFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 本例，包含，单个参数 function，双参数 biFunction，三参数，自定义 myFunction
 * @author luwt-a
 * @date 2024/9/16
 */
public class FunctionUtil {


    public void function() {
        // 单参数
        testOneParam(this::doTestOneParam, 1);
    }

    private void testOneParam(Function<Integer, String> func, Integer param) {
        String apply = func.apply(param);
    }

    private String doTestOneParam(Integer param) {
        return String.valueOf(param);
    }


    public void functionTwoParam() {
        // 双参数
        testTwoParam(this::doTestTwoParam, 1, 1);
    }

    private void testTwoParam(BiFunction<Integer, Integer, String> func, Integer param, Integer param2) {
        String apply = func.apply(param, param2);
    }

    private String doTestTwoParam(Integer param, Integer param2) {
        return String.valueOf(param);
    }


    public void functionThreeParam() {
        // 三参数
        testThreeParam(this::doTestThreeParam, 1, 2, 3);
    }

    private void testThreeParam(MyFunction<Integer, Integer, Integer, String> func, Integer param, Integer param2, Integer param3) {
        String apply = func.apply(param, param2, param3);
    }

    private String doTestThreeParam(Integer param, Integer param2, Integer param3) {
        return String.valueOf(param);
    }
}
