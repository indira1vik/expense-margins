package com.expensemargin.usersrv.service;

import com.expensemargin.grpc.UserRequest;
import com.expensemargin.grpc.UserResponse;
import com.expensemargin.grpc.UserServiceGrpc;
import com.expensemargin.usersrv.repository.UserProfileRepo;
import com.expensemargin.usersrv.entity.UserProfile;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserProfileRepo repository;

    public UserGrpcService(UserProfileRepo repository) {
        this.repository = repository;
    }

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {

        UserProfile user = repository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = UserResponse.newBuilder()
                .setUserId(user.getUserId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}