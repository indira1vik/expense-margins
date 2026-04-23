package com.expensemargin.usersrv.config;

import org.springframework.context.annotation.Configuration;

/**
 * gRPC Server Configuration
 * 
 * This configuration is automatically handled by grpc-spring-boot-starter.
 * Services are registered via @GrpcService annotation.
 * 
 * Configuration properties are defined in application.properties:
 * - grpc.server.port=9091
 */
@Configuration
public class GrpcServerConfig {
    // Configuration is handled by spring-grpc boot starter via properties
    // Services are auto-discovered and registered via @GrpcService annotation
}
