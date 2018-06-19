package com.cipop.provider.client.repository;


import com.cipop.base.repository.BaseJpaRepository;
import com.cipop.bean.Client;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: ClientRepository
 * @Description: ClientRepository
 * @Author: Yang Naihua
 * @Create: 2018-06-20 10:33
 **/
@Repository
public class ClientRepository extends BaseJpaRepository<Client, String> {
    public ClientRepository() {
        super(Client.class);
    }

//    public List<String> findAllIds() {
//        return getEm().createQuery("SELECT c.id from Client c", String.class).getResultList();
//    }
}