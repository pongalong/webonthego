package com.tscp.util.profiler;

import java.lang.reflect.Type;

public class GatewayResult {
	public Type returnType;
	public Object returnObject;

	public GatewayResult(Type returnType, Object returnObject) {
		this.returnType = returnType;
		this.returnObject = returnObject;
	}

}