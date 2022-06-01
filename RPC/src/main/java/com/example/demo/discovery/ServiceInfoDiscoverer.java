package com.example.demo.discovery;

import java.util.List;

public interface ServiceInfoDiscoverer {
    List<ServiceInfo> getServiceInfo(String name);
}
