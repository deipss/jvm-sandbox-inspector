package me.deipss.jvm.sandbox.inspector.web.domain.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    private int pageNum;
    private int pageSize;
}
