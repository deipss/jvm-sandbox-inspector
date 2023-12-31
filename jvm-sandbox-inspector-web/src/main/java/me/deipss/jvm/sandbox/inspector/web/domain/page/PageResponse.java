package me.deipss.jvm.sandbox.inspector.web.domain.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private int pageNum;
    private int pageSize;
    private long total;
    private List<T> records;
    private int code;
    private String msg;
}
