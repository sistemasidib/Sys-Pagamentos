package br.com.bioregistro.flowbank.exception.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@RegisterForReflection
public class MsgRetorno {

    public boolean success;
    public String msg;
    public Object data;

}