Cipop-back: Commons作为公共的信息导入配置模块
==

Development Requirements
===
- java8
- maven3
- intelliJ

Spring Frameworks
- Spring boot
- Spring Web
- Spring Security
- Spring Data
- Spring Rest

launch
======
```mvn spring-boot:run```

generate source code
====================
```mvn clean package -Dmaven.test.skip=true```

coverage
========
```mvn clean package```

generate api-docs
=================
execute ApiDocumentationTest
```mvn clean package -Dmaven.test.skip=true```

```schedule backup heroku pg:backups:schedule DATABASE_URL --at '02:00 Europe/Paris' --app ftl-oms


