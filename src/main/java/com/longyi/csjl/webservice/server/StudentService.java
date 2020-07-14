package com.longyi.csjl.webservice.server;

import com.longyi.csjl.webservice.dto.StudentDTO;
import com.longyi.csjl.webservice.dto.ZT_PARAM;

import javax.jws.WebService;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 16:16
 */
@WebService
public interface StudentService {

   List<StudentDTO> queryStudentInfo(ZT_PARAM zt_param);



}
