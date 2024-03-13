package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.MockType;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.Operate;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockManageRequest implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 操作
     */
    private Operate operate;


    /**
     * 具体的请求数据
     */
    private List<Inner> mockInnerList;



    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Inner implements Serializable {
        private static final long serialVersionUID = -1L;

        /**
         * mock的类型
         */
        private MockType mockType;

        /**
         * 返回的数据
         */
        private String mockResponseJson;

        /**
         * mockId
         */
        private Integer mockId;

        /**
         * 类名
         */
        private String mockClass;

        /**
         * 方法名
         */
        private String mockMethod;

        /**
         * 是否包含子类
         */
        private boolean includeSubClass;

        /**
         * 方法签名中的类型数据
         */
        private String [] parameterTypes;

        /**
         * 异常的全类名
         */
        private String exceptionClassCanonicalName;

        /**
         * 返回结果的全类名
         */
        private String returnClassCanonicalName;

    }
}
