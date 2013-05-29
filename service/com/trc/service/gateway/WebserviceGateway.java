package com.trc.service.gateway;

import java.net.URL;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.trc.config.Config;
import com.tscp.mvne.TSCPMVNA;
import com.tscp.mvne.TSCPMVNAService;

@Service
@Scope("singleton")
public class WebserviceGateway {
	public static String serviceName;
	public static String namespace;
	public static String location;
	public static boolean initialized = false;

	private static TSCPMVNAService service;
	private static TSCPMVNA port;

	private static final Logger logger = LoggerFactory.getLogger(WebserviceGateway.class);

	@PostConstruct
	public void init() throws Exception {
		if (!Config.initialized)
			Config.init();

		try {
			service = new TSCPMVNAService(new URL(location), new QName(namespace, serviceName));
			port = service.getTSCPMVNAPort();

			logger.info("WebService initialized to " + service.getWSDLDocumentLocation().toString());

			Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
			requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 60000);
			requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 60000);

			initialized = true;
		} catch (Exception e) {
			initialized = false;
			logger.error("Exception initializing WebService at " + location, e);
			throw e;
		}
	}

	public TSCPMVNA getPort() {
		return port;
	}

}