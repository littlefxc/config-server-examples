# config-server-examples

分布式配置中心示例

## Apollo 示例

```sh
java -Denv=DEV -Dapollo.bootstrap.namespaces=application,application-v1,DEV.common \
-Dapollo.configService=http://192.168.200.19:30002 -jar application-0.0.1-SNAPSHOT.jar \
--server.port=8050 --spring.cloud.consul.discovery.ip-address=192.168.212.67
```
