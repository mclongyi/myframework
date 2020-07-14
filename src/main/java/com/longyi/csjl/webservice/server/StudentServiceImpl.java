package com.longyi.csjl.webservice.server;

import com.longyi.csjl.webservice.dto.StudentDTO;
import com.longyi.csjl.webservice.dto.SubjectDTO;
import com.longyi.csjl.webservice.dto.ZT_PARAM;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.naming.spi.ObjectFactory;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 16:19
 */

@Service
@WebService(serviceName = "StudentService",
        targetNamespace = "http://com.longyi.csjl.webservice.server",
        endpointInterface = "com.longyi.csjl.webservice.server.StudentService"
)
public class StudentServiceImpl implements StudentService {

  @WebMethod(
      operationName = "queryStudentInfo",
      action = "http://com.longyi.csjl.webservice.server")
  @WebResult(name = "ZT_STUDENT_INFO.response", targetNamespace = "http://com.longyi.csjl.webservice.dto.StudentDTO", partName = "parameters")
  @Override
  public List<StudentDTO> queryStudentInfo(@WebParam(partName = "parameters", name = "ZT_PARAM.request",
          targetNamespace = "com.longyi.csjl.webservice.dto.ZT_PARAM")ZT_PARAM zt_param) {
        StudentDTO studentDTO=new StudentDTO();
        studentDTO.setAge(20);
        studentDTO.setClassName("六年级");
        studentDTO.setName("张花花");
        studentDTO.setSchoolName("武汉第一中学");
        studentDTO.setWeight(50D);
        SubjectDTO subjectDTO=new SubjectDTO();
        subjectDTO.setSorce(90d);
        subjectDTO.setSubjectName("语文");
        subjectDTO.setSubjectTeacher("刘老师");
        List<SubjectDTO> list=new ArrayList<>();
        list.add(subjectDTO);
        studentDTO.setList(list);

        List studentList=new ArrayList();
        studentList.add(studentDTO);
        return studentList;
    }
}
   