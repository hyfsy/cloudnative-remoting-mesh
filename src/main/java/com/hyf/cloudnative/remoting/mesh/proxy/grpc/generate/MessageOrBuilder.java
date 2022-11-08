// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: remoting_api.proto

package com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate;

public interface MessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:remoting.v1.Message)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>bool req = 2;</code>
   * @return The req.
   */
  boolean getReq();

  /**
   * <code>bool event = 3;</code>
   * @return The event.
   */
  boolean getEvent();

  /**
   * <code>bytes class_name = 4;</code>
   * @return The className.
   */
  com.google.protobuf.ByteString getClassName();

  /**
   * <code>bytes method_name = 5;</code>
   * @return The methodName.
   */
  com.google.protobuf.ByteString getMethodName();

  /**
   * <code>bytes arg_class_name = 6;</code>
   * @return The argClassName.
   */
  com.google.protobuf.ByteString getArgClassName();

  /**
   * <code>bytes payload_class_name = 7;</code>
   * @return The payloadClassName.
   */
  com.google.protobuf.ByteString getPayloadClassName();

  /**
   * <code>.google.protobuf.Any payload = 8;</code>
   * @return Whether the payload field is set.
   */
  boolean hasPayload();
  /**
   * <code>.google.protobuf.Any payload = 8;</code>
   * @return The payload.
   */
  com.google.protobuf.Any getPayload();
  /**
   * <code>.google.protobuf.Any payload = 8;</code>
   */
  com.google.protobuf.AnyOrBuilder getPayloadOrBuilder();

  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */
  int getMetadataCount();
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */
  boolean containsMetadata(
      String key);
  /**
   * Use {@link #getMetadataMap()} instead.
   */
  @Deprecated
  java.util.Map<String, String>
  getMetadata();
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */
  java.util.Map<String, String>
  getMetadataMap();
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */

  /* nullable */
String getMetadataOrDefault(
      String key,
      /* nullable */
String defaultValue);
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */

  String getMetadataOrThrow(
      String key);
}
