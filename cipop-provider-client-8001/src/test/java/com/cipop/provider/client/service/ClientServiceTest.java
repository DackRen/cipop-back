package com.cipop.provider.client.service;

import com.cipop.provider.client.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.cipop.bean", "com.cipop.base", "com.cipop.provider.client"})
@Sql("/sql/clients.sql")
@Transactional
public class ClientServiceTest {
    //    @Autowired
//    @Resource(name = "clientServiceImpl")
//    private IClientService clientServiceImpl;
    @Resource
    private ClientServiceImpl clientService;

    @Test
    public void findById() {
//        Client client = clientService.get("1");
//clientService.get("1");
//        assertEquals(client.getId(),"1");
//        assertEquals(client.getEmail(),"1@ftl-express.fr");
//        assertEquals(client.getName(),"1");
//        assertEquals(client.getPhoneNumber(),"33 710 35455888");
    }
//
//    @Test
//    public void save() {
//        Client client = new Client();
//        client.setId("99");
//        client.setEmail("99@gmail.com");
//        client.setName("天巡");
//        client.setPhoneNumber("18888888");
//
//        Client destination = clientService.save(client);
//
//        assertEquals(client.getId(),destination.getId());
//        assertEquals(client.getEmail(),destination.getEmail());
//        assertEquals(client.getName(),destination.getName());
//        assertEquals(client.getPhoneNumber(),destination.getPhoneNumber());
//    }
//
//    @Test
//    public void createPermanentToken() {
//        System.err.println(clientService.getToken("ADMIN"));
//    }
}
