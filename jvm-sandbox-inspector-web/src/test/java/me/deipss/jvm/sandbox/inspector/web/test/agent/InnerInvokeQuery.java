package me.deipss.jvm.sandbox.inspector.web.test.agent;

import me.deipss.jvm.sandbox.inspector.web.test.agent.domain.QueryDTO;
import me.deipss.jvm.sandbox.inspector.web.test.agent.domain.QueryResultDTO;

public class InnerInvokeQuery {

    public QueryResultDTO query(QueryDTO queryDTO){
        QueryResultDTO queryResultDTO = new QueryResultDTO();
        queryResultDTO.setName(queryDTO.getName());
        queryResultDTO.setOrderId(queryDTO.getOrderId());
        queryResultDTO.setUserId(queryDTO.getUserId());
        queryResultDTO.setStartTime(queryDTO.getStartTime());
        System.out.println(queryResultDTO);
        return queryResultDTO;
    }
}
