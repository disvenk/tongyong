package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ElectronicTicketConfig;

public interface ElectronicTicketConfigService extends GenericService<ElectronicTicketConfig, Long> {

    ElectronicTicketConfig selectByBrandId(String brandId);
}
