package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.http.copier.HttpServletResponseCopier;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpInvocation extends Invocation {

    private transient HttpServletResponseCopier httpServletResponseCopier;
}
