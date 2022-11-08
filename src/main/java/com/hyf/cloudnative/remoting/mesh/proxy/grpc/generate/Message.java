// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: remoting_api.proto

package com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate;

/**
 * Protobuf type {@code remoting.v1.Message}
 */
public final class Message extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:remoting.v1.Message)
    MessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Message.newBuilder() to construct.
  private Message(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Message() {
    className_ = com.google.protobuf.ByteString.EMPTY;
    methodName_ = com.google.protobuf.ByteString.EMPTY;
    argClassName_ = com.google.protobuf.ByteString.EMPTY;
    payloadClassName_ = com.google.protobuf.ByteString.EMPTY;
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new Message();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return RemotingApiProto.internal_static_remoting_v1_Message_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 9:
        return internalGetMetadata();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return RemotingApiProto.internal_static_remoting_v1_Message_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            Message.class, Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private int id_;
  /**
   * <code>int32 id = 1;</code>
   * @return The id.
   */
  @Override
  public int getId() {
    return id_;
  }

  public static final int REQ_FIELD_NUMBER = 2;
  private boolean req_;
  /**
   * <code>bool req = 2;</code>
   * @return The req.
   */
  @Override
  public boolean getReq() {
    return req_;
  }

  public static final int EVENT_FIELD_NUMBER = 3;
  private boolean event_;
  /**
   * <code>bool event = 3;</code>
   * @return The event.
   */
  @Override
  public boolean getEvent() {
    return event_;
  }

  public static final int CLASS_NAME_FIELD_NUMBER = 4;
  private com.google.protobuf.ByteString className_;
  /**
   * <code>bytes class_name = 4;</code>
   * @return The className.
   */
  @Override
  public com.google.protobuf.ByteString getClassName() {
    return className_;
  }

  public static final int METHOD_NAME_FIELD_NUMBER = 5;
  private com.google.protobuf.ByteString methodName_;
  /**
   * <code>bytes method_name = 5;</code>
   * @return The methodName.
   */
  @Override
  public com.google.protobuf.ByteString getMethodName() {
    return methodName_;
  }

  public static final int ARG_CLASS_NAME_FIELD_NUMBER = 6;
  private com.google.protobuf.ByteString argClassName_;
  /**
   * <code>bytes arg_class_name = 6;</code>
   * @return The argClassName.
   */
  @Override
  public com.google.protobuf.ByteString getArgClassName() {
    return argClassName_;
  }

  public static final int PAYLOAD_CLASS_NAME_FIELD_NUMBER = 7;
  private com.google.protobuf.ByteString payloadClassName_;
  /**
   * <code>bytes payload_class_name = 7;</code>
   * @return The payloadClassName.
   */
  @Override
  public com.google.protobuf.ByteString getPayloadClassName() {
    return payloadClassName_;
  }

  public static final int PAYLOAD_FIELD_NUMBER = 8;
  private com.google.protobuf.Any payload_;
  /**
   * <code>.google.protobuf.Any payload = 8;</code>
   * @return Whether the payload field is set.
   */
  @Override
  public boolean hasPayload() {
    return payload_ != null;
  }
  /**
   * <code>.google.protobuf.Any payload = 8;</code>
   * @return The payload.
   */
  @Override
  public com.google.protobuf.Any getPayload() {
    return payload_ == null ? com.google.protobuf.Any.getDefaultInstance() : payload_;
  }
  /**
   * <code>.google.protobuf.Any payload = 8;</code>
   */
  @Override
  public com.google.protobuf.AnyOrBuilder getPayloadOrBuilder() {
    return getPayload();
  }

  public static final int METADATA_FIELD_NUMBER = 9;
  private static final class MetadataDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        String, String> defaultEntry =
            com.google.protobuf.MapEntry
            .<String, String>newDefaultInstance(
                RemotingApiProto.internal_static_remoting_v1_Message_MetadataEntry_descriptor,
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.STRING,
                "");
  }
  private com.google.protobuf.MapField<
      String, String> metadata_;
  private com.google.protobuf.MapField<String, String>
  internalGetMetadata() {
    if (metadata_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          MetadataDefaultEntryHolder.defaultEntry);
    }
    return metadata_;
  }

  public int getMetadataCount() {
    return internalGetMetadata().getMap().size();
  }
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */

  @Override
  public boolean containsMetadata(
      String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    return internalGetMetadata().getMap().containsKey(key);
  }
  /**
   * Use {@link #getMetadataMap()} instead.
   */
  @Override
  @Deprecated
  public java.util.Map<String, String> getMetadata() {
    return getMetadataMap();
  }
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */
  @Override

  public java.util.Map<String, String> getMetadataMap() {
    return internalGetMetadata().getMap();
  }
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */
  @Override

  public String getMetadataOrDefault(
      String key,
      String defaultValue) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<String, String> map =
        internalGetMetadata().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <code>map&lt;string, string&gt; metadata = 9;</code>
   */
  @Override

  public String getMetadataOrThrow(
      String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<String, String> map =
        internalGetMetadata().getMap();
    if (!map.containsKey(key)) {
      throw new IllegalArgumentException();
    }
    return map.get(key);
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (id_ != 0) {
      output.writeInt32(1, id_);
    }
    if (req_ != false) {
      output.writeBool(2, req_);
    }
    if (event_ != false) {
      output.writeBool(3, event_);
    }
    if (!className_.isEmpty()) {
      output.writeBytes(4, className_);
    }
    if (!methodName_.isEmpty()) {
      output.writeBytes(5, methodName_);
    }
    if (!argClassName_.isEmpty()) {
      output.writeBytes(6, argClassName_);
    }
    if (!payloadClassName_.isEmpty()) {
      output.writeBytes(7, payloadClassName_);
    }
    if (payload_ != null) {
      output.writeMessage(8, getPayload());
    }
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetMetadata(),
        MetadataDefaultEntryHolder.defaultEntry,
        9);
    getUnknownFields().writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, id_);
    }
    if (req_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, req_);
    }
    if (event_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, event_);
    }
    if (!className_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(4, className_);
    }
    if (!methodName_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(5, methodName_);
    }
    if (!argClassName_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(6, argClassName_);
    }
    if (!payloadClassName_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(7, payloadClassName_);
    }
    if (payload_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(8, getPayload());
    }
    for (java.util.Map.Entry<String, String> entry
         : internalGetMetadata().getMap().entrySet()) {
      com.google.protobuf.MapEntry<String, String>
      metadata__ = MetadataDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, metadata__);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof Message)) {
      return super.equals(obj);
    }
    Message other = (Message) obj;

    if (getId()
        != other.getId()) return false;
    if (getReq()
        != other.getReq()) return false;
    if (getEvent()
        != other.getEvent()) return false;
    if (!getClassName()
        .equals(other.getClassName())) return false;
    if (!getMethodName()
        .equals(other.getMethodName())) return false;
    if (!getArgClassName()
        .equals(other.getArgClassName())) return false;
    if (!getPayloadClassName()
        .equals(other.getPayloadClassName())) return false;
    if (hasPayload() != other.hasPayload()) return false;
    if (hasPayload()) {
      if (!getPayload()
          .equals(other.getPayload())) return false;
    }
    if (!internalGetMetadata().equals(
        other.internalGetMetadata())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getId();
    hash = (37 * hash) + REQ_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getReq());
    hash = (37 * hash) + EVENT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getEvent());
    hash = (37 * hash) + CLASS_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getClassName().hashCode();
    hash = (37 * hash) + METHOD_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getMethodName().hashCode();
    hash = (37 * hash) + ARG_CLASS_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getArgClassName().hashCode();
    hash = (37 * hash) + PAYLOAD_CLASS_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getPayloadClassName().hashCode();
    if (hasPayload()) {
      hash = (37 * hash) + PAYLOAD_FIELD_NUMBER;
      hash = (53 * hash) + getPayload().hashCode();
    }
    if (!internalGetMetadata().getMap().isEmpty()) {
      hash = (37 * hash) + METADATA_FIELD_NUMBER;
      hash = (53 * hash) + internalGetMetadata().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static Message parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Message parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Message parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Message parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Message parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Message parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Message parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Message parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static Message parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static Message parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static Message parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Message parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(Message prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code remoting.v1.Message}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:remoting.v1.Message)
      MessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return RemotingApiProto.internal_static_remoting_v1_Message_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 9:
          return internalGetMetadata();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 9:
          return internalGetMutableMetadata();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return RemotingApiProto.internal_static_remoting_v1_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Message.class, Builder.class);
    }

    // Construct using com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message.newBuilder()
    private Builder() {

    }

    private Builder(
        BuilderParent parent) {
      super(parent);

    }
    @Override
    public Builder clear() {
      super.clear();
      id_ = 0;

      req_ = false;

      event_ = false;

      className_ = com.google.protobuf.ByteString.EMPTY;

      methodName_ = com.google.protobuf.ByteString.EMPTY;

      argClassName_ = com.google.protobuf.ByteString.EMPTY;

      payloadClassName_ = com.google.protobuf.ByteString.EMPTY;

      if (payloadBuilder_ == null) {
        payload_ = null;
      } else {
        payload_ = null;
        payloadBuilder_ = null;
      }
      internalGetMutableMetadata().clear();
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return RemotingApiProto.internal_static_remoting_v1_Message_descriptor;
    }

    @Override
    public Message getDefaultInstanceForType() {
      return Message.getDefaultInstance();
    }

    @Override
    public Message build() {
      Message result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public Message buildPartial() {
      Message result = new Message(this);
      int from_bitField0_ = bitField0_;
      result.id_ = id_;
      result.req_ = req_;
      result.event_ = event_;
      result.className_ = className_;
      result.methodName_ = methodName_;
      result.argClassName_ = argClassName_;
      result.payloadClassName_ = payloadClassName_;
      if (payloadBuilder_ == null) {
        result.payload_ = payload_;
      } else {
        result.payload_ = payloadBuilder_.build();
      }
      result.metadata_ = internalGetMetadata();
      result.metadata_.makeImmutable();
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof Message) {
        return mergeFrom((Message)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(Message other) {
      if (other == Message.getDefaultInstance()) return this;
      if (other.getId() != 0) {
        setId(other.getId());
      }
      if (other.getReq() != false) {
        setReq(other.getReq());
      }
      if (other.getEvent() != false) {
        setEvent(other.getEvent());
      }
      if (other.getClassName() != com.google.protobuf.ByteString.EMPTY) {
        setClassName(other.getClassName());
      }
      if (other.getMethodName() != com.google.protobuf.ByteString.EMPTY) {
        setMethodName(other.getMethodName());
      }
      if (other.getArgClassName() != com.google.protobuf.ByteString.EMPTY) {
        setArgClassName(other.getArgClassName());
      }
      if (other.getPayloadClassName() != com.google.protobuf.ByteString.EMPTY) {
        setPayloadClassName(other.getPayloadClassName());
      }
      if (other.hasPayload()) {
        mergePayload(other.getPayload());
      }
      internalGetMutableMetadata().mergeFrom(
          other.internalGetMetadata());
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              id_ = input.readInt32();

              break;
            } // case 8
            case 16: {
              req_ = input.readBool();

              break;
            } // case 16
            case 24: {
              event_ = input.readBool();

              break;
            } // case 24
            case 34: {
              className_ = input.readBytes();

              break;
            } // case 34
            case 42: {
              methodName_ = input.readBytes();

              break;
            } // case 42
            case 50: {
              argClassName_ = input.readBytes();

              break;
            } // case 50
            case 58: {
              payloadClassName_ = input.readBytes();

              break;
            } // case 58
            case 66: {
              input.readMessage(
                  getPayloadFieldBuilder().getBuilder(),
                  extensionRegistry);

              break;
            } // case 66
            case 74: {
              com.google.protobuf.MapEntry<String, String>
              metadata__ = input.readMessage(
                  MetadataDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
              internalGetMutableMetadata().getMutableMap().put(
                  metadata__.getKey(), metadata__.getValue());
              break;
            } // case 74
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private int id_ ;
    /**
     * <code>int32 id = 1;</code>
     * @return The id.
     */
    @Override
    public int getId() {
      return id_;
    }
    /**
     * <code>int32 id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(int value) {
      
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      
      id_ = 0;
      onChanged();
      return this;
    }

    private boolean req_ ;
    /**
     * <code>bool req = 2;</code>
     * @return The req.
     */
    @Override
    public boolean getReq() {
      return req_;
    }
    /**
     * <code>bool req = 2;</code>
     * @param value The req to set.
     * @return This builder for chaining.
     */
    public Builder setReq(boolean value) {
      
      req_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bool req = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearReq() {
      
      req_ = false;
      onChanged();
      return this;
    }

    private boolean event_ ;
    /**
     * <code>bool event = 3;</code>
     * @return The event.
     */
    @Override
    public boolean getEvent() {
      return event_;
    }
    /**
     * <code>bool event = 3;</code>
     * @param value The event to set.
     * @return This builder for chaining.
     */
    public Builder setEvent(boolean value) {
      
      event_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bool event = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearEvent() {
      
      event_ = false;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString className_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes class_name = 4;</code>
     * @return The className.
     */
    @Override
    public com.google.protobuf.ByteString getClassName() {
      return className_;
    }
    /**
     * <code>bytes class_name = 4;</code>
     * @param value The className to set.
     * @return This builder for chaining.
     */
    public Builder setClassName(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      className_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes class_name = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearClassName() {
      
      className_ = getDefaultInstance().getClassName();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString methodName_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes method_name = 5;</code>
     * @return The methodName.
     */
    @Override
    public com.google.protobuf.ByteString getMethodName() {
      return methodName_;
    }
    /**
     * <code>bytes method_name = 5;</code>
     * @param value The methodName to set.
     * @return This builder for chaining.
     */
    public Builder setMethodName(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      methodName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes method_name = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearMethodName() {
      
      methodName_ = getDefaultInstance().getMethodName();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString argClassName_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes arg_class_name = 6;</code>
     * @return The argClassName.
     */
    @Override
    public com.google.protobuf.ByteString getArgClassName() {
      return argClassName_;
    }
    /**
     * <code>bytes arg_class_name = 6;</code>
     * @param value The argClassName to set.
     * @return This builder for chaining.
     */
    public Builder setArgClassName(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      argClassName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes arg_class_name = 6;</code>
     * @return This builder for chaining.
     */
    public Builder clearArgClassName() {
      
      argClassName_ = getDefaultInstance().getArgClassName();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString payloadClassName_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes payload_class_name = 7;</code>
     * @return The payloadClassName.
     */
    @Override
    public com.google.protobuf.ByteString getPayloadClassName() {
      return payloadClassName_;
    }
    /**
     * <code>bytes payload_class_name = 7;</code>
     * @param value The payloadClassName to set.
     * @return This builder for chaining.
     */
    public Builder setPayloadClassName(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      payloadClassName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes payload_class_name = 7;</code>
     * @return This builder for chaining.
     */
    public Builder clearPayloadClassName() {
      
      payloadClassName_ = getDefaultInstance().getPayloadClassName();
      onChanged();
      return this;
    }

    private com.google.protobuf.Any payload_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Any, com.google.protobuf.Any.Builder, com.google.protobuf.AnyOrBuilder> payloadBuilder_;
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     * @return Whether the payload field is set.
     */
    public boolean hasPayload() {
      return payloadBuilder_ != null || payload_ != null;
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     * @return The payload.
     */
    public com.google.protobuf.Any getPayload() {
      if (payloadBuilder_ == null) {
        return payload_ == null ? com.google.protobuf.Any.getDefaultInstance() : payload_;
      } else {
        return payloadBuilder_.getMessage();
      }
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    public Builder setPayload(com.google.protobuf.Any value) {
      if (payloadBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        payload_ = value;
        onChanged();
      } else {
        payloadBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    public Builder setPayload(
        com.google.protobuf.Any.Builder builderForValue) {
      if (payloadBuilder_ == null) {
        payload_ = builderForValue.build();
        onChanged();
      } else {
        payloadBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    public Builder mergePayload(com.google.protobuf.Any value) {
      if (payloadBuilder_ == null) {
        if (payload_ != null) {
          payload_ =
            com.google.protobuf.Any.newBuilder(payload_).mergeFrom(value).buildPartial();
        } else {
          payload_ = value;
        }
        onChanged();
      } else {
        payloadBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    public Builder clearPayload() {
      if (payloadBuilder_ == null) {
        payload_ = null;
        onChanged();
      } else {
        payload_ = null;
        payloadBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    public com.google.protobuf.Any.Builder getPayloadBuilder() {
      
      onChanged();
      return getPayloadFieldBuilder().getBuilder();
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    public com.google.protobuf.AnyOrBuilder getPayloadOrBuilder() {
      if (payloadBuilder_ != null) {
        return payloadBuilder_.getMessageOrBuilder();
      } else {
        return payload_ == null ?
            com.google.protobuf.Any.getDefaultInstance() : payload_;
      }
    }
    /**
     * <code>.google.protobuf.Any payload = 8;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Any, com.google.protobuf.Any.Builder, com.google.protobuf.AnyOrBuilder> 
        getPayloadFieldBuilder() {
      if (payloadBuilder_ == null) {
        payloadBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Any, com.google.protobuf.Any.Builder, com.google.protobuf.AnyOrBuilder>(
                getPayload(),
                getParentForChildren(),
                isClean());
        payload_ = null;
      }
      return payloadBuilder_;
    }

    private com.google.protobuf.MapField<
        String, String> metadata_;
    private com.google.protobuf.MapField<String, String>
    internalGetMetadata() {
      if (metadata_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            MetadataDefaultEntryHolder.defaultEntry);
      }
      return metadata_;
    }
    private com.google.protobuf.MapField<String, String>
    internalGetMutableMetadata() {
      onChanged();;
      if (metadata_ == null) {
        metadata_ = com.google.protobuf.MapField.newMapField(
            MetadataDefaultEntryHolder.defaultEntry);
      }
      if (!metadata_.isMutable()) {
        metadata_ = metadata_.copy();
      }
      return metadata_;
    }

    public int getMetadataCount() {
      return internalGetMetadata().getMap().size();
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */

    @Override
    public boolean containsMetadata(
        String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      return internalGetMetadata().getMap().containsKey(key);
    }
    /**
     * Use {@link #getMetadataMap()} instead.
     */
    @Override
    @Deprecated
    public java.util.Map<String, String> getMetadata() {
      return getMetadataMap();
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */
    @Override

    public java.util.Map<String, String> getMetadataMap() {
      return internalGetMetadata().getMap();
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */
    @Override

    public String getMetadataOrDefault(
        String key,
        String defaultValue) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<String, String> map =
          internalGetMetadata().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */
    @Override

    public String getMetadataOrThrow(
        String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<String, String> map =
          internalGetMetadata().getMap();
      if (!map.containsKey(key)) {
        throw new IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearMetadata() {
      internalGetMutableMetadata().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */

    public Builder removeMetadata(
        String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      internalGetMutableMetadata().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @Deprecated
    public java.util.Map<String, String>
    getMutableMetadata() {
      return internalGetMutableMetadata().getMutableMap();
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */
    public Builder putMetadata(
        String key,
        String value) {
      if (key == null) { throw new NullPointerException("map key"); }
      if (value == null) {
  throw new NullPointerException("map value");
}

      internalGetMutableMetadata().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <code>map&lt;string, string&gt; metadata = 9;</code>
     */

    public Builder putAllMetadata(
        java.util.Map<String, String> values) {
      internalGetMutableMetadata().getMutableMap()
          .putAll(values);
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:remoting.v1.Message)
  }

  // @@protoc_insertion_point(class_scope:remoting.v1.Message)
  private static final Message DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new Message();
  }

  public static Message getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Message>
      PARSER = new com.google.protobuf.AbstractParser<Message>() {
    @Override
    public Message parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<Message> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<Message> getParserForType() {
    return PARSER;
  }

  @Override
  public Message getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

