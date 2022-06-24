package com.jh.job.admin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class JobGroup {

    private int id;

    private String name;
    
    private String addressList;

    public List<String> getAddressAsList() {
        List<String> registryList = new ArrayList<>();
        if (addressList!=null && addressList.trim().length()>0) {
            registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }
}
