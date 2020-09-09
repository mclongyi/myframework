package com.odianyun.search.whale.common.remote;

import com.odianyun.soa.InputDTO;

/**
 * Created by hs on 2018/8/2.
 */
public interface RemoteSoa {


    <T> T call(RemoteSoaServiceEnum remoteSoaServiceEnum, InputDTO<?> inputDTO);

    <T> T call(RemoteSoaServiceEnum remoteSoaServiceEnum, InputDTO<?> inputDTO, String targetUrl);

    <T> T call(RemoteSoaServiceEnum remoteSoaServiceEnum, Object object);
}
