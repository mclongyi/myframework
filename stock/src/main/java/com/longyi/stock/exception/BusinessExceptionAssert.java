package com.longyi.stock.exception;

import java.text.MessageFormat;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/19 11:33
 */
public interface BusinessExceptionAssert extends IResponseEnum,Assert{


    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg, t);
    }

}
