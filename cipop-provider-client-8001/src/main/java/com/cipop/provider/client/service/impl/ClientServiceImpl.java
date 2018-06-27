package com.cipop.provider.client.service.impl;

import com.cipop.base.exception.ErrorCode;
import com.cipop.base.exception.InternalServerException;
import com.cipop.bean.Client;
import com.cipop.provider.client.repository.ClientRepository;
import com.cipop.provider.client.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ClientServiceImpl
 * @Description: ClientServiceImpl
 * @Author: Yang Naihua
 * @Create: 2018-06-20 10:33
 **/
@Service
public class ClientServiceImpl implements IClientService {
    @Autowired
//@Resource(name = "clientRepository")
//    private IClientService clientServiceImpl;
    private ClientRepository clientRepository;

    @Override
    public Client get(String id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> new InternalServerException(ErrorCode.CLIENT_NOT_FOUND));
    }
//	public Dept get(long id) ;
//	public boolean add(Dept dept) ;
//	public List<Dept> list() ;
}
