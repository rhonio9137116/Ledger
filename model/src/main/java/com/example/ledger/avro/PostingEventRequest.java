/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.example.ledger.avro;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class PostingEventRequest extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -8441784691909171479L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PostingEventRequest\",\"namespace\":\"com.example.ledger.avro\",\"fields\":[{\"name\":\"postingId\",\"type\":\"long\"},{\"name\":\"userId\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PostingEventRequest> ENCODER =
      new BinaryMessageEncoder<PostingEventRequest>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PostingEventRequest> DECODER =
      new BinaryMessageDecoder<PostingEventRequest>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<PostingEventRequest> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<PostingEventRequest> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<PostingEventRequest>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this PostingEventRequest to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a PostingEventRequest from a ByteBuffer. */
  public static PostingEventRequest fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public long postingId;
  @Deprecated public long userId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PostingEventRequest() {}

  /**
   * All-args constructor.
   * @param postingId The new value for postingId
   * @param userId The new value for userId
   */
  public PostingEventRequest(java.lang.Long postingId, java.lang.Long userId) {
    this.postingId = postingId;
    this.userId = userId;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return postingId;
    case 1: return userId;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: postingId = (java.lang.Long)value$; break;
    case 1: userId = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'postingId' field.
   * @return The value of the 'postingId' field.
   */
  public java.lang.Long getPostingId() {
    return postingId;
  }

  /**
   * Sets the value of the 'postingId' field.
   * @param value the value to set.
   */
  public void setPostingId(java.lang.Long value) {
    this.postingId = value;
  }

  /**
   * Gets the value of the 'userId' field.
   * @return The value of the 'userId' field.
   */
  public java.lang.Long getUserId() {
    return userId;
  }

  /**
   * Sets the value of the 'userId' field.
   * @param value the value to set.
   */
  public void setUserId(java.lang.Long value) {
    this.userId = value;
  }

  /**
   * Creates a new PostingEventRequest RecordBuilder.
   * @return A new PostingEventRequest RecordBuilder
   */
  public static com.example.ledger.avro.PostingEventRequest.Builder newBuilder() {
    return new com.example.ledger.avro.PostingEventRequest.Builder();
  }

  /**
   * Creates a new PostingEventRequest RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PostingEventRequest RecordBuilder
   */
  public static com.example.ledger.avro.PostingEventRequest.Builder newBuilder(com.example.ledger.avro.PostingEventRequest.Builder other) {
    return new com.example.ledger.avro.PostingEventRequest.Builder(other);
  }

  /**
   * Creates a new PostingEventRequest RecordBuilder by copying an existing PostingEventRequest instance.
   * @param other The existing instance to copy.
   * @return A new PostingEventRequest RecordBuilder
   */
  public static com.example.ledger.avro.PostingEventRequest.Builder newBuilder(com.example.ledger.avro.PostingEventRequest other) {
    return new com.example.ledger.avro.PostingEventRequest.Builder(other);
  }

  /**
   * RecordBuilder for PostingEventRequest instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PostingEventRequest>
    implements org.apache.avro.data.RecordBuilder<PostingEventRequest> {

    private long postingId;
    private long userId;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.example.ledger.avro.PostingEventRequest.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.postingId)) {
        this.postingId = data().deepCopy(fields()[0].schema(), other.postingId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.userId)) {
        this.userId = data().deepCopy(fields()[1].schema(), other.userId);
        fieldSetFlags()[1] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing PostingEventRequest instance
     * @param other The existing instance to copy.
     */
    private Builder(com.example.ledger.avro.PostingEventRequest other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.postingId)) {
        this.postingId = data().deepCopy(fields()[0].schema(), other.postingId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.userId)) {
        this.userId = data().deepCopy(fields()[1].schema(), other.userId);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'postingId' field.
      * @return The value.
      */
    public java.lang.Long getPostingId() {
      return postingId;
    }

    /**
      * Sets the value of the 'postingId' field.
      * @param value The value of 'postingId'.
      * @return This builder.
      */
    public com.example.ledger.avro.PostingEventRequest.Builder setPostingId(long value) {
      validate(fields()[0], value);
      this.postingId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'postingId' field has been set.
      * @return True if the 'postingId' field has been set, false otherwise.
      */
    public boolean hasPostingId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'postingId' field.
      * @return This builder.
      */
    public com.example.ledger.avro.PostingEventRequest.Builder clearPostingId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'userId' field.
      * @return The value.
      */
    public java.lang.Long getUserId() {
      return userId;
    }

    /**
      * Sets the value of the 'userId' field.
      * @param value The value of 'userId'.
      * @return This builder.
      */
    public com.example.ledger.avro.PostingEventRequest.Builder setUserId(long value) {
      validate(fields()[1], value);
      this.userId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'userId' field has been set.
      * @return True if the 'userId' field has been set, false otherwise.
      */
    public boolean hasUserId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'userId' field.
      * @return This builder.
      */
    public com.example.ledger.avro.PostingEventRequest.Builder clearUserId() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PostingEventRequest build() {
      try {
        PostingEventRequest record = new PostingEventRequest();
        record.postingId = fieldSetFlags()[0] ? this.postingId : (java.lang.Long) defaultValue(fields()[0]);
        record.userId = fieldSetFlags()[1] ? this.userId : (java.lang.Long) defaultValue(fields()[1]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PostingEventRequest>
    WRITER$ = (org.apache.avro.io.DatumWriter<PostingEventRequest>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PostingEventRequest>
    READER$ = (org.apache.avro.io.DatumReader<PostingEventRequest>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
