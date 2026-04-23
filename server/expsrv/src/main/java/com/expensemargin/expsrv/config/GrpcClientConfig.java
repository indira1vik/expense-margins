package com.expensemargin.expsrv.config;

import org.springframework.context.annotation.Configuration;

/**
 * gRPC Client Configuration
 * 
 * This configuration is automatically handled by grpc-spring-boot-starter.
 * Client stub injection is done via @GrpcClient annotation in services.
 * 
 * Configuration properties are defined in application.properties:
 * - grpc.client.user-service.address=static://localhost:9091
 * - grpc.client.user-service.negotiationType=plaintext
 */
@Configuration
public class GrpcClientConfig {
    // Configuration is handled by spring-grpc boot starter via properties
    // No additional beans needed
}
