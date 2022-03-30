package com.thoughtworks.airticketorder.config;

import com.thoughtworks.airticketorder.exceptions.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new NotFoundException();
        }
        return new Default().decode(methodKey, response);
    }
}
