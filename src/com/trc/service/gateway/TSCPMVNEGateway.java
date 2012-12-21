package com.trc.service.gateway;

import java.net.URL;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import org.springframework.stereotype.Service;

import com.trc.config.Config;
import com.tscp.mvne.TSCPMVNA;
import com.tscp.mvne.TSCPMVNAService;

@Service
public class TSCPMVNEGateway {
	private TSCPMVNAService service;
	private TSCPMVNA port;

	@PostConstruct
	public void init() {
		try {
			if (!TSCPMVNE.initialized) {
				Config.loadProperties();
			}
			String namespace = TSCPMVNE.namespace;
			String servicename = TSCPMVNE.serviceName;
			String location = TSCPMVNE.location;
			service = new TSCPMVNAService(new URL(location), new QName(namespace, servicename));
		} catch (Exception e) {
			e.printStackTrace();
			service = new TSCPMVNAService();
		}
		port = service.getTSCPMVNAPort();
	}

	public TSCPMVNA getPort() {
		return port;
	}
}