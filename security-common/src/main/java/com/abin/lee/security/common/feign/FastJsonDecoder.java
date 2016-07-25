
package com.abin.lee.security.common.feign;

import static feign.Util.ensureClosed;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonIOException;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

public class FastJsonDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.status() == 404)
            return Util.emptyValueOf(type);
        if (response.body() == null)
            return null;
        Reader reader = response.body().asReader();
        try {
            return JSON.parseObject(IOUtils.toByteArray(reader), type);
        } catch (JsonIOException e) {
            if (e.getCause() != null && e.getCause() instanceof IOException) {
                throw IOException.class.cast(e.getCause());
            }
            throw e;
        } finally {
            ensureClosed(reader);
        }
    }

}
