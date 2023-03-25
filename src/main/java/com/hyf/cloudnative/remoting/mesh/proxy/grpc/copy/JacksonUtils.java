/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyf.cloudnative.remoting.mesh.exception.DeserializationException;
import com.hyf.cloudnative.remoting.mesh.exception.SerializationException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Json utils implement by Jackson.
 *
 * @author <a href="mailto:liaochuntao@live.com">liaochuntao</a>
 */
public final class JacksonUtils {

    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * Object to json string byte array.
     *
     * @param obj obj
     * @return json string byte array
     * @throws SerializationException if transfer failed
     */
    public static byte[] toJsonBytes(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException(obj.getClass(), e);
        }
    }

    /**
     * Json string deserialize to Object.
     *
     * @param inputStream json string input stream
     * @param cls         class of object
     * @param <T>         General type
     * @return object
     * @throws DeserializationException if deserialize failed
     */
    public static <T> T toObj(InputStream inputStream, Class<T> cls) {
        try {
            return mapper.readValue(inputStream, cls);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

}
