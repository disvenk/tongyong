package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Examine;

public interface ExamineService extends GenericService<Examine, Long> {

    Examine selectByContractIdAndType(Long contractId, Integer type);
}
