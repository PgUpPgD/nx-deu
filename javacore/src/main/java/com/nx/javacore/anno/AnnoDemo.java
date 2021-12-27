package com.nx.javacore.anno;

import lombok.Data;

@Data
public class AnnoDemo {

    @MyAnno(str = {"y","q","w"}, myenum = ColorEnum.RED, myanno = @MyAnno2("yqw"))
    private String name;
    private String age;

}
