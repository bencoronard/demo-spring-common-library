package dev.hireben.demo.common_libs.annotation.authorization;
// package dev.hireben.demo.common.grpc.interceptor;

// import org.springframework.web.service.invoker.HttpRequestValues.Metadata;

// import dev.hireben.demo.common.constant.ApiHeaderKey;
// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// public class ApiKeyInterceptor implements ServerInterceptor {

// private final String expectedApiKey;

// //
// =============================================================================

// @Override
// public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
// ServerCall<ReqT, RespT> call,
// Metadata headers,
// ServerCallHandler<ReqT, RespT> next) {

// String reqApiKey = headers.get(Metadata.Key.of(ApiHeaderKey.API_KEY,
// Metadata.ASCII_STRING_MARSHALLER));

// if (reqApiKey == null || reqApiKey.isBlank()) {
// call.close(Status.UNAUTHENTICATED.withDescription("Missing API key"), new
// Metadata());
// return new ServerCall.ListenerM<>() {
// };
// }

// if (!reqApiKey.strip().equals(expectedApiKey)) {
// call.close(Status.UNAUTHENTICATED.withDescription("Invalid API key"), new
// Metadata());
// return new ServerCall.ListenerM<>() {
// };
// }

// return next.startCall(call, headers);
// }

// }
