package com.nx.javacore.generic.effect;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * 保证了类型安全
 * 避免了强转
 * 提高代码重复使用率
 * @param <T>
 */
public class Reuse<T extends Comparable>{

    public Integer compaerTo(T t1,T t2){
        return t1.compareTo(t2);
    }

    @Test
    public void testComara(){
        Reuse<Integer> reuse = new Reuse<>();
        assertTrue(reuse.compaerTo(123,123)==0);

        Reuse<String> stringReuse = new Reuse<>();
        stringReuse.compaerTo("!23","234");


    }

}
