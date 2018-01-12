package online.pangge.exam.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectType extends BaseDomain{

    /**
     * subject type name
     */
    private String typeName;

    /**
     * subject type code
     */
    private String typeCode;
}