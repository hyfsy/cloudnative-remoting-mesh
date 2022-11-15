

该项目存在原因：微服务接入k8s+istio时，SpringCloud很多功能都不需要，但还存在一些特殊情况：

|        | 功能                                                         |
| ------ | ------------------------------------------------------------ |
| k8s    | 服务注册与发现、配置管理                                     |
| istio  | 负载均衡、熔断、限流、超时、重试、流量/API网关、链路指标收集 |
| 未支持 | 配置动态刷新、服务调用、降级、链路指标展示、分布式事务       |

- **配置动态刷新**可由`spring-cloud-kubernetes-fabric8-config` 类库负责

- **链路指标展示**可由三方链路追踪中间件处理

- **分布式事务**可由seata负责

- **服务调用**可通过RestTemplate调用、**降级**可通过捕获异常处理

从上面可以看到，服务调用和降级这块的功能写起来会比较麻烦，考虑到这种情况，就可以使用此类库达到和OpenFeign一样的服务调用使用方式，而不必引入过重的OpenFeign，减少和SpringCloud框架的耦合。





> 用例参考：https://github.com/hyfsy/mesh-demo






## 1. 网格环境使用



客户端支持`HTTP`和`GRPC`两种通讯方式，用户可根据业务需要进行选择



### 1.1. 引入服务调用依赖

```xml
<dependency>
    <groupId>io.github.hyfsy</groupId>
    <artifactId>cloudnative-remoting-mesh</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```



### 1.2. 编写生产者端代码

1、编写客户端调用接口

```java
// http请求
@K8SClient(
    name = "${k8s.service.demo.name}", // 必填，为k8s服务名称
    port = "${k8s.service.demo.port}", // 可选，为k8s服务端口，http默认8080，grpc默认5443
    requestWay = RequestWay.HTTP 	   // 请求方式为http方式，默认grpc
)
@RequestMapping("user")
public interface TestHttpClient {
    @GetMapping("{id}")
    Result<User> getUserByIdByHttp(@PathVariable("id") Integer id);
}

// grpc请求
@K8SClient("${k8s.service.demo.name}")
public interface TestGrpcClient {
    Result<User> getUserByIdByGrpc(Integer id); // 注意：gRPC情况下方法仅支持 1 个参数
}
```

2、编写生产者端的控制器代码

```java
// http请求
// 和 SpringMVC 一样使用，此处就不写示例了


// grpc请求

@GrpcController // 1. 标记grpc请求的控制器
public class DemoProviderController implements TestGrpcClient { // 2. 必须实现客户端接口

    @Override
    public Result<User> getUserByIdByGrpc(Integer id) {
        return Result.of(new User(id, "grpc_zhagnsan" + id));
    }
}
```



### 1.3. 编写消费者端代码

1、启动类添加自动配置注解

```java
@EnableK8SClients("com.hyf.cloudnative.client.api") // 扫描k8s客户端接口
@SpringBootApplication
public class DemoConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoConsumerApplication.class, args);
    }
}
```

2、配置客户端接口要求的配置

```properties
### application.properties ###

# 生产者端 服务名称
k8s.service.demo.name=service-demo-provider
# 生产者端 HTTP服务端口
k8s.service.demo.port=8080
```

3、注入客户端使用

```java
@RestController
@RequestMapping("demo")
public class DemoConsumerController {

    @Resource
    private TestHttpClient testHttpClient;
    @Resource
    private TestGrpcClient testGrpcClient;

    @GetMapping("http/{id}")
    public Result<User> getByHttp(@PathVariable Integer id) {
        return testHttpClient.getUserByIdByHttp(id);
    }

    @GetMapping("grpc/{id}")
    public Result<User> getByGrpc(@PathVariable Integer id) {
        return testGrpcClient.getUserByIdByGrpc(id);
    }
}
```





## 2. 本地开发测试注意事项

由于服务统一通过k8s内的服务域名调用，所以本地开发测试时，需要在本地`hosts`文件添加主机域名映射

文件路径：`C:\Windows\System32\drivers\etc\hosts`

```
# service-demo-provider为k8s生产者端服务名，default为k8s命名空间名称，cluster.local为k8s集群域名
127.0.0.1 service-demo-provider.default.svc.cluster.local
```



