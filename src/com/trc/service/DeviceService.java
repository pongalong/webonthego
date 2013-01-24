package com.trc.service;

import java.util.List;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.exception.service.DeviceServiceException;
import com.trc.service.gateway.WebserviceGateway;
import com.trc.service.gateway.WebserviceAdapter;
import com.trc.user.User;
import com.tscp.mvne.Account;
import com.tscp.mvne.BillingException_Exception;
import com.tscp.mvne.Device;
import com.tscp.mvne.DeviceException_Exception;
import com.tscp.mvne.NetworkException_Exception;
import com.tscp.mvne.NetworkInfo;
import com.tscp.mvne.ProvisionException_Exception;
import com.tscp.mvne.ServiceInstance;
import com.tscp.mvne.TSCPMVNA;

@Service
public class DeviceService implements DeviceServiceModel {
  private TSCPMVNA port;

  @Autowired
  public void init(WebserviceGateway gateway) {
    this.port = gateway.getPort();
  }

  @Override
  public NetworkInfo getNetworkInfo(String esn, String msid) throws DeviceServiceException {
    try {
      return port.getNetworkInfo(esn, msid);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (NetworkException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void updateDeviceInfo(User user, Device deviceInfo) throws DeviceServiceException {
    try {
      port.updateDeviceInfoObject(WebserviceAdapter.toCustomer(user), deviceInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public List<Device> getDeviceInfoList(User user) throws DeviceServiceException {
    try {
      return port.getDeviceList(WebserviceAdapter.toCustomer(user));
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public NetworkInfo swapDevice(User user, NetworkInfo oldNetworkInfo, Device deviceInfo) throws DeviceServiceException {
    try {
      return port.swapDevice(WebserviceAdapter.toCustomer(user), oldNetworkInfo, deviceInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public NetworkInfo reserveMdn() throws DeviceServiceException {
    try {
      return port.reserveMDN();
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public Device addDeviceInfo(User user, Device deviceInfo) throws DeviceServiceException {
    try {
      return port.addDeviceInfoObject(WebserviceAdapter.toCustomer(user), deviceInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public List<Device> deleteDeviceInfo(User user, Device deviceInfo) throws DeviceServiceException {
    try {
      return port.deleteDeviceInfoObject(WebserviceAdapter.toCustomer(user), deviceInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void suspendService(int userId, int accountNo, int deviceId) throws DeviceServiceException {
    try {
      port.suspendAccount(userId, accountNo, deviceId);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (ProvisionException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (NetworkException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (DeviceException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (BillingException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void restoreService(int userId, int accountNo, int deviceId) throws DeviceServiceException {
    try {
      port.restoreAccount(userId, accountNo, deviceId);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (ProvisionException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (NetworkException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (DeviceException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    } catch (BillingException_Exception e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public NetworkInfo activateService(User user, NetworkInfo networkInfo) throws DeviceServiceException {
    try {
      return port.activateService(WebserviceAdapter.toCustomer(user), networkInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public Account createServiceInstance(Account account, ServiceInstance serviceInstance) throws DeviceServiceException {
    try {
      return port.createServiceInstance(account, serviceInstance);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void disconnectService(ServiceInstance serviceInstance) throws DeviceServiceException {
    try {
      port.disconnectService(serviceInstance);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void disconnectFromNetwork(NetworkInfo networkInfo) throws DeviceServiceException {
    try {
      port.disconnectFromNetwork(networkInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void disconnectFromKenan(Account account, ServiceInstance serviceInstance) throws DeviceServiceException {
    try {
      port.disconnectServiceInstanceFromKenan(account, serviceInstance);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public NetworkInfo reinstallCustomerDevice(User user, Device deviceInfo) throws DeviceServiceException {
    try {
      return port.reinstallCustomerDevice(WebserviceAdapter.toCustomer(user), deviceInfo);
    } catch (WebServiceException e) {
      throw new DeviceServiceException(e.getMessage(), e.getCause());
    }
  }

}
