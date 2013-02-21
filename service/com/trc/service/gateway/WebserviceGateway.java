package com.trc.service.gateway;

import java.net.URL;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.trc.config.Config;
import com.tscp.mvne.TSCPMVNA;
import com.tscp.mvne.TSCPMVNAService;
import com.tscp.util.logger.DevLogger;

@Service
@Scope("singleton")
public class WebserviceGateway {
	public static String serviceName;
	public static String namespace;
	public static String location;
	public static boolean initialized = false;

	private TSCPMVNAService service;
	private TSCPMVNA port;

	@PostConstruct
	public void init() throws Exception {
		if (!Config.initialized)
			Config.init();

		try {
			service = new TSCPMVNAService(new URL(location), new QName(namespace, serviceName));
			DevLogger.debug("Service initialized to " + service.getWSDLDocumentLocation().toString());
			port = service.getTSCPMVNAPort();
			initialized = true;
		} catch (Exception e) {
			initialized = false;
			DevLogger.debug("Exception initializing service at " + location, e);
			throw e;
		}
	}

	public TSCPMVNA getPort() {
		return port;
	}

}